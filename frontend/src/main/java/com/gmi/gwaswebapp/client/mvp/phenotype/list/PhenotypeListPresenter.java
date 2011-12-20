package com.gmi.gwaswebapp.client.mvp.phenotype.list;

import java.util.List;

import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.DeletePhenotypeAction;
import com.gmi.gwaswebapp.client.command.DeleteTransformationAction;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.gmi.gwaswebapp.client.events.DeleteTransformationEvent;
import com.gmi.gwaswebapp.client.events.ProgressBarEvent;
import com.gmi.gwaswebapp.client.events.UpdateDataEvent;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter;
import com.gmi.gwaswebapp.client.mvp.progress.ProgressPresenter;
import com.gmi.gwaswebapp.client.mvp.transformation.list.TransformationListPresenter;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PhenotypeListPresenter extends PresenterWidget<PhenotypeListPresenter.MyView> implements PhenotypeListUIHandlers{

	
	public interface MyView extends View,HasUiHandlers<PhenotypeListUIHandlers> {
		HasData<Phenotype> getDisplay();
		void drawValueStatistics(AbstractDataTable value_view);
		void drawInfoStatistics(AbstractDataTable info_view);
		void clearStatistics();
	}
	
	protected ListDataProvider<Phenotype> phenotypeDataProvider = new ListDataProvider<Phenotype>();
	private final DispatchAsync dispatch;
	private final BackendResultReader backendResultReader;
	
	@Inject
	public PhenotypeListPresenter(EventBus eventBus, MyView view,final DispatchAsync dispatch,final BackendResultReader backendResultReader,final ProgressPresenter progressPresenter) {
		super(eventBus, view);
		this.dispatch = dispatch;
		this.backendResultReader = backendResultReader;
		getView().setUiHandlers(this);
		phenotypeDataProvider.addDataDisplay(getView().getDisplay());
	}


	public void setPhenotypeList(List<Phenotype> phenotypes) {
		phenotypeDataProvider.setList(phenotypes);
	}
	

	private DataTable getStatisticsData(List<Phenotype> phenotypes) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING,"Phenotype");
		data.addColumn(ColumnType.NUMBER,"Values");
		data.addColumn(ColumnType.NUMBER,"Datasets");
		data.addColumn(ColumnType.NUMBER,"Transformations");
		data.addColumn(ColumnType.NUMBER,"Results");
		data.addRows(phenotypes.size());
		for (int i = 0;i< phenotypes.size();i++)  {
			Phenotype phenotype = phenotypes.get(i);
			data.setValue(i, 0, phenotype.getName());
			data.setValue(i, 1, phenotype.getNumValues());
			data.setValue(i, 2, phenotype.getDatasets().size());
			data.setValue(i, 3, phenotype.getTransformationCount());
			data.setValue(i ,4, phenotype.getResultCount());
		}
		return data;
	}
	
	


	@Override
	public void onReset() {
		super.onReset();
		DataTable data = getStatisticsData(phenotypeDataProvider.getList());
		DataView value_view  = DataView.create(data);
		value_view.setColumns(new int[] {0,1});
		DataView info_view = DataView.create(data);
		info_view.setColumns(new int[] {0,2,3,4});
		getView().clearStatistics();
		getView().drawValueStatistics(value_view);
		getView().drawInfoStatistics(info_view);
	}


	@Override
	public void deletePhenotype(Phenotype phenotype) {
		dispatch.execute(new DeletePhenotypeAction(phenotype.getName(),backendResultReader), new GWASCallback<BaseStatusResult>(getEventBus()) {
			@Override
			public void onSuccess(BaseStatusResult result) {
				UpdateDataEvent.fire(PhenotypeListPresenter.this);
			}
		});
	}
}
