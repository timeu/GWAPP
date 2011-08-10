'''
Created on Jun 17, 2011

@author: uemit.seren
'''

import tables
import csv
import time
from datetime import datetime

'''
SQL Statement:
SELECT ecotypeid,nativename,longitude,latitude,country,CONCAT(firstname,' ', surname) as collector,collectiondate INTO OUTFILE '/tmp/250k_accession_list.csv'  from stock.ecotype_info_with_haplogroup LEFT JOIN array_info ON array_info.maternal_ecotype_id = stock.ecotype_info_with_haplogroup.ecotypeid LEFT JOIN call_info ON array_info.id = call_info.array_id WHERE call_info.method_id =75 ;
'''

def importAccessions(hdf5_file,csv_file,delimiter='\t'):
    hdf5_f = None
    csv_f = None
    try:
        hdf5_f = tables.openFile(hdf5_file,"r+")
        group = hdf5_f.root.accessions
        csv_f =  csv.reader(open(csv_file,'rb'),delimiter=delimiter)
        hdf5_f.removeNode(group,'infos',True)
        table = hdf5_f.createTable(group,'infos',Accessions,'Accessions Infos')
        for accession in csv_f:
            row = table.row
            row['accession_id'] = accession[0]
            row['name'] = accession[1]
            if accession[2] != "\\N":
                row['longitude'] = accession[2]
            if accession[3] != "\\N":
                row['latitude'] = accession[3]
            row['country'] = accession[4]
            row['collector'] = accession[5]
            if accession[6] != "\\N": 
                unix_time = int(time.mktime(datetime.strptime(accession[6],"%Y-%m-%d %H:%M:%S").timetuple()))
                print unix_time
            row['collection_date'] = unix_time
            row.append()
        table.flush()
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


