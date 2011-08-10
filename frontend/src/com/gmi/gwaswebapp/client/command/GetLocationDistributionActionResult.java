package com.gmi.gwaswebapp.client.command;

import java.util.HashMap;

import com.google.gwt.visualization.client.DataTable;
import com.gwtplatform.dispatch.shared.Result;

public class GetLocationDistributionActionResult implements Result {
	
	private final HashMap<String,DataTable> dataTables;

	public GetLocationDistributionActionResult(final HashMap<String,DataTable> dataTables) {
		super();
		this.dataTables = dataTables;
	}
	
	public HashMap<String,DataTable> getDataTables() {
		return dataTables;
	}
} 
