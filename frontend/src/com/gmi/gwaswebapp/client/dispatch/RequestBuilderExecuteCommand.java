package com.gmi.gwaswebapp.client.dispatch;

import com.gmi.gwaswebapp.client.command.RequestBuilderAction;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.dispatch.client.DefaultCallbackDispatchRequest;
import com.gwtplatform.dispatch.shared.DispatchRequest;
import com.gwtplatform.dispatch.client.actionhandler.ExecuteCommand;
import com.gwtplatform.dispatch.shared.Result;

public class RequestBuilderExecuteCommand<A extends RequestBuilderAction<R>,R extends Result> implements ExecuteCommand<A,R> {

	@Override
	public DispatchRequest execute(final A action, AsyncCallback<R> resultCallback) {
		final DefaultCallbackDispatchRequest<R> dispatchRequest = new DefaultCallbackDispatchRequest<R>(resultCallback);
		final RequestBuilder requestBuilder = new RequestBuilder(action.getMethod(),action.getUrl());
		if (action.getContentType() != null && !action.getContentType().isEmpty()) 
			requestBuilder.setHeader("Content-Type", action.getContentType());
		String requestData = action.getRequestData();
		if (requestData != null)
			requestBuilder.setRequestData(requestData);
		requestBuilder.setCallback(new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				if (response.getStatusCode() != 200)
					dispatchRequest.onFailure(new Exception(response.getText()));
				else
					dispatchRequest.onSuccess(action.extractResult(response));
			}
			
			@Override
			public void onError(Request request, Throwable exception) {
				dispatchRequest.onFailure(exception);
			}
		});
        try {
            requestBuilder.send();
        } catch (final RequestException e) {
           dispatchRequest.onFailure(e);
        }
        return dispatchRequest;
	}
}

