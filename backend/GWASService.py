'''
Created on Nov 16, 2010

@author: uemit.seren
'''

import gviz_api, os
import traceback
import gwa_records
from cherrypy import tools
import cherrypy
import h5py
import numpy, math
from cherrypy.lib.static import serve_file
import simplejson
from JBrowseDataSource import DataSource as JBrowseDataSource 
from cherrypy.lib import http
import tables
import time
from gviz_api import *
import datetime
from gwa_records import ProgressFileWriter
import cPickle

@staticmethod
def SingleValueToJSPerf(value, value_type, escape_func=None):
    if escape_func is None:
        escape_func = gviz_api.DataTable._EscapeValue
    if isinstance(value, tuple):
        # In case of a tuple, we run the same function on the value itself and
        # add the formatted value.
        if (len(value) not in [2, 3] or
          (len(value) == 3 and not isinstance(value[2], dict))):
            raise DataTableException("Wrong format for value and formatting - %s." % 
                                 str(value))
        if not isinstance(value[1], types.StringTypes + (types.NoneType,)):
            raise DataTableException("Formatted value is not string, given %s." % 
                                 type(value[1]))
        js_value = DataTable.SingleValueToJS(value[0], value_type)
        if value[1] is None:
            return (js_value, None)
        return (js_value, escape_func(value[1]))

    # The standard case - no formatting.
    t_value = type(value)
    if value is None:
        return "null"
    if value_type == "boolean":
        if value:
            return "true"
        return "false"
    elif value_type == "number":
        if isinstance(value, (int, long, float)):
            if isinstance(value, float):
                return "%.2f" % value
            else:
                return str(value)
        raise DataTableException("Wrong type %s when expected number" % t_value)

    elif value_type == "string":
        if isinstance(value, tuple):
            raise DataTableException("Tuple is not allowed as string value.")
        return escape_func(value)

    elif value_type == "date":
        if not isinstance(value, (datetime.date, datetime.datetime)):
            raise DataTableException("Wrong type %s when expected date" % t_value)
        # We need to shift the month by 1 to match JS Date format
        return "new Date(%d,%d,%d)" % (value.year, value.month - 1, value.day)

    elif value_type == "timeofday":
        if not isinstance(value, (datetime.time, datetime.datetime)):
            raise DataTableException("Wrong type %s when expected time" % t_value)
        return "[%d,%d,%d]" % (value.hour, value.minute, value.second)

    elif value_type == "datetime":
        if not isinstance(value, datetime.datetime):
            raise DataTableException("Wrong type %s when expected datetime" % 
                                 t_value)
        return "new Date(%d,%d,%d,%d,%d,%d)" % (value.year,
                                              value.month - 1, # To match JS
                                              value.day,
                                              value.hour,
                                              value.minute,
                                              value.second)
    # If we got here, it means the given value_type was not one of the
    # supported types.
    raise DataTableException("Unsupported type %s" % value_type)

gviz_api.DataTable.SingleValueToJS = SingleValueToJSPerf
 

