package com.gmi.gwaswebapp.client.mvp.result.details;

import java.util.ArrayList;
import java.util.List;

import at.gmi.nordborglab.widgets.geneviewer.client.datasource.impl.GeneSuggestion;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.impl.JBrowseDataSourceImpl;
import at.gmi.nordborglab.widgets.geneviewer.client.datasource.impl.ServerSuggestOracle;
import at.gmi.nordborglab.widgets.gwasgeneviewer.client.GWASGeneViewer;

import org.danvk.dygraphs.client.events.DataPoint;
import org.danvk.dygraphs.client.events.SelectHandler;
import org.danvk.dygraphs.client.events.SelectHandler.SelectEvent;
import com.gmi.gwaswebapp.client.dto.Cofactor;
import com.gmi.gwaswebapp.client.mvp.result.details.ResultDetailPresenter.MyView;
import com.gmi.gwaswebapp.client.resources.CellTableResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.view.client.HasData;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.inject.Inject;

import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ResultDetailView extends ViewWithUiHandlers<ResultDetailUiHandlers> implements MyView {

	private static ResultDetailViewUiBinder uiBinder = GWT
			.create(ResultDetailViewUiBinder.class);

	interface ResultDetailViewUiBinder extends
			UiBinder<Widget, ResultDetailView> {
	}
	
	private final Widget widget;
	protected JBrowseDataSourceImpl geneDataSource = new JBrowseDataSourceImpl("/gwas/");
	protected SNPPopup snpPopup = new SNPPopup();
	@UiField(provided=true)	final SuggestBox searchGene;
	@UiField FlowPanel container;
	@UiField FlowPanel container_cofactor;
	//@UiField Button selected_optimal_snp;
	@UiField(provided = true) CellTable<Cofactor> cofactorTable;
	@UiField(provided = true) LineChart statistic_chart;
	@UiField ListBox statistic_type;
	@UiField AnchorElement download_link;
	protected List<GWASGeneViewer> gwasGeneViewers = new ArrayList<GWASGeneViewer>();
	private String[] colors = {"blue", "green", "red", "cyan", "purple"};
	private String[] gene_mark_colors = {"red", "red", "blue", "red", "green"};
	

	@Inject
	public ResultDetailView(final CellTableResources cellTableResources) {
		searchGene = new SuggestBox(new ServerSuggestOracle(geneDataSource,5));
		((DefaultSuggestionDisplay)searchGene.getSuggestionDisplay()).setAnimationEnabled(true);
		searchGene.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				GeneSuggestion suggestion =  (GeneSuggestion)event.getSelectedItem();
				GWASGeneViewer viewer = getGWASGeneViewer(suggestion.getGene().getChromosome());
				if (viewer != null)
				{
					viewer.clearDisplayGenes();
					viewer.addDisplayGene(suggestion.getGene());
					viewer.refresh();
				}
			}
		});
		cofactorTable = new CellTable<Cofactor>(15,cellTableResources);
		cofactorTable.addColumn(new TextColumn<Cofactor>() {

			@Override
			public String getValue(Cofactor object) {
				return object.getStep().toString();
			}
		},"Step");
		cofactorTable.addColumn(new TextColumn<Cofactor>() {

			@Override
			public String getValue(Cofactor object) {
				if (object.getChr() != null)
					return object.getChr().toString();
				else return "";
			}
		},"Chr.");
		cofactorTable.addColumn(new TextColumn<Cofactor>(){

			@Override
			public String getValue(Cofactor object) {
				if (object.getPos() != null)
					return object.getPos().toString();
				else
					return "";
			}
			
		}, "Position");
		cofactorTable.addColumn(new TextColumn<Cofactor>(){

			@Override
			public String getValue(Cofactor object) {
				return Double.toString(object.getBic());
			}
			
		}, "BIC");
		cofactorTable.addColumn(new TextColumn<Cofactor>(){

			@Override
			public String getValue(Cofactor object) {
				return Double.toString(object.getEbic());
			}
			
		}, "e-BIC");
		cofactorTable.addColumn(new TextColumn<Cofactor>(){

			@Override
			public String getValue(Cofactor object) {
				return Double.toString(object.getMbic());
			}
			
		}, "m-BIC");
		cofactorTable.addColumn(new TextColumn<Cofactor>(){

			@Override
			public String getValue(Cofactor object) {
				return Double.toString(object.getMaxCofPval());
			}
			
		}, "mbonf");
		cofactorTable.addColumn(new TextColumn<Cofactor>(){

			@Override
			public String getValue(Cofactor object) {
				return Double.toString(object.getPseudoHeritability());
			}
			
		}, "pseudo-heritability");
		statistic_chart = new LineChart(DataTable.create(), createStatsticChartOptions());
		widget = uiBinder.createAndBindUi(this);
		if (statistic_type.getItemCount() == 0)
		{
			statistic_type.addItem("", "");
			statistic_type.addItem("BIC", "1");
			statistic_type.addItem("mBIC", "2");
			statistic_type.addItem("eBIC", "3");
			statistic_type.addItem("max cofactor pValue", "4");
			statistic_type.addItem("pseudo-heritability", "5");
		}
		
		snpPopup.getRunStepWiseLink().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getUiHandlers().runStepWiseGWAS(snpPopup.chromosome,snpPopup.position);
				snpPopup.hide();
			}
		});
	}

	private Options createStatsticChartOptions() {
		Options options = Options.create();
	    options.setWidth(600);
	    options.setHeight(400);
	    return options;
	}

	protected GWASGeneViewer getGWASGeneViewer(String chromosome) {
		for (GWASGeneViewer gwasGeneViewer : gwasGeneViewers) {
			
			if (gwasGeneViewer.getChromosome().equals(chromosome)) 
				return gwasGeneViewer;
		}
		return null;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasData<Cofactor> getDisplay() {
		return cofactorTable;
	}

	
	@Override
	public void clearAssociationCharts() {
		container.clear();
		gwasGeneViewers.clear();
	}

	@Override
	public void drawAssociationCharts(List<DataTable> dataTables,List<Cofactor> cofactors,
			List<Integer> chrLengths, double maxScore,
			double bonferroniThreshold) {
		clearAssociationCharts();
		Integer i = 1;
		java.util.Iterator<DataTable> iterator = dataTables.iterator();
		int minWidth = 600;
		int width = container.getOffsetWidth() - 60;
		if (width < minWidth)
			width = Window.getClientWidth() - 160 - 49;
		if (width < minWidth)
			width = minWidth;
		
		while(iterator.hasNext())
		{
			DataTable dataTable = iterator.next();
			String color = colors[i%colors.length];
			String gene_marker_color = gene_mark_colors[i%gene_mark_colors.length];
			GWASGeneViewer chart = new GWASGeneViewer("Chr"+i.toString(), color, gene_marker_color, geneDataSource);
			gwasGeneViewers.add(chart);
			for (Cofactor cofactor: cofactors){
				if (cofactor.getChr() == i)
				{
					chart.addSelection(GWASGeneViewer.getSelectionFromPos(dataTable, cofactor.getPos()));
					cofactors.remove(i);
				}
			}
			chart.addSelectionHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					DataPoint point = event.point;
					Event mouseEvent = event.event;
					String id = event.id;
					int chromosome;
					try
					{
						chromosome = Integer.parseInt(id);
					}
					catch (Exception e)
					{
						chromosome =Integer.parseInt(id.charAt(3)+"");
					}
					getUiHandlers().onSelectSNP(chromosome,(int)point.getXVal(),mouseEvent.getClientX(),mouseEvent.getClientY());
				}
				
			});
			chart.setGeneInfoUrl("http://arabidopsis.org/servlets/TairObject?name={0}&type=gene");
			container.add((IsWidget)chart);
			chart.draw(dataTable,maxScore,0,chrLengths.get(i-1),bonferroniThreshold);
			i++;
		}
	}
	
	@UiHandler("statistic_type")
	public void onChangeStatisticType(ChangeEvent event) {
		if (statistic_type.getSelectedIndex() <=0 )
			statistic_chart.setVisible(false);
		else
			getUiHandlers().loadStatisticChart(statistic_type.getValue(statistic_type.getSelectedIndex()));
	}

	@Override
	public void drawStatisticPlots(DataView view) {
		statistic_chart.setVisible(true);
		Options options = createStatsticChartOptions();
		statistic_chart.draw(view,options);
		
	}

	@Override
	public void showSNPPopup(int chromosome, int position, int x, int y) {
		snpPopup.setDataPoint(chromosome, position);
		snpPopup.setPosition(x, y);
		snpPopup.show();
	}

	@Override
	public void setDownloadURL(String url) {
		download_link.setHref(url);
	}



}