package com.gmi.gwaswebapp.client.command;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.visualization.client.DataTable;

public class GetTransformationAction extends RequestBuilderActionImpl<GetTransformationActionResult> {

	final String Phenotype;
	final String Dataset;
	final String Transformation;
	
	public GetTransformationAction(final String phenotype,final String dataset, final String transformation) {
		super();
		this.Phenotype = phenotype;
		this.Transformation = transformation;
		this.Dataset = dataset;
	}

	@Override
	public String getUrl() {
		return _getUrl(Phenotype, Dataset,Transformation);
	}

	@Override
	public GetTransformationActionResult extractResult(Response response) {
		JSONObject json =  JSONParser.parseLenient(response.getText()).isObject();
		String phenotypeTable_str = json.get("transformationTable").isString().stringValue();
		String motionchartTable_str = json.get("motionchartTable").isString().stringValue();
		DataTable transformationDataTable = DataTable.create(JSONParser.parseLenient(phenotypeTable_str).isObject().getJavaScriptObject());
		DataTable motionchartDataTable = DataTable.create(JSONParser.parseLenient(motionchartTable_str).isObject().getJavaScriptObject());
		return new GetTransformationActionResult(transformationDataTable,motionchartDataTable);
	}
	
	public static String _getUrl(String Phenotype,String Dataset,String Transformation)
	{
		return BaseURL + "/getTransformation?phenotype="+ Phenotype + "&dataset="+Dataset+"&transformation=" + Transformation;
	}
}
