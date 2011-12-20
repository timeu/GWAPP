package com.gmi.gwaswebapp.client.dto;

import java.util.List;


public class GWASResult extends BackendResult{
	
	List<Phenotype> phenotypes;
	String result_name;
	
	public GWASResult()
	{
		super();
	}
	
	public GWASResult(STATUS status, String statustext) {
		super(status,statustext);
	}

	public String getResultName()
	{
		return result_name;
	}
	
	public List<Phenotype> getPhenotypes()
	{
		return phenotypes;
	}
}
