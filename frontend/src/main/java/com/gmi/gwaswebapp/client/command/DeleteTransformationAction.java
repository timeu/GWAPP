package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.google.gwt.http.client.Response;


public class DeleteTransformationAction extends RequestBuilderActionImpl<BaseStatusResult> {

	final String Phenotype;
	final String Dataset;
	final String Transformation;
	final BackendResultReader reader;
	
	public DeleteTransformationAction(String phenotype,String Dataset,String transformation, final BackendResultReader reader) {
		super();
		this.Phenotype = phenotype;
		this.Dataset = Dataset;
		this.Transformation = transformation;
		this.reader = reader;
	}

	@Override
	public String getUrl() {
		return _getUrl(Phenotype, Dataset,Transformation);
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

	public static String _getUrl(String Phenotype,String Dataset,String Transformation) {
		return BaseURL + "/deleteTransformation?phenotype="+ Phenotype + "&dataset="+Dataset+"&transformation=" + Transformation;
	}
	
}
