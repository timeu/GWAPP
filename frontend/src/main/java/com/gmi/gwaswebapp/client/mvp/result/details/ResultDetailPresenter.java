package com.gmi.gwaswebapp.client.mvp.result.details;

import java.util.List;

import at.gmi.nordborglab.widgets.ldviewer.client.datasource.FetchExactLDCallback;
import at.gmi.nordborglab.widgets.ldviewer.client.datasource.FetchLDCallback;
import at.gmi.nordborglab.widgets.ldviewer.client.datasource.FetchLDForSNPCallback;
import at.gmi.nordborglab.widgets.ldviewer.client.datasource.LDDataSource;
import at.gmi.nordborglab.widgets.ldviewer.client.datasource.impl.LDDataForSNP;

import com.gmi.gwaswebapp.client.command.CheckGWASAction;
import com.gmi.gwaswebapp.client.command.CheckGWASActionResult;
import com.gmi.gwaswebapp.client.command.GetAssociationDataAction;
import com.gmi.gwaswebapp.client.command.GetAssociationDataActionResult;
import com.gmi.gwaswebapp.client.command.RunGWASAction;
import com.gmi.gwaswebapp.client.command.RunGWASActionResult;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.Analysis.TYPE;
import com.gmi.gwaswebapp.client.dto.BackendResult;
import com.gmi.gwaswebapp.client.dto.Cofactor;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.gmi.gwaswebapp.client.dto.Readers.GWASResultReader;
import com.gmi.gwaswebapp.client.dto.ResultData;
import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.gmi.gwaswebapp.client.events.LoadingIndicatorEvent;
import com.gmi.gwaswebapp.client.events.ProgressBarEvent;
import com.gmi.gwaswebapp.client.events.RunGWASFinishedEvent;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
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
				double pvalThreshold, Double med_pval, Double ks_stat, Double ks_pval);
		void clearAssociationCharts();
		void drawStatisticPlots(DataView view);
		void showSNPPopup(int chromosome, int position, int x, int y,boolean showStepWiseLink);
		
		void setDownloadURL(String url);

		void drawVarStatisticChart(AbstractDataTable data);

		void detachCharts();

		String getSelectedStatistic();

		void showLDPlot(JsArrayInteger snps, JsArray<JsArrayNumber> r2Values,
				int chr, int startSNP, int stopSNP);

		void highlightLD(int position,LDDataForSNP data);

		void setUploadGenomeStatsFormUrl(String url,String urlParameters);
	}
	
	private Analysis analysis;
	private final DispatchAsync dispatch;
	protected ListDataProvider<Cofactor> dataProvider = new ListDataProvider<Cofactor>();
	protected DataTable statistics_data = null;
	protected DataTable varStatData = null;
	protected List<DataTable> dataTables = null;
	private final GWASResultReader gwasResultReader;
	private final BackendResultReader backendResultReader;
	private final LDDataSource ldDataSource;
	
	@Inject
	public ResultDetailPresenter(EventBus eventBus, MyView view,final DispatchAsync dispatch,final GWASResultReader gwasResultReader,final BackendResultReader backendResultReader,final LDDataSource ldDataSource) {
		super(eventBus, view);
		getView().setUiHandlers(this);
		this.dispatch = dispatch;
		this.gwasResultReader = gwasResultReader;
		this.backendResultReader = backendResultReader;
		this.ldDataSource = ldDataSource;
		dataProvider.addDataDisplay(getView().getDisplay());
	}
	
	
	public void loadData(final Analysis analysis)  {
		this.analysis = analysis;
		this.initStatistics();
		getView().setUploadGenomeStatsFormUrl("/gwas/uploadGenomeStatsData","phenotype="+analysis.getPhenotype()+"&dataset=" +analysis.getDataset());
		String download_url = "/gwas/downloadAssociationData?phenotype="+analysis.getPhenotype()+"&dataset="+analysis.getDataset()+"&transformation="+analysis.getTransformation()+"&analysis="+analysis.getType().toString().toLowerCase()+"&result_name="+analysis.getResultName();
		getView().setDownloadURL(download_url);
		dispatch.execute(new GetAssociationDataAction(analysis.getPhenotype(),analysis.getDataset(),analysis.getTransformation(),analysis.getType().toString(),analysis.getResultName()), new GWASCallback<GetAssociationDataActionResult>(getEventBus()) {

			@Override
			public void onSuccess(GetAssociationDataActionResult result) {
				final MyView v = getView();
				ResultData info = result.getResultData();
				dataTables = info.getAssociationTables();
				dataProvider.setList(analysis.getCofactors());
				v.drawAssociationCharts(dataTables,analysis.getCofactors(),info.getChrLengths(),info.getMaxScore(),info.getPvalThreshold(),info.getMedPval(),info.getKsStat(),info.getKsPval());
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
				 statistics_data.setValue(index, 3, cofactor.getMaxCofPval());
				 statistics_data.setValue(index, 4, cofactor.getPseudoHeritability());
				 varStatData.setValue(index, 0, cofactor.getStep().toString());
				 varStatData.setValue(index, 1, cofactor.getPercVarExpl());
				 varStatData.setValue(index, 2, cofactor.getRemainingPercGenVar());
				 varStatData.setValue(index, 3, cofactor.getRemainingPercErrVar());
			}
			getView().drawVarStatisticChart(varStatData);
			String selectedStatistic = getView().getSelectedStatistic();
			if (selectedStatistic != null) {
				loadStatisticChart(selectedStatistic);
			}
		}
		
	}


	@Override
	public void onSelectSNP(int chromosome, int position, int x, int y) {
		getView().showSNPPopup(chromosome,position,x,y,analysis.getType() == TYPE.AMM);
	}


	@Override
	public void loadStatisticChart(String value) {
		DataView view = DataView.create(statistics_data);
		view.setColumns(new int[]{0, Integer.parseInt(value)});
		getView().drawStatisticPlots(view);
		
	}


	@Override
	public void runStepWiseGWAS(final Integer chromosome, final Integer position) {
		if (analysis.getType() != TYPE.AMM && analysis.getType() != TYPE.LM) {
			DisplayNotificationEvent.fireError(ResultDetailPresenter.this, "GWAS-analysis", "Step-wise is only supported for EMMAX");
			return;
		}
		dispatch.execute(new CheckGWASAction(backendResultReader),new GWASCallback<CheckGWASActionResult>(getEventBus()) {
			@Override
			public void onSuccess(CheckGWASActionResult result) {
				if (result.getResult().getStatus() == BackendResult.STATUS.OK) {
					final RunGWASAction gwasAction = new RunGWASAction(analysis.getPhenotype(),analysis.getDataset(), analysis.getTransformation(),analysis.getType(),analysis.getResultName(),chromosome,position,gwasResultReader);
					dispatch.execute(gwasAction, new GWASCallback<RunGWASActionResult>(getEventBus()) {
						@Override
						public void onFailure(Throwable caught) {
							ProgressBarEvent.fire(this,gwasAction.getUrl(),true);
							super.onFailure(caught);
						}
						
						@Override
						public void onSuccess(RunGWASActionResult result) {
							if (result.result.getStatus() ==  BackendResult.STATUS.OK) {
								RunGWASFinishedEvent.fire(ResultDetailPresenter.this, result.Chromosome, result.Position, result.Phenotypes, result.Phenotype, result.Dataset, result.Transformation, result.ResultName);
							}
							else if (result.result.getStatus() == BackendResult.STATUS.WARNING) {
								DisplayNotificationEvent.fireWarning(ResultDetailPresenter.this, "GWAS-Analysis", result.result.getStatustext());
							}
							else {
								ProgressBarEvent.fire(this,gwasAction.getUrl(),true);
								DisplayNotificationEvent.fireError(ResultDetailPresenter.this, "Backend-Error", result.result.getStatustext());
							}
						}
					});
					ProgressBarEvent.fire(this,gwasAction.getUrl());
				}
				else
				{
					DisplayNotificationEvent.fireWarning(ResultDetailPresenter.this, "GWAS-Analysis", result.getResult().getStatustext());
				}
			}
		});
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		getView().detachCharts();
	}


	@Override
	public void showLocalLD(final Integer chromosome, Integer position) {
		int maximumSNPStoDisplay = 500;
		DataTable data = dataTables.get(chromosome-1);
		int indexOfPos = 0;
		for (int i = 0;i<data.getNumberOfRows();i++) {
			if (data.getValueInt(i, 0) == position) {
				indexOfPos = i;
				break;
			}
		}
		final int startSNP = data.getValueInt((maximumSNPStoDisplay/2 > indexOfPos ? 1 : indexOfPos - maximumSNPStoDisplay/2),0);
		final int stopSNP = data.getValueInt((maximumSNPStoDisplay/2 + indexOfPos > data.getNumberOfRows() ? data.getNumberOfRows()-2 : indexOfPos + maximumSNPStoDisplay/2), 0);
		String url = "phenotype="+analysis.getPhenotype()+"&dataset="+analysis.getDataset()+"&transformation="+analysis.getTransformation()+"&analysis="+analysis.getType().toString().toLowerCase()+"&result_name="+analysis.getResultName();
		LoadingIndicatorEvent.fire(this, true);
		ldDataSource.fetchLDValues(url,chromosome.toString(), startSNP, stopSNP, new FetchLDCallback() {
			
			@Override
			public void onFetchLDValues(JsArrayInteger snps,
					JsArray<JsArrayNumber> r2Values) {
				LoadingIndicatorEvent.fire(ResultDetailPresenter.this, false);
				getView().showLDPlot(snps,r2Values,chromosome,startSNP,stopSNP);
			}
		});
	}


	@Override
	public void runGlobalLD(Integer chromosome, final Integer position) {
		String url = "phenotype="+analysis.getPhenotype()+"&dataset="+analysis.getDataset()+"&transformation="+analysis.getTransformation()+"&analysis="+analysis.getType().toString().toLowerCase()+"&result_name="+analysis.getResultName();
		LoadingIndicatorEvent.fire(this, true);
		ldDataSource.fetchLDValuesForSNP(url,chromosome.toString(), position, new FetchLDForSNPCallback() {
			
			@Override
			public void onFetchLDForSNP(LDDataForSNP data) {
				LoadingIndicatorEvent.fire(ResultDetailPresenter.this, false);
				getView().highlightLD(position,data);
			}
		});
	}


	@Override
	public void calculateLocalExactLD(final Integer chromosome, Integer position) {
		String url = "phenotype="+analysis.getPhenotype()+"&dataset="+analysis.getDataset()+"&transformation="+analysis.getTransformation()+"&analysis="+analysis.getType().toString().toLowerCase()+"&result_name="+analysis.getResultName()+"&exact=1";
		LoadingIndicatorEvent.fire(this, true);
		ldDataSource.fetchExactLDValues(url,chromosome.toString(), position, new FetchExactLDCallback() {
			
			@Override
			public void onFetchExactLDValues(JsArrayInteger snps,
					JsArray<JsArrayNumber> r2Values, int start, int end) {
				LoadingIndicatorEvent.fire(ResultDetailPresenter.this, false);
				getView().showLDPlot(snps,r2Values,chromosome,start,end);
			}
		});
	}
}
