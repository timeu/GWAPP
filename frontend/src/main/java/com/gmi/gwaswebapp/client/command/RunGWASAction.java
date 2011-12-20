package com.gmi.gwaswebapp.client.command;

import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.GWASResult;
import com.gmi.gwaswebapp.client.dto.Readers.GWASResultReader;


import com.google.gwt.http.client.Response;

public class RunGWASAction extends RequestBuilderActionImpl<RunGWASActionResult> {

	public final String Phenotype;
	public final String Dataset;
	public final String Transformation;
	public final Analysis.TYPE Analysis;
	public final String ResultName;
	public final Integer Chromosome;
	public final Integer Position;
	private final GWASResultReader reader;
	
	public RunGWASAction(String phenotype,String dataset,String transformation,Analysis.TYPE analysis,final GWASResultReader reader) {
		this(phenotype,dataset,transformation,analysis,null,null,null,reader);
	}
	
	public RunGWASAction(String phenotype,String dataset,String transformation,Analysis.TYPE analysis,String ResultName,Integer Chromosome, Integer Position,GWASResultReader reader ) {
		super();
		this.Dataset = dataset;
		this.Phenotype = phenotype;
		this.Transformation = transformation;
		this.Analysis = analysis;
		this.Chromosome = Chromosome;
		this.Position = Position;
		this.ResultName = ResultName;
		this.reader = reader;
	}
	

	@Override
	public String getUrl() {
		return _getUrl(Phenotype,Dataset, Transformation, Analysis,ResultName,Chromosome,Position);
	}

	@Override
	public RunGWASActionResult extractResult(Response response) {
		GWASResult  result = null;
		if (response.getStatusCode() != 200)
			result = new GWASResult(GWASResult.STATUS.ERROR,"Server Error. Statuscode: "+response.getStatusCode());
		else {
			result = reader.read(response.getText());
		}
		return new RunGWASActionResult(result,Chromosome,Position,result.getPhenotypes(),Phenotype,Dataset,Transformation,result.getResultName());
	}
	
	public static String _getUrl(String Phenotype,String Dataset,String Transformation,Analysis.TYPE Analysis,String ResultName,Integer Chromosome,Integer Position)
	{
		String URL =  BaseURL + "/run_gwas?phenotype="+ Phenotype + "&dataset="+Dataset+"&transformation=" + Transformation + "&analysis="+Analysis;
		if (Chromosome != null && Position != null)
			URL+="&chromosome="+Chromosome+"&position="+Position;
		if (ResultName != null)
			URL+="&result_name="+ResultName;
		return URL;
	}
}
