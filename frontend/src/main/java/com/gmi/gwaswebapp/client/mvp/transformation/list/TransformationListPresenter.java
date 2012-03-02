package com.gmi.gwaswebapp.client.mvp.transformation.list;

import java.util.List;

import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.CheckGWASAction;
import com.gmi.gwaswebapp.client.command.CheckGWASActionResult;
import com.gmi.gwaswebapp.client.command.DeleteTransformationAction;
import com.gmi.gwaswebapp.client.command.PreviewTransformationAction;
import com.gmi.gwaswebapp.client.command.PreviewTransformationActionResult;
import com.gmi.gwaswebapp.client.command.RunGWASAction;
import com.gmi.gwaswebapp.client.command.RunGWASActionResult;
import com.gmi.gwaswebapp.client.command.SaveNewTransformationAction;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Analysis.TYPE;
import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.BaseModel;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.gmi.gwaswebapp.client.dto.Readers.GWASResultReader;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.gmi.gwaswebapp.client.events.DeleteTransformationEvent;
import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.gmi.gwaswebapp.client.events.NewTransformationSavedEvent;
import com.gmi.gwaswebapp.client.events.ProgressBarEvent;
import com.gmi.gwaswebapp.client.events.RunGWASFinishedEvent;
import com.gmi.gwaswebapp.client.mvp.progress.ProgressPresenter;
import com.gmi.gwaswebapp.client.mvp.result.details.ResultDetailPresenter;
import com.gmi.gwaswebapp.client.mvp.result.list.ResultListPresenter;
import com.gmi.gwaswebapp.client.mvp.transformation.details.TransformationDetailPresenter;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.DataTable;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.dispatch.shared.Result;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;


public class TransformationListPresenter extends PresenterWidget<TransformationListPresenter.MyView> implements TransformationListUiHandlers{

	public interface MyView extends View,HasUiHandlers<TransformationListUiHandlers>{
		HasData<Transformation> getDisplay();
		void setSelectionModel(SingleSelectionModel<BaseModel> selectionModel);
		void showTransformationPreviewHistogram(DataTable data, Double spPval);
		void hideTransformationPreviewHistogram();
		void showNotification(String iconUrl,String title,String body);
	}
	public static final Object TYPE_SetTransformationDetailContent = new Object();
	public static final Object TYPE_SetResultListContent = new Object();
	
	
	protected ListDataProvider<Transformation> transformationDataProvider = new ListDataProvider<Transformation>();
	protected final TransformationDetailPresenter transformationDetailPresenter;
	protected final ResultListPresenter resultListPresenter;
	protected final DispatchAsync dispatch;
	protected final BackendResultReader backendResultReader;
	protected final GWASResultReader gwasResultReader;
	private final ProgressPresenter progressPresenter;
	private final GoogleAnalytics googleAnalytics;
	
	//
	@Inject
	public TransformationListPresenter(EventBus eventBus,
			MyView view, final DispatchAsync dispatch,
			final TransformationDetailPresenter transformationDetailPresenter,
			final BackendResultReader backendResultReader,
			final GWASResultReader gwasResultReader,
			final ResultListPresenter resultListPresenter,
			final ProgressPresenter progressPresenter, 
			final GoogleAnalytics googleAnalytics
			) {
		super(eventBus, view);
		this.transformationDetailPresenter = transformationDetailPresenter;
		this.dispatch = dispatch;
		this.backendResultReader = backendResultReader;
		this.gwasResultReader = gwasResultReader;
		this.resultListPresenter = resultListPresenter;
		this.progressPresenter = progressPresenter;
		this.googleAnalytics = googleAnalytics;
		getView().setUiHandlers(this);
		transformationDataProvider.addDataDisplay(getView().getDisplay());
	}

	public void setTransformations(List<Transformation> transformations, Transformation transformation) {
		transformationDataProvider.setList(transformations);
		if (transformation != null)
		{
			setInSlot(TYPE_SetTransformationDetailContent, transformationDetailPresenter);
			transformationDetailPresenter.setTransformation(transformation);
			setInSlot(TYPE_SetResultListContent, resultListPresenter);
			resultListPresenter.setResults(transformation.getAnalysisMethods());
		}
		else {
			clearSlot(TYPE_SetTransformationDetailContent);
			clearSlot(TYPE_SetResultListContent);
		}
	}

	public void setSelectionModel(SingleSelectionModel<BaseModel> selectionModel) {
		getView().setSelectionModel(selectionModel);
		
	}

	@Override
	public void startNewTransformation(Transformation transformation) {
		transformation.setIsNewTransformation(true);
		transformation.setNewTransformation("");
		transformationDataProvider.refresh();
		getView().showTransformationPreviewHistogram(null,null);
	}

