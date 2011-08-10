package com.gmi.gwaswebapp.client.command;


import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gwtplatform.dispatch.shared.Result;

public class BaseStatusResult implements Result {
	
	public final BackendResult result;

	public BaseStatusResult(final BackendResult result) {
		super();
		this.result = result;
	}
} 