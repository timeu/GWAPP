package com.gmi.gwaswebapp.client.mvp.result.list;

import java.util.List;


import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.DeleteResultAction;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.BaseModel;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.gmi.gwaswebapp.client.events.DeleteResultEvent;
import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ResultListPresenter extends PresenterWidget<ResultListPresenter.MyView> implements ResultListUiHandlers{

	

	public interface MyView extends View,HasUiHandlers<ResultListUiHandlers>{

		HasData<Analysis> getDisplay();

		void setSelectionModel(SingleSelectionModel<BaseModel> selectionModel);

	}
	
	private final DispatchAsync dispatch;
	private final BackendResultReader backendResultReader;
	private  ListDataProvider<Analysis> resultsDataProvider = new ListDataProvider<Analysis>();
	 
	@Inject
	public ResultListPresenter(EventBus eventBus, MyView view,final DispatchAsync dispatch,final BackendResultReader backendResultReader) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.dispatch = dispatch;
		this.backendResultReader = backendResultReader;
		resultsDataProvider.addDataDisplay(getView().getDisplay());
	}
	
	
	public void setResults(List<Analysis> results) {
		resultsDataProvider.setList(results);
	}
	
	public void setSelectionModel(SingleSelectionModel<BaseModel> selectionModel) {
		getView().setSelectionModel(selectionModel);
	}


	@Override
	public void deleteAnalysis(final Analysis analysis) {
		dispatch.execute(new DeleteResultAction(analysis.getPhenotype(),analysis.getDataset(),analysis.getTransformation(),analysis.getType().toString(),analysis.getResultName(),backendResultReader), new GWASCallback<BaseStatusResult>(getEventBus()) {
			@Override
			public void onSuccess(BaseStatusResult result) {
				if (result.result.getStatus() == BackendResult.STATUS.OK)
					DeleteResultEvent.fire(ResultListPresenter.this,analysis.getPhenotype(),analysis.getDataset(),analysis.getTransformation());
				else
					DisplayNotificationEvent.fireError(ResultListPresenter.this,"Backend-Error",result.result.getStatustext());
			}
		});
	}

}
