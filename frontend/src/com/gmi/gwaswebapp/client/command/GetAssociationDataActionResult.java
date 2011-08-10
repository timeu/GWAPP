package com.gmi.gwaswebapp.client.command;


import com.gmi.gwaswebapp.client.dto.ResultData;
import com.gwtplatform.dispatch.shared.Result;



public class GetAssociationDataActionResult implements Result {
	private final ResultData resultData;

	public GetAssociationDataActionResult(final ResultData resultData) {
		super();
		this.resultData = resultData;
	}


	public ResultData getResultData() {
		return resultData;
	}

} 




