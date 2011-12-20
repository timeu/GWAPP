package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.google.gwt.http.client.Response;

public class DeleteDatasetAction extends RequestBuilderActionImpl<BaseStatusResult> {

	final String Phenotype;
	final String Dataset;
	final BackendResultReader reader;
	
	public DeleteDatasetAction(String phenotype,String dataset, final BackendResultReader reader) {
		super();
		this.Phenotype = phenotype;
		this.Dataset = dataset;
		this.reader = reader;
	}

	@Override
	public String getUrl() {
		return _getUrl(Phenotype, Dataset);
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

	public static String _getUrl(String Phenotype,String Dataset) {
		return BaseURL + "/deleteDataset?phenotype="+ Phenotype + "&dataset=" + Dataset;
	}
	
}
