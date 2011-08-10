package com.gmi.gwaswebapp.client.dispatch;

@SuppressWarnings("serial")
public class RequestRuntimeException extends RuntimeException {

	public RequestRuntimeException() {
	}

	public RequestRuntimeException(final String message) {
		super(message);
	}

	public RequestRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public RequestRuntimeException(final Throwable cause) {
		super(cause);
	}

}