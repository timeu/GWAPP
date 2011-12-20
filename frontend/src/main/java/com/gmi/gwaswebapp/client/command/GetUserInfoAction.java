package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.Readers.UserDataReader;
import com.google.gwt.http.client.Response;
import com.google.inject.Inject;

public class GetUserInfoAction extends RequestBuilderActionImpl<GetUserInfoActionResult> {

	protected final UserDataReader reader;
	protected boolean loadAccessions = false;
	
	@Inject
	public GetUserInfoAction(UserDataReader reader) {
		this(reader,false);
	}

	public GetUserInfoAction(UserDataReader reader, boolean loadAccessions) {
		super();
		this.reader = reader;
		this.loadAccessions = loadAccessions;
	}

	@Override
	public String getUrl() {
		return _getUrl(loadAccessions);
	}
	
	@Override
	public GetUserInfoActionResult extractResult(Response response) {
		return new GetUserInfoActionResult(reader.read(response.getText()));
	}

	public static String _getUrl(boolean loadAccessions) 
	{
		return BaseURL + "/getUserData" + (loadAccessions ? "?loadAccessions=1":"");
	}
	
}
