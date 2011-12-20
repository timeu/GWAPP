package com.gmi.gwaswebapp.client.command;

import com.google.gwt.http.client.Response;
import com.gwtplatform.dispatch.shared.Result;


public class SaveNewTransformationAction extends RequestBuilderActionImpl<Result> {

	public final String Phenotype;
	public final String Dataset;
	public final String Transformation;
	public final String New_Transformation;
	
	public SaveNewTransformationAction(final String phenotype,final String Dataset,final String transformation,final String new_transformation) {
		super();
		this.Phenotype = phenotype;
		this.Dataset = Dataset;
		this.Transformation = transformation;
		this.New_Transformation = new_transformation;
	}

	@Override
	public String getUrl() {
		return _getUrl(Phenotype,Dataset,Transformation, New_Transformation);
	}

	@Override
	public Result extractResult(Response response) {
		/*BaseJSONResult  result = null;
		if (response.getStatusCode() != 200)
			result = new BaseJSONResult(BaseJSONResult.STATUS.ERROR,"Server Error. Statuscode: "+response.getStatusCode());
		else
			result = Readers.BaseResultJSON.read(response.getText());*/
		return new Result() {
		};
	}
	
	public static String _getUrl(final String Phenotype,final String Dataset,final String Transformation, final String New_Transformation)
	{
		return BaseURL + "/saveNewTransformation?phenotype="+ Phenotype + "&dataset="+Dataset+"&transformation=" + Transformation + "&new_transformation="+New_Transformation;
	}
}
