package com.gmi.gwaswebapp.client.mvp.phenotype.details;

import com.gmi.gwaswebapp.client.dto.BaseModel;
import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.gmi.gwaswebapp.client.events.NewDatasetEvent;
import com.gmi.gwaswebapp.client.events.NewDatasetEvent.NewDatasetEventHandler;
import com.gmi.gwaswebapp.client.mvp.dataset.detail.DatasetDetailPresenter;
import com.gmi.gwaswebapp.client.mvp.dataset.detail.DatasetDetailPresenter.MODE;
import com.gmi.gwaswebapp.client.mvp.dataset.list.DatasetListPresenter;
import com.gmi.gwaswebapp.client.mvp.transformation.list.TransformationListPresenter;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class PhenotypeDetailPresenter extends PresenterWidget<PhenotypeDetailPresenter.MyView>  {

	
	protected Phenotype phenotype;
	protected SingleSelectionModel<BaseModel> selectionModel;
	protected Dataset dataset;
	protected Transformation transformation;
	protected final DatasetListPresenter datasetListPresenter;
	protected final DatasetDetailPresenter datasetDetailPresenter;
	private final TransformationListPresenter transformationListPresenter;
	protected String action = "read";
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetDatasetListContent = new Type<RevealContentHandler<?>>();
	
	
	
	public interface MyView extends View {
		HasText getPhenotypeName();
		HasText getStddev();
		HasText getNumvalues();
		HasText getCondition();
		HasText getScoring();
		HasText getMethod();
		HasText getScale();
	}
	

	@Inject
	public PhenotypeDetailPresenter(EventBus eventBus, MyView view,
			final DatasetListPresenter datasetListPresenter,DatasetDetailPresenter datasetDetailPresenter,
			final TransformationListPresenter transformationListPresenter) {
		super(eventBus, view);
		this.datasetListPresenter = datasetListPresenter;
		this.datasetDetailPresenter = datasetDetailPresenter;
		this.transformationListPresenter = transformationListPresenter;
	}
	
	public void setData(Phenotype phenotype, Dataset dataset, Transformation transformation, SingleSelectionModel<BaseModel> selectionModel, String action) {
		this.phenotype = phenotype;
		this.dataset = dataset;
		this.transformation = transformation;
		this.selectionModel = selectionModel;
		if (action.isEmpty())
			action = "read";
		this.action = action;
		updateView();
	}
	
	public void updateView() {
		if (phenotype!= null) {
			MyView v = getView();
			v.getPhenotypeName().setText(phenotype.getName());
			v.getStddev().setText((phenotype.getStdDev() == null ? "N/A" : phenotype.getStdDev().toString()));
			v.getNumvalues().setText((phenotype.getNumValues() == null ? "N/A" : phenotype.getNumValues().toString()));
			v.getCondition().setText((phenotype.getGrowthConditions() == null ? "N/A" : phenotype.getGrowthConditions()));
			v.getScoring().setText((phenotype.getPhenotypeScoring() == null ? "N/A" :phenotype.getPhenotypeScoring()));
			v.getMethod().setText((phenotype.getMethodDescription() == null ? "N/A" : phenotype.getMethodDescription()));
			v.getScale().setText((phenotype.getMeasurementScale() == null ? "N/A" :phenotype.getMeasurementScale()));
			
			if (transformation != null) {
				transformationListPresenter.setTransformations(dataset.getTransformations(), transformation);
				transformationListPresenter.setSelectionModel(selectionModel);
				setInSlot(TYPE_SetDatasetListContent, transformationListPresenter);
			}
			else if (dataset != null) {
				datasetDetailPresenter.setData(dataset,DatasetDetailPresenter.MODE.valueOf(action.toUpperCase()));
				setInSlot(TYPE_SetDatasetListContent, datasetDetailPresenter);
			}
			else {
				datasetListPresenter.setData(phenotype.getDatasets());
				setInSlot(TYPE_SetDatasetListContent, datasetListPresenter);
			}
		}
	}
}
