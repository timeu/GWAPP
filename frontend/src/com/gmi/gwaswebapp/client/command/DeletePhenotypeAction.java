package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.google.gwt.http.client.Response;

public class DeletePhenotypeAction extends RequestBuilderActionImpl<BaseStatusResult> {

	final String Phenotype;
	private final BackendResultReader reader;
	
	public DeletePhenotypeAction(String phenotype,BackendResultReader reader) {
		super();
		this.reader = reader;
		this.Phenotype = phenotype;
	}

	@Override
	public String getUrl() {
		return _getUrl(Phenotype);
	}

	@Override
	public BaseStatusResult extractResult(Response response) {
		BackendResult result = null;
		if (response.getStatusCode() != 200)
			result = new BackendResult(BackendResult.STATUS.ERROR,"Server Error. Statuscode: "+response.getStatusCode());
		else
			result = reader.read(response.getText());
		return new BaseStatusResult(result);
	}
	
	public static String _getUrl(String Phenotype) {
		return BaseURL + "/deletePhenotype?phenotype="+ Phenotype;
	}
}
