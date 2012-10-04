package com.gmi.gwaswebapp.client.dto;

import java.util.List;

import com.google.gwt.visualization.client.DataTable;

public class ResultData {

	protected List<DataTable> associationTables;
	protected List<Integer> chr_lengths;
	protected double max_score;
	protected double pvalThreshold;
	
	public ResultData (List<DataTable> associationTables,List<Integer> chr_lengths,double max_score,double pvalThreshold)
	{
		this.associationTables = associationTables;
		this.max_score = max_score;
		this.chr_lengths = chr_lengths;
		this.pvalThreshold = pvalThreshold;
	}
	
	public List<DataTable> getAssociationTables()
	{
		return this.associationTables;
	}
	
	public double getMaxScore()
	{
		return this.max_score;
	}
	
	public List<Integer> getChrLengths()
	{
		return chr_lengths;
	}

	public double getPvalThreshold() {
		return pvalThreshold;
	}
	
}
