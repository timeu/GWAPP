package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.GWASResult;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.google.gwt.http.client.Response;

public class CheckGWASAction extends RequestBuilderActionImpl<CheckGWASActionResult>  {

	private static String URL = "/gwas/check_gwas";
	private final BackendResultReader backendResultReader;
	
	
	public CheckGWASAction(final BackendResultReader backendResultReader) {
		this.backendResultReader = backendResultReader;
	}
	
	@Override
	public String getUrl() {
		return URL;
	}

	@Override
	public CheckGWASActionResult extractResult(Response response) {
		BackendResult  result = null;
		if (response.getStatusCode() != 200)
			result = new BackendResult(GWASResult.STATUS.ERROR,"Server Error. Statuscode: "+response.getStatusCode());
		else {
			result = backendResultReader.read(response.getText());
		}
		return new CheckGWASActionResult(result);
	}
	
	public static String _getUrl(String Phenotype,String Dataset,String Transformation,Analysis.TYPE Analysis,String ResultName,Integer Chromosome,Integer Position)
	{
		String URL =  BaseURL + "/run_gwas?phenotype="+ Phenotype + "&dataset="+Dataset+"&transformation=" + Transformation + "&analysis="+Analysis;
		if (Chromosome != null && Position != null)
			URL+="&chromosome="+Chromosome+"&position="+Position;
		if (ResultName != null)
			URL+="&result_name="+ResultName;
		return URL;
	}

}