class GWASService:
    base_path = "/srv/data/"
    base_path_jbrowse = base_path
    base_path_datasets = base_path + "datasets/"
    base_jbrowse_path = base_path_jbrowse + "jbrowse/"
    track_folder = "TAIR10"
    
    #tracks/Chr%s/TAIR10/"
    __datasource = None
    _lazyArrayChunks = [{}, {}, {}, {}, {}]
    hdf5_filename = base_path + "data.hdf5"
    genomeStats_hdf5_filename = base_path + '250k_stats.hdf5'
    gene_annot_file = base_path + "genome_annotation.pickled"
    
    def __init__(self):
        self.data_file = tables.openFile(self.hdf5_filename, "r")
        self.accession_ids = None
        gene_annot_file = open(self.gene_annot_file, 'rb')
        self.gene_annotation = cPickle.load(gene_annot_file)
        gene_annot_file.close()
        self.genomestats_file = h5py.File(self.genomeStats_hdf5_filename, 'r')
        self.genome_wide_stats = [{'name':'genecount', 'label':'# Genes', 'isStackable':False, 'isStepPlot':True}, \
                    {'name':'fst', 'label':'Fst (North-South) [Lewontin and Krakhauer, 1973]'}, {'name':'clr', 'label':'CLR [Nielsen et al., 2005]'}, \
                    {'name':'phs', 'label':'PHS [Toomaijan et al., 2006]'}, {'name':'rho', 'label':'RHO [McVean et al., 2004]', 'isStackable':False},{'name':'lyr','label':'Lyrata-similarity [Hu et al., 2011]','isStackable':False}]
        self.supported_transformations = ['', 'log', 'sqrt', 'box_cox']
        
    def _getUserId(self):
        request = cherrypy.request
        return request.cookie.get('GWAS_USER_ID', None)
    
    def _getUserPath(self):
        userID = self._getUserId()
        if userID  is None: 
            raise Exception('No UserID found')  
        path = self.base_path_datasets + userID.value + ".hdf5"
        return path 
    
    def _generateUserID(self):
        import uuid
        UserID = uuid.uuid1()
        return str(UserID)
    
    def _getGeneCountHistogramData(self, chr):
        if self.__datasource == None:
            self.__datasource = JBrowseDataSource(self.base_jbrowse_path, self.track_folder)
        bpPerBin, histogramData = self.__datasource.getGeneHistogram(chr)
        binCount = len(histogramData)
        maxValue = max(histogramData)
        positions = [i * bpPerBin for i in range(0, binCount + 1)]
        return zip(positions, histogramData)
    
    def _getPhenotypeExplorerData(self, phenotype, dataset, transformation, phen_vals=None):
        import datetime
        result = {}
        column_name_type_ls = [("label", ("string", "ID Name Phenotype")), ("date", ("date", "Date")), \
                               ("lon", ("number", "Longitude")), ("lat", ("number", "Latitude")), \
                               ("phenotype", ("number", "Phenotype")), ("name", ("string", "Native Name")), \
                               ("country", ("string", "Country")) ]
        if phen_vals == None:
            phen_vals = self._getPhenotypeValues(phenotype, transformation)
        table = self.data_file.root.accessions.infos
        data = []
        for i, ecotype in enumerate(phen_vals['ecotype']):
            for accession in table.where('(accession_id == %r)' % ecotype):
                value = phen_vals['mean_value'][i]
                label = '%s ID:%s Phenotype:%s.' % (accession['name'], ecotype, value)
                data.append({'label':label, 'date':datetime.date(2009, 2, 3), 'accession_id':ecotype, \
                             'lon':None if math.isnan(accession['longitude']) else accession['longitude'], 'lat':None if math.isnan(accession['latitude']) else accession['latitude'], \
                             'phenotype':value, 'name':accession['name'], 'country':accession['country']})
        data_table = gviz_api.DataTable(dict(column_name_type_ls))
        data_table.LoadData(data)
        column_ls = [row[0] for row in column_name_type_ls]
        result = data_table.ToJSon(columns_order=column_ls)
        return result
    
    def _getAccessionsFromIds(self, accession_ids=[]):
        import bisect
        table = self.data_file.root.accessions.infos
        ids = table.read(field='accession_id')
        indices = [bisect.bisect(ids, val) - 1 for val in accession_ids]
        accessions = table.readCoordinates(indices)
        return accessions
    
         
    
    def _getLocationDistributionFromIds(self, accession_ids=[]):
        import bisect, itertools
        from operator import itemgetter
        if accession_ids == None:
            accessions = self.data_file.root.accessions.infos[:]
        else:
            accessions = self._getAccessionsFromIds(accession_ids).tolist()
        locations = {}
        selector = lambda row:row[4]
        
        for country, rows in itertools.groupby(sorted(accessions, key=itemgetter(4)), selector):
            locations[country] = len(list(rows))
        return locations
    
    def _getAccessionIds(self):
        if self.accession_ids is None: 
            table = self.data_file.root.accessions.infos
            self.accession_ids = table.read(field='accession_id')
        return self.accession_ids 
    
    def _getAccessions(self, start, length, Name='', Country=''):
        
        table = self.data_file.root.accessions.infos
        accessions = []
        stop = int(start) + int(length)
        if stop > table.nrows:
            stop = table.nrows
        isCriteria = False
        if (Name != '') or (Country != ''):
            isCriteria = True
        if isCriteria == False:
            for row in table.iterrows(start=int(start), stop=table.nrows):
                if row['collection_date'] != 0:
                    collection_date = datetime.datetime.fromtimestamp(int(row['collection_date']))
                    collection_date = datetime.datetime.strftime(collection_date, '%d.%m.%Y')
                else:
                    collection_date = None
                accession = {'accession_id':row['accession_id'], 'collection_date':collection_date, 'collector':unicode(row['collector'], 'latin1'), 'country':row['country_ISO'], 'latitude':row['latitude'] if not numpy.isnan(row['latitude']) else None, 'longitude':row['longitude'] if not numpy.isnan(row['longitude']) else None , 'name':unicode(row['name'], 'utf8')}
                accessions.append(accession)
            count = table.nrows
        else:
            i = 0
            for row in table:
                if (Name == '' or Name.lower() in unicode(row[6], 'utf8').lower()) and (Country == '' or Country.lower() in row[3].lower()):
                    if i >= start and i <= stop:
                        collection_date = datetime.fromtimestamp(int(row[1]))
                        collection_date = datetime.strftime(collection_date, '%d.%m.%Y')
                        accession = {'accession_id':row[0], 'collection_date':collection_date, 'collector':unicode(row[2], 'latin1'), 'country':row[4], 'latitude':row[4] if not numpy.isnan(row[4]) else None, 'longitude':row[5] if not numpy.isnan(row[5]) else None, 'name':unicode(row[6], 'utf8')}
                        
                        accessions.append(accession)
                    i = i + 1
            count = i
        return [accessions, count]
    
    def _getPhenotypes(self, userID=None):
        phenotypes = []
        try:
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            if not gwa_record.is_file_exists():
                return phenotypes
            phenotypes = gwa_record.get_phenotype_info()
        except Exception, err:
            raise cherrypy.HTTPError("500", str(err))
        return phenotypes  
    
    def _getPhenotypeValues(self, phenotype, transformation):
        path = self._getUserPath()
        gwa_record = gwa_records.GWASRecord(path)
        gwa_record.open("r+")
        values = gwa_record.get_phenotype_values(phenotype, transformation)
        return values
        
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getUserData(self, loadAccessions=None):
        userID = self._getUserId()
        if userID is None:
            userID = self._generateUserID()
            phenotypes = []
        else:
            userID = userID.value
            phenotypes = self._getPhenotypes(userID)
            
        retval = {'userid':userID, 'phenotypes':phenotypes}
        if loadAccessions is not None:
            accessions, count = self._getAccessions(0, -1)
            retval['accessions'] = {'accessions':accessions, 'count':count}
        retval['supportedTransformations'] = self.supported_transformations
        return retval
    
    
    
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getPhenotypeInfo(self, phenotype, transformation=None, userID=None):
        try:
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            result = {'phenotype':gwa_record.get_phenotype_info(phenotype)[0]}
            column_name_type_ls = [("x_axis", ("string", "Phenotype Value")), \
                            ("frequency", ("number", "Frequency"))]
            description = dict(column_name_type_ls)
            data_table = gviz_api.DataTable(description)
            data_table.LoadData(gwa_record.get_phenotype_bins(phenotype, transformation))
            column_ls = [row[0] for row in column_name_type_ls]
            result['phenotypeTable'] = data_table.ToJSon(columns_order=column_ls)
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        if result['status'] == 'ERROR':
            raise cherrypy.HTTPError("500", result['statustext'])
        return result
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getTransformation(self, phenotype, dataset, transformation): 
        path = self._getUserPath()
        gwa_record = gwa_records.GWASRecord(path)
        gwa_record.open("r+")
        phen_vals = gwa_record.get_phenotype_values(phenotype, dataset, transformation)
        sp_pval = None
        if 'sp_pval' in phen_vals:
            sp_pval = phen_vals['sp_pval']
        column_name_type_ls = [("x_axis", ("string", "Phenotype Value")), \
                          ("frequency", ("number", "Frequency"))]
        description = dict(column_name_type_ls)
        data_table = gviz_api.DataTable(description)
        try:
            phen_bins = gwa_record.get_phenotype_bins(phenotype, transformation, phen_vals=phen_vals['mean_value'])
            data_table.LoadData(phen_bins)
        except:
            test = ''
        column_ls = [row[0] for row in column_name_type_ls]
        result = {'transformationTable':data_table.ToJSon(columns_order=column_ls)}
        result['sp_pval'] = sp_pval if sp_pval == 0 or sp_pval is None else round(-math.log10(sp_pval), 2)
        result['motionchartTable'] = self._getPhenotypeExplorerData(phenotype, dataset, transformation, phen_vals)
        return result
            
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getAssociationData(self, phenotype, dataset, transformation, analysis, result_name=None, userID=None):
        try:
            import math
            if result_name == None:
                result_name = analysis
            result = {}
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            association_result = gwa_record.get_results_by_chromosome(phenotype, dataset, analysis.lower(), result_name, transformation)
            
            description = [('position', "number", "Position"), ('value', 'number', '-log Pvalue')]
            chr2data = {}
            for i in range(1, 6):
                data = zip(association_result[i]['position'], association_result[i]['score'])
                data.sort()
                data_table = gviz_api.DataTable(description)
                data_table.LoadData(data)
                chr2data[i] = data_table.ToJSon(columns_order=("position", "value")) 
            result['chr2data'] = chr2data
            result['chr2length'] = association_result['chromosome_ends']
            result['max_value'] = association_result['max_score']
            if 'no_of_tests' in association_result:
                result['bonferroniThreshold'] = -math.log10(1.0 / (association_result['no_of_tests'] * 20.0))
            else:
                result['bonferroniThreshold'] = -math.log10(1.0 / (214000.0 * 20.0))
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        return result
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def deleteTransformation(self, phenotype, dataset, transformation, userID=None):
        try:
            result = {}
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            gwa_record.delete_transformation(phenotype, dataset, transformation)
            result = {"status":"OK", "statustext":""}
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        return result
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def deleteAnalysis(self, phenotype, dataset, transformation, analysis, userID=None):
        try:
            result = {}
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            gwa_record.delete_analysis(phenotype, dataset, transformation, analysis.lower())
            result = {"status":"OK", "statustext":""}
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        return result
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def deleteResult(self, phenotype, dataset, transformation, analysis, result_name, userID=None):
        try:
            result = {}
            #result ={"status":"OK","statustext":""}
            #return result
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            gwa_record.delete_result(phenotype, dataset, transformation, analysis.lower(), result_name)
            result = {"status":"OK", "statustext":""}
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        return result
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def deletePhenotype(self, phenotype, userID=None):
        try:
            result = {}
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            gwa_record.delete_phenotype(phenotype)
            result = {"status":"OK", "statustext":""}
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        return result
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def check_gwas(self):
        result = {'status':'OK'}
        path = self._getUserPath()
        gwa_record = gwa_records.GWASRecord(path)
        gwa_record.open("r+")
        check = gwa_record.check_run_count()
        if check is not None:
            result['status'] = 'WARNING'
            result['statustext'] = check
        return result
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def run_gwas(self, phenotype, dataset, transformation, analysis, result_name=None, chromosome=None, position=None, showProgress=None):
        try:
            response = cherrypy.response
            response.timeout = 3600
            result = {}
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            base_path = '/Network/Data/250k/db/dataset/'
            snps_data_file = base_path + '250K_t54.csv.binary'
            kinship_data_file = base_path + 'kinship_matrix_cm54.pickled'
            data = {}
            result = {}
            progress_filename = '/tmp/%s_%s_%s_%s_%s.log' % (phenotype, dataset, transformation, analysis, self._getUserId())
            if showProgress is not None:
                if not os.path.isfile(progress_filename):
                    result['progress'] = 100
                else:
                    showProgress_writer = ProgressFileWriter(progress_filename, "r")
                    
                    result['progress'] = showProgress_writer.get_progress()
                    result['currentTask'] = showProgress_writer.get_task_status()
                    result['status'] = "OK"
            else:
                showProgress_writer = ProgressFileWriter(progress_filename)
                if chromosome is not None and position is not None:
                    result_name = gwa_record.perform_stepwise_gwas(phenotype, dataset, transformation.lower(), analysis.lower(), result_name, chromosome, position, progress_file_writer=showProgress_writer)
                else:
                    result_name = gwa_record.perform_gwas(phenotype, dataset, transformation.lower(), analysis.lower(), progress_file_writer=showProgress_writer)
                result = {"status":"OK", "statustext":"", 'phenotypes':gwa_record.get_phenotype_info(), 'result_name':result_name}
                showProgress_writer.close_file()
                os.remove(progress_filename)
        except Exception, err:
            result = {'status':'ERROR', 'statustext':str(err)}
        finally:
            if showProgress is None:
                gwa_record.dec_run_count()
        return result
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getQQPlotData(self, phenotype, dataset, transformation, analysis=None):
        try:
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            column_name_type_ls = [('x_axis', "number", "Expected (p)")]
            data = gwa_record.getQQPlotData(phenotype, transformation, analysis)
            for results in data['observed']:
                column_name_type_ls.append((results['name'], ("number", "Observer (p) - %s" % results['name'])))
                
            description = dict(column_name_type_ls)
            data_table = gviz_api.DataTable(description)
            data_table.LoadData(zip(data['expected'], *data['observerd'].values()))
            column_ls = [row[0] for row in column_name_type_ls]
            result = {'status':'OK', 'data':data_table.ToJSon(columns_order=column_ls)}
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        return result 
    
    @cherrypy.expose
    @cherrypy.tools.response_headers(headers=[('Content-Type', 'image/png')])
    def getQQPlotImage(self, phenotype, dataset, transformation, analysis=None, result_name='results'):
        path = self._getUserPath()
        gwa_record = gwa_records.GWASRecord(path)
        gwa_record.open("r+")
        image = gwa_record.get_qq_plots(phenotype, dataset, transformation, analysis, result_name)
        return image
        
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getTransformationPreview(self, phenotype, dataset, transformation, new_transformation, userID=None):
        try:
            result = {}
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            column_name_type_ls = [("x_axis", ("string", "Phenotype Value")), \
                            ("frequency", ("number", "Frequency"))]
            description = dict(column_name_type_ls)
            data_table = gviz_api.DataTable(description)
            phen_bins, sp_pval = gwa_record.preview_transform_phenotype(phenotype, dataset, new_transformation, transformation)
            data_table.LoadData(phen_bins)
            column_ls = [row[0] for row in column_name_type_ls]
            result['transformationTable'] = data_table.ToJSon(columns_order=column_ls)
            result['sp_pval'] = sp_pval if sp_pval == 0 or sp_pval is None else round(-math.log10(sp_pval), 2)
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        return result
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def saveNewTransformation(self, phenotype, dataset, transformation, new_transformation, userID=None):
        try:
            result = {}
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            if gwa_record.exists_transformation(phenotype, dataset, new_transformation):
                raise Exception('Transformation already exists');
            gwa_record.transform_phenotype(phenotype, dataset, new_transformation, transformation, store=True)
            result = {"status":"OK", "statustext":""}
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        return result
    
    @cherrypy.expose
    def uploadPhenotype(self, phenotype_file=None, phenotype_content=None):
        import tempfile
        try:
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            temp_file = tempfile.NamedTemporaryFile(delete=False)
            if phenotype_file.file is not None:
                temp_file.file.write(phenotype_file.file.read())
                
            elif phenotype_content is not None:
                temp_file.file.write(phenotype_content)
            else:
                raise Exception('You have to specify either file or content')
            temp_file.close()
            retval = gwa_record.add_phenotype_file(phen_file_name=temp_file.name, ecotype_ids=map(str, self._getAccessionIds()))
            os.unlink(temp_file.name)
        except Exception, err:
            retval = {"status":"ERROR", "statustext":"%s" % str(err)}
        return simplejson.dumps(retval)
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getGenes(self, chromosome, start, end, isFeatures=''):
        try:
            if self.__datasource == None:
                self.__datasource = JBrowseDataSource(self.base_jbrowse_path, self.track_folder)
            genes = []
            genes = self.__datasource.getGenes(chromosome, int(start), int(end), bool(isFeatures))
            retval = {'status': 'OK', 'genes':genes}
        except Exception, err:
            retval = {"status":"ERROR", "statustext":"%s" % str(err)}
        return retval
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getGeneFromName(self, query):
        try:
            if self.__datasource == None:
                self.__datasource = JBrowseDataSource(self.base_jbrowse_path, self.track_folder)
            genes = []
            gene = self.__datasource.getGeneFromName(query)
            retval = {'status': 'OK', 'gene':gene}
        except Exception, err:
            retval = {"status":"ERROR", "statustext":"%s" % str(err)}
        return retval
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getGenesFromQuery(self, query):
        try:
            if self.__datasource == None:
                self.__datasource = JBrowseDataSource(self.base_jbrowse_path, self.track_folder)
            genes = []
            genes = self.__datasource.getGenesFromQuery(query)
            isMore = False
            count = 0
            if len(genes) > 20:
                count = len(genes)
                genes = genes[0:20]
                isMore = True
            retval = {'status': 'OK', 'isMore':isMore, 'count':count, 'genes':genes}
        except Exception, err:
            retval = {"status":"ERROR", "statustext":"%s" % str(err)}
        return retval
    
    
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getGeneDescription(self, gene):
        try:
            gene_parts = gene.split('.')
            description = self.gene_annotation[gene_parts[0]][gene]['functional_description']['computational_description']
            retval = {'status': 'OK', 'description':description}
        except Exception, err:
            retval = {"status":"ERROR", "statustext":"%s" % str(err)}
        return retval
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getAccessions(self, start, length, Name='', Country=''):
        accessions, count = self._getAccessions(int(start), int(length), Name, Country)
        retval = {'accessions':accessions, 'count':count}
        return retval
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getPhenotypeExplorerData(self, phenotype, transformation):
        retval = self._getPhenotypeExplorerData(phenotype, transformation)
        return retval
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getLocationDistributionData(self, phenotype=None, dataset=None):
        path = self._getUserPath()
        gwa_record = gwa_records.GWASRecord(path)
        gwa_record.open("r+")
        retval = {}
        column_name_type_ls = [("Country", ("string", "Country")), \
                            ("Count", ("number", "Count"))]
        column_ls = [row[0] for row in column_name_type_ls]

        if phenotype == None and dataset == None:
            accession_ids_ls = {'All':None}
        else:
            accession_ids_ls = gwa_record.get_dataset_accession_ids(phenotype, dataset)
        
        for dataset, accession_ids in accession_ids_ls.iteritems():
            location_dist = self._getLocationDistributionFromIds(accession_ids)
            data = []
            for country, count in location_dist.iteritems():
                data.append({'Country':country, 'Count':count})
            data_table = gviz_api.DataTable(dict(column_name_type_ls))
            data_table.LoadData(data)
            retval[dataset] = data_table.ToJSon(columns_order=column_ls)
        return retval
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getDatasetAccession(self, phenotype, dataset, transformation='raw'):
        path = self._getUserPath()
        gwa_record = gwa_records.GWASRecord(path)
        gwa_record.open("r+")
        accession_ids_ls = gwa_record.get_dataset_accession_ids(phenotype, dataset)
        
    @cherrypy.expose
    @cherrypy.tools.json_out()
    @cherrypy.tools.json_in()
    def saveDataset(self):
        retval = {'status':"ERROR", 'statustext':''}
        path = self._getUserPath()
        dataset = cherrypy.request.json
        gwa_record = gwa_records.GWASRecord(path)
        gwa_record.open("r+")
        retval = gwa_record.save_dataset(dataset)
        if retval['status'] != 'OK':
            raise Exception(retval['statustext']) 
        return retval
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def deleteDataset(self, phenotype, dataset, userID=None):
        try:
            result = {}
            path = self._getUserPath()
            gwa_record = gwa_records.GWASRecord(path)
            gwa_record.open("r+")
            gwa_record.delete_dataset(phenotype, dataset)
            result = {"status":"OK", "statustext":""}
        except Exception, err:
            result = {"status":"ERROR", "statustext":"%s" % str(err)}
        return result

    @cherrypy.expose
    @cherrypy.tools.response_headers(headers=[('Content-Type', 'application/csv'), ('Content-disposition', 'attachment;filename=results.csv')])
    def downloadAssociationData(self, phenotype, dataset, transformation, analysis, result_name):
        import StringIO, csv
        path = self._getUserPath()
        gwa_record = gwa_records.GWASRecord(path)
        gwa_record.open("r+")
        results = gwa_record.get_results_for_csv(phenotype, dataset, transformation, analysis, result_name)
        content = ''
        tempfile = StringIO.StringIO()
        writer = csv.writer(tempfile, delimiter=',')
        writer.writerows(results)
        content = tempfile.getvalue()
        tempfile.close()
        return content;
    
    @cherrypy.expose
    def downloadHDF5File(self):
        path = self._getUserPath()
        return serve_file(path, "application/x-download", "attachment", 'analysis.hdf5')
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getGenomeStatsList(self):
        try:
            retval = {'status': 'OK', 'stats':self.genome_wide_stats}
        except Exception, err:
            retval = {"status":"ERROR", "statustext":"%s" % str(err)}
        return retval
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def getGenomeStatsData(self, stats, chr):
        try:
            if chr == "null":
                chr = "Chr1"
            description = [('position', "number", "Position")]
            stats = stats.split(',')
            if 'genecount' in stats:
                description.append(('genecount', "number", "# genes"))
                data = self._getGeneCountHistogramData(chr)
            else:
                chr_numbers = ['chr1', 'chr2', 'chr3', 'chr4', 'chr5']
                chr_num = chr_numbers.index(chr.lower())
                if 'rho' in stats:
                    group = self.genomestats_file['RecombStats']
                    stats_ix = [0]
                elif 'lyr' in stats:
                    group = self.genomestats_file['lyrata_similarity']
                    stats_ix = [0]
                else:
                    group = self.genomestats_file['GeneStats']
                    stats_list = group.attrs['headers']
                    stats_ix = [numpy.where(stats_list == stat)[0][0] for stat in stats]
                    stats_ix.sort()
                    stats = stats_list[stats_ix]
                chr_region = group.attrs['chr_regions'][chr_num]
                stats_values = group['stats'][chr_region[0]:chr_region[1], stats_ix]
                positions = group['positions'][chr_region[0]:chr_region[1], 1]
                
                for stat in stats:
                    stat_label = stat
                    stat_data = next((genome_stat for genome_stat in self.genome_wide_stats if genome_stat['name'] == stat), None)
                    if stat_data is not None and 'label' in stat_data:
                        stat_label = stat_data['label']
                    description.append((stat, "number", stat_label))
                stats_values_trans = stats_values.transpose().tolist()
                stats_values_trans_filtered = [map(lambda value: None if  math.isnan(value) else value, values) for values in stats_values_trans]
                data = zip(positions.tolist(), *(stats_values_trans_filtered))
            data_table = gviz_api.DataTable(description)
            data_table.LoadData(data)
            retval = {'status': 'OK', 'data':data_table.ToJSon()}
        except Exception, err:
            retval = {"status":"ERROR", "statustext":"%s" % str(err)}
        return retval
    
    @cherrypy.expose()
    def getProgressBarHTML(self):
        retval = """
        <html>
        <head>
        <STYLE type="text/css">
             .progress_bar{
            -webkit-border-radius: 4px 4px 4px 4px;
            -moz-border-radius: 4px 4px 4px 4px;
            border-radius: 4px 4px 4px 4px;
            float:left;
            width:100%;
            height:24px;
            overflow:hidden;
            padding:1px;
            position:relative;
            background: literal("-webkit-gradient(linear, left top, left bottom, color-stop(0, #D6D5D4), color-stop(1, #E5E5E4)) !important");
            background: literal("-moz-linear-gradient(#D6D5D4 0%, #E5E5E4 100%) !important");
            background: literal("-o-linear-gradient(#D6D5D4 0%, #E5E5E4 100%) !important");
        }
        
        
        .progress_bar_bg {
            -webkit-border-radius: 4px 4px 4px 4px;
            -moz-border-radius: 4px 4px 4px 4px;
            border-radius: 4px 4px 4px 4px;
            padding-right: 2px;
            width:100%;
            height:100%;
            position: relative;
            background: #B7B5B3;
            background: literal("-webkit-gradient(linear, left top, left bottom, color-stop(0, #ABA9A7), color-stop(.3, #B7B5B3), color-stop(1, #B7B5B3)) !important");
            background: literal("-moz-linear-gradient(#ABA9A7 0%, #B7B5B3 30%, #B7B5B3 100%) !important");
            background: literal("-o-linear-gradient(#ABA9A7 0%, #B7B5B3 30%, #B7B5B3 100%) !important");
            -webkit-box-shadow: inset 0px 1px 4px 0px rgba(0, 0, 0, 0.5), 0px 1px 0px 0px rgba(255, 255, 255, .5);
            -moz-box-shadow: inset 0px 1px 4px 0px rgba(0, 0, 0, 0.5), 0px 1px 0px 0px rgba(255, 255, 255, .5);
            box-shadow: inset 0px 1px 4px 0px rgba(0, 0, 0, 0.5), 0px 1px 0px 0px rgba(255, 255, 255, .5);
        }
        
        .progress_bar_status {
            transition: width 2s;
            -moz-transition: width 2s; /* Firefox 4 */
            -webkit-transition: width 2s; /* Safari and Chrome */
            -o-transition: width 2s; /* Opera */
            -webkit-border-radius: 4px 4px 4px 4px;
            -moz-border-radius: 4px 4px 4px 4px;
            border-radius: 4px 4px 4px 4px;
            background: literal("-moz-linear-gradient(#6DA8EF 0%, #3776B4 100%) repeat scroll 0 0 transparent !important");
            box-shadow: 1px -1px 0 0 #8A8987;
            margin: 1px 0 0 1px;
            overflow: hidden;
            position: absolute;
            top: 1px;
            width: 0;

        }
        
        .progress_bar_status_complete {
            color: green;
            /* background: #E5FDD0; */
        }
        
        .progress_content {
            border-right:2px solid transparent;
            position:relative;
            top: -25px;
        }
        
        .progress_inner {
             background: none repeat scroll 0 0 #5993CE;
            height: 23px;
        }
        
        .progress_highlight {
            border-radius: 100% 100% 100% 100%;
            box-shadow: 0 12px 16px -4px rgba(255, 255, 255, 0.4), 0 40px 8px -8px rgba(255, 255, 255, 0.4);
            height: 32px;
            position: absolute;
            top: -32px;
            width: 100%;
        }
        .progress_border_l_r {
            padding: 1px 0;
            width: 1px;    
        }
        .progress_border {
            left:0;
            position:absolute;
            top:0;
        }
        
        .progress_border_left div, .progress_border_right div {
             background: literal("-moz-linear-gradient(#67A5EC 0%, #3776B4 100%) repeat scroll 0 0 transparent !important");
            height: 21px;
        }
        
        .progress_border_bg {
            top:22px;
            width:100%;
        }
        
        .progress_border_bt {
            width:100%;
        }
        
        .progress_border_bt div {
            background: #D8E8F7;
            background: literal("-webkit-gradient(linear, left top, right top, color-stop(0, #6CA8EF), color-stop(.5, #D8E8F7), color-stop(1, #6CA8EF))!important");
            background: literal("-moz-linear-gradient(left, #6CA8EF 0%, #D8E8F7 50%, #6CA8EF 100%) !important");
            background: literal("-o-linear-gradient(left, #6CA8EF 0%, #D8E8F7 50%, #6CA8EF 100%) !important");
            height: 1px;
            margin: 0 1px;
        }
        
        .progress_border_bg div {
             background: none repeat scroll 0 0 #3776B4;
             height:1px;
             margin 0 1px;
        }
        
        .progress_border_right {
            left:auto;
            right:0;
        }
        .progress_label {
            text-shadow: 1px 1px 0 rgba(255, 255, 255, 0.5);
            position: absolute;
            line-height: 23px;
            top: 2px;
            left: 50%;
        }
        .progress_bar_task {
            transition: all 2s;
            -moz-transition: all 2s; /* Firefox 4 */
            -webkit-transition: all 2s; /* Safari and Chrome */
            -o-transition: all 2s; /* Opera */
            text-shadow: 1px 1px 0 rgba(255, 255, 255, 0.5);
            left:50%;
        }
        
        .progress_bar_container {
            opacity : 1;
            transition: opacity 1s;
            -moz-transition: opacity 1s; /* Firefox 4 */
            -webkit-transition: opacity 1s; /* Safari and Chrome */
            -o-transition: opacity 1s; /* Opera */
        }
        
        .progress_bar_container_visible {
            opacity: 1;
        }
         </STYLE>
         <script type="text/javascript">
                
                window.addEventListener("storage", function(e) {
                    var progress_bar_status = document.getElementById('progress_bar_status');
                    var progress_bar_label = document.getElementById('progress_label');
                    var progress_bar_task = document.getElementById('progress_bar_task');
                    var key = e.key;
                    var value = e.newValue;
                    if (key == 'progress') {
                        if (value == null || value == '')
                            value = '100';
                        if (value == '100') {
                            progress_bar_task.innerHTML = 'FINISHED';
                        }
                        progress_bar_status.style.width = value + '%';
                        progress_bar_label.innerHTML=value+'%';
                    }
                    else if (key == 'task') {
                        if (value == null || value == '')  {
                            value = 'FINISHED';
                            progress_bar_task.style.color='green';
                            progress_bar_task.style.font-weight='bold';
                        }
                        progress_bar_task.innerHTML = value;
                    }
                }, false);
                
        function refresh() {
           
           document.getElementById('progress_bar_task').innerHTML = 'test';
        }
         </script>
        </head>
        """
        retval2 = """
        <body style="font-family:Arial Unicode MS, Arial, sans-serif;font-size:small;";>
        <div class="container" style="width: 350px; height: 50px;">
            <div class="progress_bar_container" style="width: 280px; height: 100%%;">
                <div> 
                    <span class="progress_bar_task" id="progress_bar_task">&nbsp;</span> 
                </div> 
                <div class="progress_bar"> 
                   <div class="progress_bar_bg">  </div> 
                   <div class="progress_content"> 
                      <div class="progress_bar_status" id="progress_bar_status" style="width: 0%;">
                         <div class="progress_inner"> 
                         <div class="progress_highlight">
                             <div>  </div>
                        </div> 
                        <div class="progress_border_left progress_border_l_r progress_border"><div>
                      </div>
                    </div> 
                    <div class="progress_border_right progress_border_l_r progress_border">
                       <div>
                    </div>
                  </div> 
                  <div class="progress_border_bg progress_border">
                     <div></div>
                  </div> 
                </div> 
                <div class="progress_border progress_border_bt">
                   <div> </div>
                </div>
              </div> 
              <span class="progress_label" id="progress_label">0%</span> 
            </div> 
        </div>
    </div>
</div>
        </body>
        </html>
        """ 
        return retval + retval2
    
    @cherrypy.expose
    @cherrypy.tools.json_out()
    def testProgress(self, test=None, showProgress=None):
        retval = {'status':'OK', 'statustext':'', 'progress':'', 'currentTask':''}
        progress_file_name = "/tmp/fifo"
        f = None
        try:
            if not os.path.isfile(progress_file_name):
                retval['progress'] = 100
                return retval
            f = open(progress_file_name, "r")
            progress_text = f.read()
            retval['progress'], retval['currentTask'] = progress_text.split('\n')
        except Exception, err:
            retval['status'] = "ERROR"
            retval['statustext'] = '%s' % str(err)
        if f is not None:
            f.close()
        return retval
   
