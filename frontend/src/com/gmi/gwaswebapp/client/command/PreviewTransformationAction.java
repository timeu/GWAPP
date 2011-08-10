package com.gmi.gwaswebapp.client.command;


import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.visualization.client.DataTable;


public class PreviewTransformationAction extends RequestBuilderActionImpl<PreviewTransformationActionResult> {

	final String Phenotype;
	final String Dataset;
	final String Transformation;
	final String New_Transformation;
	
	public PreviewTransformationAction(final String phenotype,final String dataset,final String transformation,final String new_transformation) {
		super();
		this.Phenotype = phenotype;
		this.Dataset = dataset;
		this.Transformation = transformation;
		this.New_Transformation = new_transformation;
	}

	@Override
	public String getUrl() {
		return _getUrl(Phenotype,Dataset, Transformation, New_Transformation);
	}

	@Override
	public PreviewTransformationActionResult extractResult(Response response) {
		// TODO Auto-generated method stub
		JSONObject json =  JSONParser.parseLenient(response.getText()).isObject();
		String phenotypeTable_str = json.get("transformationTable").isString().stringValue();
		DataTable transformationDataTable = DataTable.create(JSONParser.parseLenient(phenotypeTable_str).isObject().getJavaScriptObject());
		return new PreviewTransformationActionResult(transformationDataTable);
	}
	
	public static String _getUrl(String Phenotype,String Dataset,String Transformation,String New_Transformation)
	{
		return BaseURL + "/getTransformationPreview?phenotype="+ Phenotype + "&dataset="+Dataset+"&transformation=" + Transformation + "&new_transformation="+New_Transformation;
	}
}
