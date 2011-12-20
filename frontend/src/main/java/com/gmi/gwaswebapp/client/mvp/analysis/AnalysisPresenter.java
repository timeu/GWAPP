package com.gmi.gwaswebapp.client.mvp.analysis;

import java.util.List;

import com.gmi.gwaswebapp.client.CurrentUser;
import com.gmi.gwaswebapp.client.NameTokens;
import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.BaseModel;
import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.gmi.gwaswebapp.client.dto.Readers.PhenotypeReader;
import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.gmi.gwaswebapp.client.events.DeleteResultEvent;
import com.gmi.gwaswebapp.client.events.DeleteTransformationEvent;
import com.gmi.gwaswebapp.client.events.NewDatasetEvent;
import com.gmi.gwaswebapp.client.events.NewDatasetEvent.NewDatasetEventHandler;
import com.gmi.gwaswebapp.client.events.NewTransformationSavedEvent;
import com.gmi.gwaswebapp.client.events.DeleteResultEvent.DeleteResultEventHandler;
import com.gmi.gwaswebapp.client.events.DeleteTransformationEvent.DeleteTransformationEventHandler;
import com.gmi.gwaswebapp.client.events.NewTransformationSavedEvent.NewTransformationSavedHandler;
import com.gmi.gwaswebapp.client.events.RunGWASFinishedEvent;
import com.gmi.gwaswebapp.client.events.SaveDatasetEvent;
import com.gmi.gwaswebapp.client.events.UpdateDataEvent;
import com.gmi.gwaswebapp.client.events.RunGWASFinishedEvent.RunGWASFinishedEventHandler;
import com.gmi.gwaswebapp.client.events.SaveDatasetEvent.SaveDatasetEventHandler;
import com.gmi.gwaswebapp.client.events.UpdateDataEvent.UpdateDataHandler;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter;
import com.gmi.gwaswebapp.client.mvp.phenotype.details.PhenotypeDetailPresenter;
import com.gmi.gwaswebapp.client.mvp.phenotype.list.PhenotypeListPresenter;
import com.gmi.gwaswebapp.client.mvp.result.details.ResultDetailPresenter;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class AnalysisPresenter extends Presenter<AnalysisPresenter.MyView,AnalysisPresenter.MyProxy> 
							 implements AnalysisUiHandlers,NewTransformationSavedHandler,
							            UpdateDataHandler,DeleteTransformationEventHandler,
							            RunGWASFinishedEventHandler,
							            DeleteResultEventHandler,
							            NewDatasetEventHandler,
							            SaveDatasetEventHandler{

	
	@ProxyCodeSplit
	@NameToken(NameTokens.analysisPage)
	public interface MyProxy extends ProxyPlace<AnalysisPresenter>{	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetPhenotypeListContent = new Type<RevealContentHandler<?>>();
	public static final Type<RevealContentHandler<?>> TYPE_SetPhenotypeDetailContent = new Type<RevealContentHandler<?>>();
	public static final Type<RevealContentHandler<?>> TYPE_SetResultDetailContent = new Type<RevealContentHandler<?>>();
	

	public interface MyView extends View,HasUiHandlers<AnalysisUiHandlers> {
		void initTreeItems(List<Phenotype> phenotypes);
		SingleSelectionModel<BaseModel> getTreeSelectionModel();
		boolean expandTree(BaseModel selectedItem,TreeNode node);
	}
	
	protected final CurrentUser currentUser;
	protected final PlaceManager placeManager;
	//protected BaseModel selectedTreeItem;
	protected final PhenotypeReader phenotypeReader;
	private final DispatchAsync dispatch;
	protected final PhenotypeListPresenter phenotypeListPresenter;
	protected final PhenotypeDetailPresenter phenotypeDetailPresenter;
	protected final ResultDetailPresenter resultDetailPresenter;
	private boolean dontExpandTree = false;
	
	@Inject
	public AnalysisPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			PlaceManager placeManager,PhenotypeReader phenotypeReader,
			final DispatchAsync dispatch,final PhenotypeListPresenter phenotypeListPresenter,
			final CurrentUser currentUser,final PhenotypeDetailPresenter phenotypeDetailPresenter,
			final ResultDetailPresenter resultDetailPresenter) {
		super(eventBus, view, proxy);
		this.dispatch = dispatch;
		this.placeManager = placeManager;
		this.currentUser = currentUser;
		this.phenotypeReader = phenotypeReader;

		//Presenters
		this.phenotypeListPresenter = phenotypeListPresenter;
		this.phenotypeDetailPresenter = phenotypeDetailPresenter;
		this.resultDetailPresenter = resultDetailPresenter;
		getView().setUiHandlers(this);
		
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this,MainPagePresenter.TYPE_SetMainContent,this);
	}
	
	@Override
	public void onReset()
	{
		super.onReset();
		getView().initTreeItems(currentUser.getUserData().getPhenotypes());
		
		PlaceRequest currentRequest = placeManager.getCurrentPlaceRequest();
		if (currentRequest.getParameterNames().size() == 0) {
			BaseModel selectedItem = getView().getTreeSelectionModel().getSelectedObject();
			if (selectedItem != null)
				getView().getTreeSelectionModel().setSelected(selectedItem, false);
			setInSlot(TYPE_SetPhenotypeListContent, phenotypeListPresenter);
			phenotypeListPresenter.setPhenotypeList(currentUser.getUserData().getPhenotypes());
			clearSlot(TYPE_SetPhenotypeDetailContent);
			clearSlot(TYPE_SetResultDetailContent);
		}
		else
		{
			String phenotype_name = currentRequest.getParameter("phenotype", "");
			String dataset_name = currentRequest.getParameter("dataset", null);
			String transformation_name = currentRequest.getParameter("transformation", "");
			String analysis_name = currentRequest.getParameter("result", "");
			String action = currentRequest.getParameter("action", "");
			Phenotype selectedPhenotype = currentUser.getUserData().getPhenotypeFromName(phenotype_name);
			Dataset selectedDataset = selectedPhenotype.getDatasetFromId(dataset_name);
			Transformation selectedTransformation = null;
			if (selectedDataset != null) {
				selectedTransformation = selectedDataset.getTransformationFromName(transformation_name);
			}
			Analysis selectedAnalysis = null;
			if (selectedTransformation != null) 
				selectedAnalysis = selectedTransformation.getAnalysisFromName(analysis_name);
			
			if (selectedAnalysis != null) {
				getView().getTreeSelectionModel().setSelected(selectedAnalysis, true);
				if (!dontExpandTree)
					getView().expandTree(selectedAnalysis,null);
				else 
					dontExpandTree = false;
			}
			else if (selectedTransformation != null) {
				getView().getTreeSelectionModel().setSelected(selectedTransformation, true);
				if (!dontExpandTree)
					getView().expandTree(selectedTransformation,null);
				else 
					dontExpandTree = false;
			}
			else if (selectedDataset != null) {
				getView().getTreeSelectionModel().setSelected(selectedDataset, true);
				if (!dontExpandTree)
					getView().expandTree(selectedDataset,null);
				else 
					dontExpandTree = false;
			}
			else if (selectedPhenotype != null) {
				getView().getTreeSelectionModel().setSelected(selectedPhenotype, true);
			}
			clearSlot(TYPE_SetPhenotypeListContent);
			if (selectedAnalysis != null) {
				clearSlot(TYPE_SetPhenotypeDetailContent);
				setInSlot(TYPE_SetResultDetailContent, resultDetailPresenter);
				resultDetailPresenter.loadData(selectedAnalysis);
			}
			else {
				setInSlot(TYPE_SetPhenotypeDetailContent, phenotypeDetailPresenter);
				phenotypeDetailPresenter.setData(selectedPhenotype,selectedDataset,selectedTransformation,getView().getTreeSelectionModel(),action);
				clearSlot(TYPE_SetResultDetailContent);
			}
		}
	}
	
	protected void fetchData() {

	}
	

	@Override
	public void onLoadPhenotype(Phenotype phenotype) {
		PlaceRequest currentRequest = placeManager.getCurrentPlaceRequest();
		
		PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
		place = place.with("phenotype",phenotype.getName());
		if (!currentRequest.equals(place))
			placeManager.revealPlace(place);
	}

	@Override
	public void onLoadTransformation(Transformation transformation) {
		dontExpandTree = true;
		String url = placeManager.buildHistoryToken(placeManager.getCurrentPlaceRequest());
		PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
		Phenotype phenotype= currentUser.getUserData().getPhenotypeFromName(transformation.getPhenotype());
		place = place.with("phenotype",phenotype.getName()).with("dataset",transformation.getDataset()).with("transformation",transformation.getName());
		if (!url.equals(placeManager.buildHistoryToken(place))) 
			placeManager.revealPlace(place);
	}

	@ProxyEvent
	@Override
	public void onNewTransformationSaved(final NewTransformationSavedEvent event) {
		currentUser.refresh(new Runnable() {
			
			@Override
			public void run() {
				PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
				place = place.with("phenotype",event.getPhenotype()).with("dataset",event.getDataset()).with("transformation",event.getTransformation());
				placeManager.revealPlace(place);
			}
		});
	}
	

	
	
	@ProxyEvent
	@Override
	public void onUpdateData(UpdateDataEvent event)
	{
		currentUser.refresh(new Runnable() {
			
			@Override
			public void run() {
				PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
				placeManager.revealPlace(place);
			}
		});
	}

	@ProxyEvent
	@Override
	public void onDeleteTransformation(final DeleteTransformationEvent event) {
		currentUser.refresh(new Runnable() {
			
			@Override
			public void run() {
				PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
				place = place.with("phenotype",event.getPhenotype());
				placeManager.revealPlace(place);
			}
		});
	}
	

	@ProxyEvent
	@Override
	public void onGWASFinished(RunGWASFinishedEvent event) {
		currentUser.refresh(event.Phenotypes);
		getView().initTreeItems(currentUser.getUserData().getPhenotypes());
		PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
		place = place.with("phenotype",event.Phenotype).with("dataset",event.Dataset).with("transformation",event.Transformation).with("result",event.ResultName);
		placeManager.revealPlace(place);
	}

	@ProxyEvent
	@Override
	public void onDeleteResult(final DeleteResultEvent event) {
		currentUser.refresh(new Runnable() {
			
			@Override
			public void run() {
				PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
				place = place.with("phenotype",event.getPhenotype()).with("transformation", event.getTransformation());
				placeManager.revealPlace(place);
			}
		});
		
	}

	@Override
	public void onLoadGWAS(Analysis analysis) {
		dontExpandTree = true;
		String url = placeManager.buildHistoryToken(placeManager.getCurrentPlaceRequest());
		PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
		Phenotype phenotype= currentUser.getUserData().getPhenotypeFromName(analysis.getPhenotype());
		place = place.with("phenotype",phenotype.getName()).with("dataset",analysis.getDataset()).with("transformation",analysis.getTransformation()).with("result",analysis.getName());
		if (!url.equals(placeManager.buildHistoryToken(place))) 
			placeManager.revealPlace(place);
	}

	@Override
	public void onLoadDataset(Dataset dataset) {
		dontExpandTree = true;
		PlaceRequest currentPlace = placeManager.getCurrentPlaceRequest();
		Phenotype phenotype = currentUser.getUserData().getPhenotypeFromName(dataset.getPhenotype());
		if (!(currentPlace.getNameToken().equals(NameTokens.analysisPage) && currentPlace.getParameter("phenotype", "").equals(phenotype.getName()) 
				&& currentPlace.getParameter("dataset", "").equals(dataset.getInternId()) 
				&& currentPlace.getParameter("transformation", "").equals("")))
		{
			PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
			place = place.with("phenotype",phenotype.getName()).with("dataset",dataset.getInternId());
			placeManager.revealPlace(place);
		}
		
	}

	@ProxyEvent
	@Override
	public void onNewDataset(NewDatasetEvent event) {
		String url = placeManager.buildHistoryToken(placeManager.getCurrentPlaceRequest());
		PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
		Phenotype phenotype= currentUser.getUserData().getPhenotypeFromName(event.getDataset().getPhenotype());
		Dataset subset = phenotype.createSubset(event.getDataset());
		place = place.with("phenotype",phenotype.getName()).with("dataset",subset.getName()).with("action", "add"); 
		if (!url.equals(placeManager.buildHistoryToken(place))) 
			placeManager.revealPlace(place);
	}

	@ProxyEvent
	@Override
	public void onSaveDataset(SaveDatasetEvent event) {
		PlaceRequest place = new PlaceRequest(NameTokens.analysisPage);
		place = place.with("phenotype",event.getDataset().getPhenotype()).with("dataset",event.getDataset().getName());
		//placeManager.revealPlace(place);
	}
}
