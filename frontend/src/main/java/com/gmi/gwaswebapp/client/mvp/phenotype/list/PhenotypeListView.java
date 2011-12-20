package com.gmi.gwaswebapp.client.mvp.phenotype.list;

import java.util.ArrayList;
import java.util.List;

import com.gmi.gwaswebapp.client.mvp.phenotype.list.PhenotypeListPresenter.MyView;
import com.gmi.gwaswebapp.client.resources.CellTableResources;
import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.DataView;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class PhenotypeListView extends ViewWithUiHandlers<PhenotypeListUIHandlers> implements MyView {

	private static PhenotypeListUiBinder uiBinder = GWT
			.create(PhenotypeListUiBinder.class);
	
	interface MyStyle extends CssResource {
	  
	}

	interface PhenotypeListUiBinder extends UiBinder<Widget, PhenotypeListView> {
	}
	
	protected Widget widget;
	@UiField MyStyle style;
	@UiField(provided = true) CellTable<Phenotype> phenotypeTable;
	@UiField HTMLPanel statistics_left_container;
	@UiField HTMLPanel statistics_right_container;
	//@UiField HTMLPanel info_statistics_container;
	private BarChart value_statistics_chart;
	private BarChart info_statistics_chart;
	private CellTableResources cellTableResources;

	@Inject
	public PhenotypeListView(final CellTableResources cellTableResources) {
		this.cellTableResources = cellTableResources;
		phenotypeTable = new CellTable<Phenotype>(15,cellTableResources);
		initCellTable();
		widget = uiBinder.createAndBindUi(this);
		
	}
	
	private void initCellTable() {
		phenotypeTable.addColumn(new PhenotypeCellTableColumns.NameColumn(),"Name");
		phenotypeTable.addColumn(new PhenotypeCellTableColumns.NumberColumn(),"#-Values");
		phenotypeTable.addColumn(new PhenotypeCellTableColumns.SubsetColumn(),"Subsets");
		/*phenotypeTable.addColumn(new PhenotypeCellTableColumns.StdevColumn(),"std");
		phenotypeTable.addColumn(new PhenotypeCellTableColumns.GrowthConditionColumn(),"Condition");
		phenotypeTable.addColumn(new PhenotypeCellTableColumns.PhenotypScoringColumn(),"Scoring");
		phenotypeTable.addColumn(new PhenotypeCellTableColumns.MethodDescriptionColumn(),"Method");
		phenotypeTable.addColumn(new PhenotypeCellTableColumns.MeasurementScaleColumn(),"Scale");*/
		
		List<HasCell<Phenotype,?>> actionCells = new ArrayList<HasCell<Phenotype,?>>();
		
		/*actionCells.add(new PhenotypeCellTableColumns.ActionHasCell(new ActionCell<Phenotype>("QQ-Plot",new ActionCell.Delegate<Phenotype>() {
			@Override
			public void execute(Phenotype object) {
				//getUiHandlers().showQQPlot(object);
			}
		})));*/
		
		
		actionCells.add(new PhenotypeCellTableColumns.ActionHasCell(new ActionCell<Phenotype>("Delete",new ActionCell.Delegate<Phenotype>() {
			@Override
			public void execute(Phenotype object) {
				if (Window.confirm("Do you really want to delete the phenotype? (all related transformations and GWAS results will also be deleted"))
				{
					getUiHandlers().deletePhenotype(object);
				}
			}
		})));
		phenotypeTable.addColumn(new IdentityColumn<Phenotype>(new CompositeCell<Phenotype>(actionCells)),"Action");
	}
	
	@Override
	public HasData<Phenotype> getDisplay() {
		return phenotypeTable;
	}
	
	

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void drawValueStatistics(AbstractDataTable data) {
		value_statistics_chart = new BarChart(data, createOptions("Values"));
		//value_statistics_chart.addStyleName(style.chart_left());
		statistics_left_container.add(value_statistics_chart);
	}
	
	
	@Override
	public void drawInfoStatistics(AbstractDataTable data) {
		info_statistics_chart = new BarChart(data, createOptions("Info"));
		//info_statistics_chart.addStyleName(style.chart_right());
		statistics_right_container.add(info_statistics_chart);
		
	}
	
	private Options createOptions(String title) {
		Options options = Options.create();
		options.setTitle(title);
		options.setHeight(550);
		int width = getAvailableHorizontalSpace();
		options.setWidth((width)/2);
		options.setBackgroundColor("#FAFAFA");
		ChartArea area = ChartArea.create();
		area.setLeft("10%");
		area.setTop("8%");
		area.setHeight("92%");
		options.setChartArea(area);
		return options;
	}

	@Override
	public void clearStatistics() {
		statistics_left_container.clear();
		statistics_right_container.clear();
	}

	private int getAvailableHorizontalSpace()  {
		int width = widget.getOffsetWidth() -38 ;
		if (width <= 0)
			width = Window.getClientWidth() - 160 - 58;
		return width;
	}
}
