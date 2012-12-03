package com.gmi.gwaswebapp.client.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gmi.gwaswebapp.client.dto.ResultData;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.visualization.client.DataTable;


public class GetAssociationDataAction extends RequestBuilderActionImpl<GetAssociationDataActionResult> {

	final String Phenotype;
	final String Dataset;
	final String Transformation;
	final String Analysis;
	final String ResultName;
	
	public GetAssociationDataAction(final String phenotype,final String dataset,final String transformation,final String analysis,final String resultname) {
		super();
		this.Phenotype = phenotype;
		this.Dataset = dataset;
		this.Transformation = transformation;
		this.Analysis = analysis;
		this.ResultName = resultname;
	}

	@Override
	public String getUrl() {
		return _getUrl(Phenotype, Dataset, Transformation, Analysis,ResultName);
	}

	@Override
	public GetAssociationDataActionResult extractResult(Response response) {
		JSONObject serverData = JSONParser.parseLenient(response.getText()).isObject();
		JSONObject chr2data = serverData.get("chr2data").isObject();
		JSONArray chr2length = serverData.get("chr2length").isArray();
		double max_value = serverData.get("max_value").isNumber().doubleValue();
		double pvalThreshold = serverData.get("pval_threshold").isNumber().doubleValue();
		double med_pval = serverData.get("med_pval").isNumber().doubleValue();
		double ks_stat = serverData.get("ks_stat").isNumber().doubleValue();
		double ks_pval = serverData.get("ks_pval").isNumber().doubleValue();
		List<DataTable> dataTables  =  new ArrayList<DataTable>();
		List<Integer> chr_lengths = new ArrayList<Integer>();
		Set<String> keys = chr2data.keySet();
		for (String chromosome : keys) 
		{
			String data = chr2data.get(chromosome).isString().stringValue();
			int chrLength = (int) chr2length.get(Integer.parseInt(chromosome)-1).isNumber().doubleValue();
			chr_lengths.add(chrLength);
			DataTable dataTable = DataTable.create(JSONParser.parseLenient(data).isObject().getJavaScriptObject());
			dataTable.insertRows(0,1);
			dataTable.setValue(0, 0, 0);
			int index = dataTable.addRow();
			dataTable.setValue(index, 0, chrLength);
			dataTables.add(dataTable);
		}
		return new GetAssociationDataActionResult(new ResultData(dataTables,chr_lengths,max_value,pvalThreshold,med_pval,ks_stat,ks_pval));
	}
	
	public static String _getUrl(String Phenotype,String Dataset,String Transformation, String Analysis,String ResultName)  {
		return BaseURL + "/getAssociationData?phenotype="+ Phenotype + "&dataset="+Dataset + "&transformation=" + Transformation + "&analysis="+Analysis + "&result_name=" + ResultName;
	}
	
	
}