	@Override
	public void changeNewTransformation(Transformation transformation) {
		if (transformation.getNewTransformation().equals(""))
		{
			getView().showTransformationPreviewHistogram(null,null);
		}
		else
		{
			dispatch.execute(new PreviewTransformationAction(transformation.getPhenotype(),transformation.getDataset(),transformation.getName(),transformation.getNewTransformation()), new GWASCallback<PreviewTransformationActionResult>(getEventBus()) {
	
				@Override
				public void onSuccess(PreviewTransformationActionResult result) {
					if (result.getTransformationTable() == null)
						DisplayNotificationEvent.fireError(this, "Backend-Error" , "Error getting transformation data");
					else
						getView().showTransformationPreviewHistogram(result.getTransformationTable(),result.getSpPval());
				}
			});
		}
	}

	@Override
	public void cancelNewTransformation(Transformation transformation) {
		getView().hideTransformationPreviewHistogram();
		transformation.setIsNewTransformation(false);
		transformationDataProvider.refresh();
	}

	@Override
	public void saveNewTransformation(final Transformation transformation) {
		if (!(transformation.getNewTransformation().equals("")))
		{
			final String newTransformation = transformation.getNewTransformation(); 
			dispatch.execute(new SaveNewTransformationAction(transformation.getPhenotype(),transformation.getDataset(),transformation.getName(),newTransformation), new GWASCallback<Result>(getEventBus()) {
				
				@Override
				public void onSuccess(Result result) {
					cancelNewTransformation(transformation);
					NewTransformationSavedEvent.fire(TransformationListPresenter.this,transformation.getPhenotype(),transformation.getDataset(),newTransformation);
				}
			});
		}
		
	}

	@Override
	public void deleteTransformation(final Transformation transformation) {
		dispatch.execute(new DeleteTransformationAction(transformation.getPhenotype(),transformation.getDataset(),transformation.getName(),backendResultReader), new GWASCallback<BaseStatusResult>(getEventBus()) {
			@Override
			public void onSuccess(BaseStatusResult result) {
				if (result.result.getStatus() == BackendResult.STATUS.OK)
					DeleteTransformationEvent.fire(TransformationListPresenter.this,transformation.getPhenotype(),transformation.getDataset());
				else
					DisplayNotificationEvent.fireError(this,"Backend-Error", result.result.getStatustext());
			}
		});
	}

	@Override
	public void performGWAS(final Transformation transformation, final TYPE analysis) {
		dispatch.execute(new CheckGWASAction(backendResultReader),new GWASCallback<CheckGWASActionResult>(getEventBus()) {
				@Override
				public void onSuccess(CheckGWASActionResult result) {
					if (result.getResult().getStatus() == BackendResult.STATUS.OK) {
						final RunGWASAction gwasAction = new RunGWASAction(transformation.getPhenotype(),transformation.getDataset(), transformation.getName(), analysis, gwasResultReader);
						dispatch.execute(gwasAction, new GWASCallback<RunGWASActionResult>(getEventBus()) {
							@Override
							public void onFailure(Throwable caught) {
								ProgressBarEvent.fire(this,gwasAction.getUrl(),true);
								getView().showNotification("", "Error", "GWAS analysis failed");
								super.onFailure(caught);
							}
				
							@Override
							public void onSuccess(RunGWASActionResult result) {
								if (result.result.getStatus() ==  BackendResult.STATUS.OK) {
									googleAnalytics.trackEvent("GWAS", "successful");
									RunGWASFinishedEvent.fire(TransformationListPresenter.this, result.Chromosome, result.Position, result.Phenotypes, result.Phenotype, result.Dataset,result.Transformation, result.ResultName);
								}
								else if (result.result.getStatus() == BackendResult.STATUS.WARNING) {
									DisplayNotificationEvent.fireWarning(TransformationListPresenter.this, "GWAS-Analysis", result.result.getStatustext());
								}
								else {
									googleAnalytics.trackEvent("GWAS", "failed");
									ProgressBarEvent.fire(this,gwasAction.getUrl(),true);
									getView().showNotification("", "Error", "GWAS analysis failed");
									DisplayNotificationEvent.fireError(TransformationListPresenter.this, "Backend-Error", result.result.getStatustext());
								}
							}
						});
						ProgressBarEvent.fire(this,gwasAction.getUrl());
					}
					else
					{
						DisplayNotificationEvent.fireWarning(TransformationListPresenter.this, "GWAS-Analysis", result.getResult().getStatustext());
					}
				}
		});
	}
	
	
}
