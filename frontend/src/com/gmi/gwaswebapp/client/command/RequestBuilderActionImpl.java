package com.gmi.gwaswebapp.client.command;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.gwtplatform.dispatch.shared.Result;

public abstract class RequestBuilderActionImpl<R extends Result> implements RequestBuilderAction<R> {


	
	static String BaseURL = "/gwas"; 
	@Override
	public String getServiceName() {
		return BaseURL;
	}

	@Override
	public boolean isSecured() {
		return false;
	}
	
	@Override
	public Method getMethod()
	{
		return RequestBuilder.GET;
	}
	

	@Override 
	public String getContentType() {
		return "application/json";
	}
	
	@Override 
	public String getRequestData() {
		return null;
	}
}
