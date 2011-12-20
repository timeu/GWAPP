package com.gmi.gwaswebapp.client.command;

import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;

public interface RequestBuilderAction <R extends Result> extends Action<R> {
		
	String getUrl();
	Method getMethod();
	R extractResult(Response response);
	String getRequestData();
	String getContentType();
}
