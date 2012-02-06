package com.gmi.gwaswebapp.client.mvp.result.details;

import java.util.List;

import at.gmi.nordborglab.widgets.geneviewer.client.datasource.impl.JBrowseDataSourceImpl;



import com.gmi.gwaswebapp.client.command.GetAssociationDataAction;
import com.gmi.gwaswebapp.client.command.GetAssociationDataActionResult;
import com.gmi.gwaswebapp.client.command.RunGWASAction;
import com.gmi.gwaswebapp.client.command.RunGWASActionResult;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.Analysis.TYPE;
import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.Cofactor;
import com.gmi.gwaswebapp.client.dto.Readers.GWASResultReader;
import com.gmi.gwaswebapp.client.dto.ResultData;
import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.gmi.gwaswebapp.client.events.ProgressBarEvent;
import com.gmi.gwaswebapp.client.events.RunGWASFinishedEvent;
import com.gmi.gwaswebapp.client.mvp.transformation.list.TransformationListPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ResultDetailPresenter extends PresenterWidget<ResultDetailPresenter.MyView> implements ResultDetailUiHandlers{

	public interface MyView extends View,HasUiHandlers<ResultDetailUiHandlers>{

		HasData<Cofactor> getDisplay();

		void drawAssociationCharts(List<DataTable> dataTables,List<Cofactor> cofactors,
				List<Integer> chrLengths, double maxScore,
				double bonferroniThreshold);
		void clearAssociationCharts();
		void drawStatisticPlots(DataView view);
		void showSNPPopup(int chromosome, int position, int x, int y);
		
		void setDownloadURL(String url);

		void drawVarStatisticChart(AbstractDataTable data);
	}
	
	private Analysis analysis;
	private final DispatchAsync dispatch;
	protected ListDataProvider<Cofactor> dataProvider = new ListDataProvider<Cofactor>();
	protected DataTable statistics_data = null;
	protected DataTable varStatData = null;
	protected List<DataTable> dataTables = null;
	private final GWASResultReader gwasResultReader;
	
	@Inject
	public ResultDetailPresenter(EventBus eventBus, MyView view,final DispatchAsync dispatch,final GWASResultReader gwasResultReader) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.dispatch = dispatch;
		this.gwasResultReader = gwasResultReader;
		dataProvider.addDataDisplay(getView().getDisplay());
	}
	
	
	public void loadData(final Analysis analysis)  {
		this.analysis = analysis;
		this.initStatistics();
		String download_url = "/gwas/downloadAssociationData?phenotype="+analysis.getPhenotype()+"&dataset="+analysis.getDataset()+"&transformation="+analysis.getTransformation()+"&analysis="+analysis.getName()+"&result_name="+analysis.getResultName();
		getView().setDownloadURL(download_url);
		dispatch.execute(new GetAssociationDataAction(analysis.getPhenotype(),analysis.getDataset(),analysis.getTransformation(),analysis.getType().toString(),analysis.getResultName()), new GWASCallback<GetAssociationDataActionResult>(getEventBus()) {

			@Override
			public void onSuccess(GetAssociationDataActionResult result) {
				final MyView v = getView();
				ResultData info = result.getResultData();
				dataTables = info.getAssociationTables();
				dataProvider.setList(analysis.getCofactors());
				v.drawAssociationCharts(dataTables,analysis.getCofactors(),info.getChrLengths(),info.getMaxScore(),info.getBonferroniThreshold());
			}
		});
	}
	
	private void initStatistics() {
		if (analysis != null) {
			statistics_data = DataTable.create();
			statistics_data.addColumn(ColumnType.STRING, "Step");
			statistics_data.addColumn(ColumnType.NUMBER, "BIC");
			statistics_data.addColumn(ColumnType.NUMBER, "eBIC");
			statistics_data.addColumn(ColumnType.NUMBER, "max_cof_pv");
			statistics_data.addColumn(ColumnType.NUMBER, "pseudo-heritability");
			varStatData = DataTable.create();
			varStatData.addColumn(ColumnType.STRING, "Step");
			varStatData.addColumn(ColumnType.NUMBER, "Explained");
			varStatData.addColumn(ColumnType.NUMBER, "Genetic");
			varStatData.addColumn(ColumnType.NUMBER, "Error");
			
			for (Cofactor cofactor:analysis.getCofactors()) 
			{
				 int index = statistics_data.addRow();
				 varStatData.addRow();
				 statistics_data.setValue(index, 0, cofactor.getStep().toString());
				 statistics_data.setValue(index, 1, cofactor.getBic());
				 statistics_data.setValue(index, 2, cofactor.getEbic());
				 statistics_data.setValue(index, 3, -1*Math.log10(cofactor.getMaxCofPval()));
				 statistics_data.setValue(index, 4, cofactor.getPseudoHeritability());
				 varStatData.setValue(index, 0, cofactor.getStep().toString());
				 varStatData.setValue(index, 1, cofactor.getPercVarExpl());
				 varStatData.setValue(index, 2, cofactor.getRemainingPercGenVar());
				 varStatData.setValue(index, 3, cofactor.getRemainingPercErrVar());
			}
			getView().drawVarStatisticChart(varStatData);
		}
		
	}


	@Override
	public void onSelectSNP(int chromosome, int position, int x, int y) {
		if (analysis.getType() == TYPE.EMMAX)
			getView().showSNPPopup(chromosome,position,x,y);
	}


	@Override
	public void loadStatisticChart(String value) {
		DataView view = DataView.create(statistics_data);
		view.setColumns(new int[]{0, Integer.parseInt(value)});
		getView().drawStatisticPlots(view);
		
	}


	@Override
	public void runStepWiseGWAS(Integer chromosome, Integer position) {
		if (analysis.getType() != TYPE.EMMAX) {
			DisplayNotificationEvent.fireError(ResultDetailPresenter.this, "GWAS-analysis", "Step-wise is only supported for EMMAX");
			return;
		}
		final RunGWASAction gwasAction = new RunGWASAction(analysis.getPhenotype(),analysis.getDataset(), analysis.getTransformation(),analysis.getType(),analysis.getResultName(),chromosome,position,gwasResultReader);
		dispatch.execute(gwasAction, new GWASCallback<RunGWASActionResult>(getEventBus()) {
			@Override
			public void onFailure(Throwable caught) {
				ProgressBarEvent.fire(this,gwasAction.getUrl(),true);
				super.onFailure(caught);
			}
			
			@Override
			public void onSuccess(RunGWASActionResult result) {
				if (result.result.getStatus() ==  BackendResult.STATUS.OK)
					RunGWASFinishedEvent.fire(ResultDetailPresenter.this, result.Chromosome, result.Position, result.Phenotypes, result.Phenotype, result.Dataset, result.Transformation, result.ResultName);
				else {
					ProgressBarEvent.fire(this,gwasAction.getUrl(),true);
					DisplayNotificationEvent.fireError(ResultDetailPresenter.this, "Backend-Error", result.result.getStatustext());
				}
					
			}
		});
		ProgressBarEvent.fire(this,gwasAction.getUrl());
	}
	
	

}
