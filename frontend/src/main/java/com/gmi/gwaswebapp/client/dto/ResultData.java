package com.gmi.gwaswebapp.client.dto;

import java.util.List;

import com.google.gwt.visualization.client.DataTable;

public class ResultData {

	protected List<DataTable> associationTables;
	protected List<Integer> chr_lengths;
	protected double max_score;
	protected double pvalThreshold;
	protected double med_pval;
	protected double ks_stat;
	protected double ks_pval;
	
	public ResultData (List<DataTable> associationTables,List<Integer> chr_lengths,
			double max_score,double pvalThreshold, double med_pval,double ks_stat, double ks_pval)
	{
		this.associationTables = associationTables;
		this.max_score = max_score;
		this.chr_lengths = chr_lengths;
		this.pvalThreshold = pvalThreshold;
		this.ks_stat = ks_stat;
		this.ks_pval = ks_pval;
		this.med_pval = med_pval;
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
	
	public double getMedPval() {
		return med_pval;
	}
	
	public double getKsStat() {
		return ks_stat;
	}
	public double getKsPval() {
		return ks_pval;
	}
	
}
