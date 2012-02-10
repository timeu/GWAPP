package com.gmi.gwaswebapp.client.mvp.transformation.details;

import com.gmi.gwaswebapp.client.mvp.transformation.details.TransformationDetailPresenter.MyView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.MotionChart;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.gwtplatform.mvp.client.ViewImpl;

public class TransformationDetailView extends ViewImpl implements MyView{

	private static TransformationDetailViewUiBinder uiBinder = GWT
			.create(TransformationDetailViewUiBinder.class);

	interface TransformationDetailViewUiBinder extends
			UiBinder<Widget, TransformationDetailView> {
	}
	
	protected Widget widget;
	protected DataTable transformationDataTable;
	protected DataTable motionchartDataTable;
	protected ColumnChart transformation_chart;
	@UiField HTMLPanel container;
	private MotionChart phenotypeExplorer;
	@UiField LayoutPanel motionchart_container;
	@UiField DisclosurePanel disclosure_panel;
	private String transformation;
	private HandlerRegistration openDisclosurePanelHandlerRegistration;
	
	private  class OpenDisclosurePanelHandler implements OpenHandler<DisclosurePanel> {

		@Override
		public void onOpen(OpenEvent<DisclosurePanel> event) {
			disclosure_panel.clear();
			phenotypeExplorer = new MotionChart();
			disclosure_panel.add(phenotypeExplorer);
			phenotypeExplorer.draw(motionchartDataTable,createMotionchartOptions());
		}
		
	}

	public TransformationDetailView() {
		widget = uiBinder.createAndBindUi(this);
		phenotypeExplorer = new MotionChart();
		disclosure_panel.add(phenotypeExplorer);
		openDisclosurePanelHandlerRegistration = disclosure_panel.addOpenHandler(new OpenDisclosurePanelHandler());
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setData(DataTable transformationDataTable,DataTable motionchartDataTable,String transformation) {
		this.transformationDataTable = transformationDataTable;
		this.motionchartDataTable = motionchartDataTable;
		this.transformation = transformation;
		updateView();
	}
	
	protected void updateView() {
		drawColumnChart();
		drawMotionChart();
	}
	
	protected void drawColumnChart() {
		if(transformation_chart == null)
			attachCharts(transformationDataTable,transformation);
		else 
			transformation_chart.draw(transformationDataTable, createColumnchartOptions(transformation));
	}
	
	protected void drawMotionChart() {
		/*if (motionchart_container.isOpen()) {
			openDisclosurePanelHandlerRegistration.removeHandler();
			motionchart_container.setOpen(false);
			phenotypeExplorer.draw(motionchartDataTable,createMotionchartOptions());
			motionchart_container.setOpen(true);
			openDisclosurePanelHandlerRegistration = motionchart_container.addOpenHandler(new OpenDisclosurePanelHandler());
		}*/
		if (disclosure_panel.isOpen()) {
			disclosure_panel.setOpen(false);
			disclosure_panel.setOpen(true);
		}
	}
	
	private MotionChart.Options createMotionchartOptions() {
		MotionChart.Options options = MotionChart.Options.create();
		options.set("state", "%7B%22time%22%3A%22notime%22%2C%22iconType%22%3A%22BUBBLE%22%2C%22xZoomedDataMin%22%3Anull%2C%22yZoomedDataMax%22%3Anull%2C%22xZoomedIn%22%3Afalse%2C%22iconKeySettings%22%3A%5B%5D%2C%22showTrails%22%3Atrue%2C%22xAxisOption%22%3A%222%22%2C%22colorOption%22%3A%224%22%2C%22yAxisOption%22%3A%223%22%2C%22playDuration%22%3A15%2C%22xZoomedDataMax%22%3Anull%2C%22orderedByX%22%3Afalse%2C%22duration%22%3A%7B%22multiplier%22%3A1%2C%22timeUnit%22%3A%22none%22%7D%2C%22xLambda%22%3A1%2C%22orderedByY%22%3Afalse%2C%22sizeOption%22%3A%22_UNISIZE%22%2C%22yZoomedDataMin%22%3Anull%2C%22nonSelectedAlpha%22%3A0.4%2C%22stateVersion%22%3A3%2C%22dimensions%22%3A%7B%22iconDimensions%22%3A%5B%22dim0%22%5D%7D%2C%22yLambda%22%3A1%2C%22yZoomedIn%22%3Afalse%7D%3B");
		
		options.setSize(disclosure_panel.getOffsetWidth()-17, 600);
		return options;
	}
	
	
	private Options createColumnchartOptions(String title)
	{
		Options options = Options.create();
		options.setTitle(title);
		options.setHeight(400);
		options.setWidth(widget.getOffsetWidth()-18);
		options.setBackgroundColor("#FAFAFA");
		AxisOptions vaxis_options = AxisOptions.create();
		vaxis_options.setMinValue(0.0);
		options.setVAxisOptions(vaxis_options);
		ChartArea area = ChartArea.create();
		area.setLeft("10%");
		area.setTop("20%");
		area.setWidth("75%");
		options.setChartArea(area);
		return options;
	}

	@Override
	public void attachCharts() {
		attachCharts(DataTable.create(),"");
	}
	
	private void attachCharts(DataTable dataTable,String transformation) {
		transformation_chart = new ColumnChart(dataTable, createColumnchartOptions(transformation));
		//phenotypeExplorer = new MotionChart();
		container.add(transformation_chart);
		//motionchart_container.add(phenotypeExplorer);
	}

	@Override
	public void detachCharts() {
		container.clear();
		//motionchart_container.clear();
		transformation_chart = null;
		//phenotypeExplorer = null;
	}
}
