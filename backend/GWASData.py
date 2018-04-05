'''
Created on Jun 17, 2011

@author: uemit.seren
'''

import tables
import csv
import time
from datetime import datetime
import pdb

'''
SQL Statement:
SELECT ecotypeid,nativename,longitude,latitude,country,CONCAT(firstname,' ', surname) as collector,collectiondate INTO OUTFILE '/tmp/250k_accession_list.csv'  from stock.ecotype_info_with_haplogroup LEFT JOIN array_info ON array_info.maternal_ecotype_id = stock.ecotype_info_with_haplogroup.ecotypeid LEFT JOIN call_info ON array_info.id = call_info.array_id WHERE call_info.method_id =75 ;
'''

ISO_COUNTRY = {'USA':'US','POR':'PT','FRA':'FR','UK':'UK','SWE':'SW','CZE':'CZ','GER':'DE','ESP':'ES',
               'NED':'NL','SUI':'CH','CAN':'CA','AUT':'AT','JPN':'JP','FIN':'FI','RUS':'RU','BEL':'BE',
               'ITA':'IT','IND':'IN','TJK':'TJ','KAZ':'KZ','DEN':'DK','NOR':'NO','ROU':'RO','LBY':'LY',
               'POL':'PL','IRL':'IE','LTU':'LT','UKR':'UA','CPV':'CV','NZL':'NZ','UNK':None,'AZE':'AZ',
               'GEO':'GE','SRB':'RS','TUR':'TR','ARM':'AM','BUL':'BG','MAR':'MA','AFG':'AF'}

def importAccessions(hdf5_file,csv_file,delimiter='\t'):
    hdf5_f = None
    csv_f = None
    try:
        pdb.set_trace()
        accessions =[]
        hdf5_f = tables.open_file(hdf5_file,"r+")
        if 'accessions' not in hdf5_f.root:
            group = hdf5_f.createGroup('/','accessions')
        else:
            group = hdf5_f.root.accessions
        csv_f =  csv.reader(open(csv_file,'rb'),delimiter=delimiter)
        if 'infos' in hdf5_f.root.accessions:
            hdf5_f.removeNode(group,'infos',True)
        table = hdf5_f.createTable(group,'infos',Accessions,'Accessions Infos')
        for accession in csv_f:
            id = int(accession[0])
            name = accession[1]
            longitude =  float(accession[2]) if accession[2] != "\\N" else None
            latitude =  float(accession[3]) if accession[3] != "\\N" else None
            country = accession[4]
            country_ISO = ISO_COUNTRY[country]
            collector = accession[5]
            collection_date =  int(time.mktime(datetime.strptime(accession[6],"%Y-%m-%d %H:%M:%S").timetuple())) if accession[6] != "\\N" else 0
            dataset = accession[7]
            accessions.append((id,collection_date,collector,country,country_ISO,dataset,latitude,longitude,name))
        pdb.set_trace()
        accessions.sort(key=lambda x:x[0])
        table.append(accessions)
        pdb.set_trace()
        table.cols.accession_id.createIndex()
        table.flush()
        pdb.set_trace()
    except Exception,err:
        print str(err)
    if hdf5_f is not None:
        hdf5_f.close()
    

class Accessions(tables.IsDescription):

    accession_id = tables.Int32Col()
    name = tables.StringCol(16)
    latitude = tables.Float32Col()
    longitude = tables.Float32Col()
    country = tables.StringCol(256)
    country_ISO = tables.StringCol(3)
    collector = tables.StringCol(256)
    collection_date = tables.Time32Col()
    dataset = tables.StringCol(256)


