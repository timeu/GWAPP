package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.ProgressResult;
import com.gmi.gwaswebapp.client.dto.Readers.ProgressResultReader;
import com.google.gwt.http.client.Response;

public class GetProgressAction extends RequestBuilderActionImpl<GetProgressActionResult> {

	private final String url;
	private final ProgressResultReader reader;
	
	
	public GetProgressAction(String url,final ProgressResultReader reader ) {
		super();
		this.url = url;
		this.reader = reader;
	}
	

	@Override
	public String getUrl() {
		return _getUrl(url);
	}

	@Override
	public GetProgressActionResult extractResult(Response response) {
		ProgressResult  result = null;
		if (response.getStatusCode() != 200)
			result = new ProgressResult(ProgressResult.STATUS.ERROR,"Server Error. Statuscode: "+response.getStatusCode());
		else {
			result = reader.read(response.getText());
		}
		return new GetProgressActionResult(result);
	}
	
	public static String _getUrl(String url)
	{
		String URL =  url+"&showProgress=1";
		return URL;
	}
}
