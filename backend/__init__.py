'''
Created on Nov 16, 2010

@author: uemit.seren
'''

import os.path
import GWASService
import cherrypy
from cherrypy import _cperror

current_dir = os.path.dirname(os.path.abspath(__file__))
config_file = os.path.join(current_dir, 'development.conf')



def handle_error():
    cherrypy.response.status = 500
    cherrypy.response.body = "Unexpected Server Error"
    

def error_page_app(status, message, traceback, version):
    return str(message)

cherrypy.config.update({'error_page.500': error_page_app})

class Root:
    _cp_config = {'request.error_response': handle_error}
    
    @cherrypy.expose
    def index(self,datasetkey=None):
        if datasetkey is not None:
            cookie = cherrypy.response.cookie
            cookie['GWAS_USER_ID'] = datasetkey
            cookie['GWAS_USER_ID']['path'] = '/'
            cookie['GWAS_USER_ID']['max-age'] = 3600*60*24
            cookie['GWAS_USER_ID']['version'] = 1
        raise cherrypy.HTTPRedirect("/index.html")




  

root = Root()
root.gwas = GWASService.GWASService()

#cherrypy.tree.mount(root,config=config_file)


if __name__ == '__main__':
    cherrypy.quickstart(root,config=config_file)


