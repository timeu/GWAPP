package com.gmi.gwaswebapp.client.command;


import com.gmi.gwaswebapp.client.dto.Accessions;
import com.gwtplatform.dispatch.shared.Result;

public class GetAccessionsActionResult  implements Result {

	private final Accessions accessions;
	
	public GetAccessionsActionResult(Accessions accessions) {
		this.accessions = accessions;
	}
	
	public Accessions getAccessions() {
		return accessions;
	}
}
