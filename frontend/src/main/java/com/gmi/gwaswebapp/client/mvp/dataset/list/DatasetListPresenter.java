package com.gmi.gwaswebapp.client.mvp.dataset.list;

import java.util.HashMap;
import java.util.List;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.DeleteDatasetAction;
import com.gmi.gwaswebapp.client.command.DeleteTransformationAction;
import com.gmi.gwaswebapp.client.command.GetAccessionsAction;
import com.gmi.gwaswebapp.client.command.GetAccessionsActionResult;
import com.gmi.gwaswebapp.client.command.GetLocationDistributionAction;
import com.gmi.gwaswebapp.client.command.GetLocationDistributionActionResult;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.gmi.gwaswebapp.client.events.DeleteDatasetEvent;
import com.gmi.gwaswebapp.client.events.DeleteTransformationEvent;
import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.gmi.gwaswebapp.client.events.NewDatasetEvent;
import com.gmi.gwaswebapp.client.mvp.transformation.list.TransformationListPresenter;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;

public class DatasetListPresenter extends
		PresenterWidget<DatasetListPresenter.MyView> implements DatasetListUiHandlers {

	public interface MyView extends View,HasUiHandlers<DatasetListUiHandlers> {
		HasData<Dataset> getDisplay();
		void clearStatistics();
		void drawValueStatistics(AbstractDataTable data);
		void drawInfoStatistics(AbstractDataTable data);
		void drawLocationDistribution(Dataset dataset,AbstractDataTable data);
	}
	
	private ListDataProvider<Dataset> datasetListProvider = new ListDataProvider<Dataset>();
	private SingleSelectionModel<Dataset> selectionModel = new SingleSelectionModel<Dataset>();
	private HashMap<String,DataTable> locationDistributionDataTables = null;
	private final DispatchAsync dispatch;
	private final BackendResultReader backendResultReader;

	@Inject
	public DatasetListPresenter(final EventBus eventBus, final MyView view,final DispatchAsync dispatch, final BackendResultReader backendResultReader) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.dispatch = dispatch;
		this.backendResultReader = backendResultReader;
		datasetListProvider.addDataDisplay(getView().getDisplay());
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (selectionModel.getSelectedObject() != null)
					drawLocationDistribution(selectionModel.getSelectedObject());
			}
		});
		getView().getDisplay().setSelectionModel(selectionModel);
	}

	@Override
	public void onReveal() {
		super.onReveal();
	}
	
	
	@Override
	public void onReset() {
		super.onReset();
		DataTable data = getStatisticsData(datasetListProvider.getList());
		DataView value_view  = DataView.create(data);
		value_view.setColumns(new int[] {0,1});
		DataView info_view = DataView.create(data);
		info_view.setColumns(new int[] {0,2,3,4,5});
		getView().clearStatistics();
		getView().drawValueStatistics(value_view);
		getView().drawInfoStatistics(info_view);
		if (selectionModel.getSelectedObject() == null) 
			selectionModel.setSelected(datasetListProvider.getList().get(0), true);
		else
		{
			drawLocationDistribution(selectionModel.getSelectedObject());
		}
	}
	
	public void setData(List<Dataset> datasets) {
		if (selectionModel.getSelectedObject() != null) {
			selectionModel.setSelected(selectionModel.getSelectedObject(),false);
		}
		locationDistributionDataTables = null;
		datasetListProvider.setList(datasets);
	}
	
	private DataTable getStatisticsData(List<Dataset> datasets) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING,"Dataset");
		data.addColumn(ColumnType.NUMBER,"Values");
		data.addColumn(ColumnType.NUMBER,"Transformations");
		data.addColumn(ColumnType.NUMBER,"Emmax");
		data.addColumn(ColumnType.NUMBER,"KW");
		data.addColumn(ColumnType.NUMBER,"LM");
		data.addRows(datasets.size());
		for (int i = 0;i< datasets.size();i++)  {
			Dataset dataset = datasets.get(i);
			data.setValue(i, 0, dataset.getName());
			data.setValue(i, 1, dataset.getAccessionCount());
			data.setValue(i, 2, dataset.getTransformations().size());
			data.setValue(i, 3, dataset.getResultCount(Analysis.TYPE.EMMAX));
			data.setValue(i, 4, dataset.getResultCount(Analysis.TYPE.KW));
			data.setValue(i, 5, dataset.getResultCount(Analysis.TYPE.LM));
		}
		return data;
	}
	
	private void drawLocationDistribution(final Dataset dataset)  {
		if (locationDistributionDataTables == null)	{
			dispatch.execute(new GetLocationDistributionAction(dataset.getPhenotype()), new GWASCallback<GetLocationDistributionActionResult>(getEventBus()) {

				@Override
				public void onSuccess(GetLocationDistributionActionResult result) {
					locationDistributionDataTables = result.getDataTables();
					DataTable dataTable = locationDistributionDataTables.get(dataset.getInternId());
					drawLocationDistribution(dataset,dataTable);
				}
			});
		}
		else {
			DataTable dataTable = locationDistributionDataTables.get(dataset.getInternId());
			if (dataTable == null) {
				dataTable = dataset.calculateLocDistribution();
				locationDistributionDataTables.put(dataset.getInternId(), dataTable);
			}
			drawLocationDistribution(dataset, dataTable);
		}
	}
	
	private void drawLocationDistribution(Dataset dataset,DataTable dataTable) {
		getView().drawLocationDistribution(dataset,dataTable);
	}



	@Override
	public void deleteDataset(final Dataset dataset) {
		if (dataset.isAdd())
		{
			DeleteDatasetEvent.fire(DatasetListPresenter.this,dataset.getPhenotype());
		}
		else
		{
			dispatch.execute(new DeleteDatasetAction(dataset.getPhenotype(),dataset.getInternId(),backendResultReader), new GWASCallback<BaseStatusResult>(getEventBus()) {
				@Override
				public void onSuccess(BaseStatusResult result) {
					if (result.result.getStatus() == BackendResult.STATUS.OK)
						DeleteDatasetEvent.fire(DatasetListPresenter.this,dataset.getPhenotype());
					else
						DisplayNotificationEvent.fireError(getEventBus(), "Deleting Dataset failed",result.result.getStatustext() );
				}
			});
		}
	}

	@Override
	public void createSubset(Dataset dataset) {
		NewDatasetEvent.fire(getEventBus(), dataset);
	}
}
