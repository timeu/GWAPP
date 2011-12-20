package com.gmi.gwaswebapp.client.command;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.visualization.client.DataTable;

public class GetLocationDistributionAction extends RequestBuilderActionImpl<GetLocationDistributionActionResult> {

	private final String phenotype;
	private final String dataset;
	
	
	public GetLocationDistributionAction(final String phenotype,String dataset) {
		super();
		this.phenotype = phenotype;
		this.dataset = dataset;
	}

	public GetLocationDistributionAction(String phenotype) {
		this(phenotype,null);
	}

	public GetLocationDistributionAction() {
		this(null,null);
	}

	@Override
	public String getUrl() {
		return _getUrl(phenotype, dataset);
	}

	@Override
	public GetLocationDistributionActionResult extractResult(Response response) {
		JSONObject json =  JSONParser.parseLenient(response.getText()).isObject();
		Set<String> keys = json.keySet();
		HashMap<String,DataTable> dataTables = new HashMap<String, DataTable>();
		for (String dataset : keys) 
		{
			String data = json.get(dataset).isString().stringValue();
			dataTables.put(dataset, DataTable.create(JSONParser.parseLenient(data).isObject().getJavaScriptObject())); 
		}
		return new GetLocationDistributionActionResult(dataTables);
	}
	
	public static String _getUrl(String Phenotype,String Dataset)
	{
		return BaseURL + "/getLocationDistributionData" + (Phenotype != null && !Phenotype.isEmpty() ? "?phenotype="+ Phenotype : "") + (Dataset != null && !Dataset.isEmpty() ? "&dataset=" + Dataset : "");
	}
}

