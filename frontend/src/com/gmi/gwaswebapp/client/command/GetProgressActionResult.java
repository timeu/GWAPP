package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.ProgressResult;
import com.gwtplatform.dispatch.shared.Result;

public class GetProgressActionResult implements Result {
	
	private final ProgressResult result;
	 
	public GetProgressActionResult(final ProgressResult result) {
		super();
		this.result = result;
	}
	
	public ProgressResult getResult() {
		return result;
	}
	
}
