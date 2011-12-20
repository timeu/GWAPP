package com.gmi.gwaswebapp.client.mvp.dataset.list;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gmi.gwaswebapp.client.mvp.dataset.list.DatasetCellTableColumns.ActionHasCell;
import com.gmi.gwaswebapp.client.resources.CellTableResources;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.GeoMap;
import com.google.inject.Inject;

public class DatasetListView extends ViewWithUiHandlers<DatasetListUiHandlers> implements
		DatasetListPresenter.MyView {

	private final Widget widget;
	private final CellTableResources cellTableResources;

	public interface Binder extends UiBinder<Widget, DatasetListView> {
	}
	
	interface MyStyle extends CssResource {
	}

	@Inject
	public DatasetListView(final Binder binder,final CellTableResources cellTableResources) {
		this.cellTableResources = cellTableResources;
		datasetTable = new CellTable<Dataset>(15,cellTableResources);
		initCellTable();
		widget = binder.createAndBindUi(this);
	}
	
	@UiField MyStyle style;
	@UiField(provided=true) CellTable<Dataset> datasetTable;
	@UiField HTMLPanel statistics_left_container;
	@UiField HTMLPanel statistics_right_container;
	@UiField GeoMap geomap;
	private BarChart value_statistics_chart;
	private BarChart info_statistics_chart;

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasData<Dataset> getDisplay() {
		return datasetTable;
	}
	
	private void initCellTable() {
		datasetTable.addColumn(new DatasetCellTableColumns.NameColumn(),"Name");
		datasetTable.addColumn(new DatasetCellTableColumns.AccessionCountColumn(),"#-Accessions");
		datasetTable.addColumn(new DatasetCellTableColumns.DescriptionColumn(),"Description");
		List<HasCell<Dataset, ?>> actionCells = new ArrayList<HasCell<Dataset,?>>();
		
		actionCells.add(new ActionHasCell(new DatasetCellTableColumns.NewActionCell("Subset", new Delegate<Dataset>() {

			@Override
			public void execute(Dataset object) {
				getUiHandlers().createSubset(object);
			
			}
		})));
		actionCells.add(new ActionHasCell(new DatasetCellTableColumns.DeleteActionCell("Delete",new Delegate<Dataset>() {

			@Override
			public void execute(Dataset object) {
				if (Window.confirm("Do you really want to delete the dataset? (all related GWAS results will also be deleted"))
				{
					getUiHandlers().deleteDataset(object);
				}
			}
		})));
		
		datasetTable.addColumn(new DatasetCellTableColumns.ActionColumn(new CompositeCell<Dataset>(actionCells)),"Action");
	}

	@Override
	public void clearStatistics() {
		statistics_left_container.clear();
		statistics_right_container.clear();
		
	}

	@Override
	public void drawValueStatistics(AbstractDataTable data) {
		int height = 100 + data.getNumberOfRows() * 100;
		value_statistics_chart = new BarChart(data, createOptions("Values",height));
		//value_statistics_chart.addStyleName(style.chart_left());
		statistics_left_container.add(value_statistics_chart);
		
	}

	@Override
	public void drawInfoStatistics(AbstractDataTable data) {
		int height =   100 +data.getNumberOfRows() * 100;
		info_statistics_chart = new BarChart(data, createOptions("Info",height));
		//info_statistics_chart.addStyleName(style.chart_right());
		statistics_right_container.add(info_statistics_chart);
	}
	
	
	
	private Options createOptions(String title,int height) {
		Options options = Options.create();
		options.setTitle(title);
		options.setHeight(height);
		int width = getAvailableHorizontalSpace();
		options.setWidth((width)/2);
		options.setBackgroundColor("#FAFAFA");
		ChartArea area = ChartArea.create();
		area.setLeft("10%");
		area.setTop("8%");
		area.setHeight("92%");
		area.setWidth("70%");
		options.setChartArea(area);
		return options;
	}

	@Override
	public void drawLocationDistribution(Dataset dataset, AbstractDataTable data) {
		if (data.getNumberOfRows() > 0)
			geomap.draw(data,createGeoMapOptions());
	}
	
	private GeoMap.Options createGeoMapOptions() {
		GeoMap.Options options = GeoMap.Options.create();
		options.setHeight(400);
		options.setWidth("100%");
		return options;
	}
	
	private int getAvailableHorizontalSpace()  {
		int width = widget.getOffsetWidth() -38 ;
		if (width <= 0)
			width = Window.getClientWidth() - 160 - 58;
		return width;
	}
	
}
