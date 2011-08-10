package com.gmi.gwaswebapp.client.mvp.analysis;

import java.util.List;


import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.BaseModel;
import com.gmi.gwaswebapp.client.dto.BaseModelKeyProvider;
import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.google.gwt.view.client.TreeViewModel.DefaultNodeInfo;

public class AnalysisTreeViewModel implements TreeViewModel  {

	private static class PhenotypeCell extends AbstractCell<Phenotype>
	{

		@Override
		public void render(Context ctx,Phenotype value, SafeHtmlBuilder sb) {
			if (value != null)	{
				sb.appendEscaped(value.getName());
			}
		}
	}
	
	private static class SubsetCell extends AbstractCell<Dataset> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Dataset value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getName());
		}
	}
	
	private static class TransformationCell extends AbstractCell<Transformation> {

		@Override
		public void render(Context ctx,Transformation value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped(value.getName());
			}
		}
	}
	
	private static class AnalysisCell extends AbstractCell<Analysis> {

		@Override
		public void render(Context ctx,Analysis value, SafeHtmlBuilder sb) {
			if (value != null)
			{
				sb.appendEscaped(value.getName());
			}
		}
	}
	
	
	private final ListDataProvider<Phenotype> phenotypeDataProvider;
	private final SelectionModel<BaseModel> selectionModel;
	
	public AnalysisTreeViewModel(List<Phenotype> phenotypes,SelectionModel<BaseModel> selectionModel)
	{
		phenotypeDataProvider = new ListDataProvider<Phenotype>(phenotypes);
		this.selectionModel = selectionModel;
	}
	
	public void updateTree(List<Phenotype> phenotypes) {
		phenotypeDataProvider.setList(phenotypes);
	}
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null)
		{
			return new DefaultNodeInfo<Phenotype>
				(phenotypeDataProvider,new PhenotypeCell(),selectionModel,null);
		}
		else if (value instanceof Phenotype)
		{
			Phenotype phenotype  = (Phenotype)value;
			return new DefaultNodeInfo<Dataset>(new ListDataProvider<Dataset>(phenotype.getDatasets()),new SubsetCell(),selectionModel,null);
		}
		else if (value instanceof Dataset) {
			Dataset subset  = (Dataset)value;
			return new DefaultNodeInfo<Transformation>(new ListDataProvider<Transformation>(subset.getTransformations()),new TransformationCell(),selectionModel,null);
		}
		else if (value instanceof Transformation)
		{
			Transformation transformation = (Transformation)value;
			return new DefaultNodeInfo<Analysis>(new ListDataProvider<Analysis>(transformation.getAnalysisMethods()),new AnalysisCell(),selectionModel,null);
		}
		String type = value.getClass().getName();
		throw new IllegalArgumentException("Unsupported object type: "+ type);
	}

	@Override
	public boolean isLeaf(Object value) {
		return value instanceof Analysis;
	}

}

