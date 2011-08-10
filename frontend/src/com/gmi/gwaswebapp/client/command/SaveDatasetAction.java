package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.gmi.gwaswebapp.client.dto.Readers.DatasetWriter;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

public class SaveDatasetAction extends RequestBuilderActionImpl<BaseStatusResult> {

	

	final Dataset dataset;
	final BackendResultReader reader;
	final DatasetWriter writer;
	
	public SaveDatasetAction(Dataset dataset, final BackendResultReader reader, final DatasetWriter writer) {
		super();
		this.dataset = dataset;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	public String getUrl() {
		return _getUrl();
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

	public static String _getUrl() {
		return BaseURL + "/saveDataset";
	}
	
	
	@Override
	public Method getMethod() {
		return RequestBuilder.POST;
	}

	@Override
	public String getRequestData() {
		return writer.toJson(dataset);
	}
}
