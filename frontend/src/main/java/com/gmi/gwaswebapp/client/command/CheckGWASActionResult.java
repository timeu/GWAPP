package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gwtplatform.dispatch.shared.Result;

public class CheckGWASActionResult implements Result{
	
	private final BackendResult result;
	
	public CheckGWASActionResult(final BackendResult result) {
		this.result = result;
	}

	public BackendResult getResult() {
		return result;
	}
}
