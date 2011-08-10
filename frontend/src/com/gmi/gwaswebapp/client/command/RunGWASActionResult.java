package com.gmi.gwaswebapp.client.command;

import java.util.List;

import com.gmi.gwaswebapp.client.dto.GWASResult;
import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.gwtplatform.dispatch.shared.Result;

public class RunGWASActionResult implements Result {
	public final GWASResult result;
	public final Integer Chromosome;
	public final Integer Position;
	public final String Phenotype;
	public final String Dataset;
	public final String Transformation;
	public final List<Phenotype> Phenotypes;
	public final String ResultName;
	 
	public RunGWASActionResult(final GWASResult result,Integer chromosome,Integer position,List<Phenotype> phenotypes,String Phenotype,String dataset,String Transformation,String ResultName) {
		super();
		this.result = result;
		this.Dataset = dataset;
		this.Chromosome = chromosome;
		this.Position = position;
		this.ResultName  = ResultName;
		this.Transformation = Transformation;
		this.Phenotypes = phenotypes;
		this.Phenotype = Phenotype;
	}
} 