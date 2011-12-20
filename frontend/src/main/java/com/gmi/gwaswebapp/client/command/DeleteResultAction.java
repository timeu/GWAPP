package com.gmi.gwaswebapp.client.command;


import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.google.gwt.http.client.Response;

public class DeleteResultAction extends RequestBuilderActionImpl<BaseStatusResult> {

	public final String Phenotype;
	public final String Transformation;
	public final String Analysis;
	public final String ResultName;
	private final BackendResultReader reader;
	
	public DeleteResultAction(String phenotype,String transformation,String analysis,String resultname,final BackendResultReader reader) {
		super();
		this.Phenotype = phenotype;
		this.Transformation = transformation;
		this.Analysis = analysis;
		this.ResultName = resultname;
		this.reader = reader;
	}

	@Override
	public String getUrl() {
		return _getUrl(Phenotype, Transformation, Analysis,ResultName);
	}

	@Override
	public BaseStatusResult extractResult(Response response) {
		BackendResult  result = null;
		if (response.getStatusCode() != 200)
			result = new BackendResult(BackendResult.STATUS.ERROR,"Server Error. Statuscode: "+response.getStatusCode());
		else
			result = reader.read(response.getText());
		return new BaseStatusResult(result);
	}
	
	public static String _getUrl(String Phenotype,String Transformation,String Analysis,String ResultName) {
		return BaseURL + "/deleteResult?phenotype="+ Phenotype + "&transformation=" + Transformation + "&analysis="+Analysis+"&result_name=" +ResultName;
	}
}

