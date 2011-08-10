package com.gmi.gwaswebapp.client.command;

import java.util.Collection;
import java.util.Iterator;

import com.gmi.gwaswebapp.client.dto.Readers.AccessionsReader;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchTerm;
import com.google.gwt.http.client.Response;

public class GetAccessionsAction extends RequestBuilderActionImpl<GetAccessionsActionResult> {

	private final int start;
	private final int length;
	private final Collection<SearchTerm> searchTerms;
	private final AccessionsReader reader;
	
	public GetAccessionsAction (int start, int length,Collection<SearchTerm> searchTerms,AccessionsReader reader)  {
		this.start = start;
		this.length = length;
		this.searchTerms = searchTerms;
		this.reader = reader;
		
	}

	public GetAccessionsAction(AccessionsReader reader) {
		this(0,-1,null,reader);
	}

	@Override
	public String getUrl() {
		return getUrl(start, length,getSearchTermParameters());
	}
	
	@Override
	public GetAccessionsActionResult extractResult(Response response) {
		return new GetAccessionsActionResult(reader.read(response.getText()));
	}
	
	private String getSearchTermParameters()  {
		StringBuilder sb = new StringBuilder();
		if (searchTerms != null && searchTerms.size() > 0)
		{
			Iterator<SearchTerm> iterator = searchTerms.iterator();
			while(iterator.hasNext()) {
				SearchTerm searchTerm = iterator.next();
				if (searchTerm.getValue() != null) {
					sb.append("&");
					sb.append(searchTerm.getCriteria().toString());
					sb.append("=");
					sb.append(searchTerm.getValue());
				}
			}
			return sb.toString();
		}
		return "";
	}
	
	public static String getUrl(int start,int length,String searchTermParameters) {
		return BaseURL + "/getAccessions?start="+ start + "&length=" + length+searchTermParameters;
	}

}
