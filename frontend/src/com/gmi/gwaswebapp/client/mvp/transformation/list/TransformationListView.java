package com.gmi.gwaswebapp.client.mvp.transformation.list;

import java.util.ArrayList;
import java.util.List;

import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.BaseModel;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.gmi.gwaswebapp.client.mvp.transformation.list.TransformationListPresenter.MyView;
import com.gmi.gwaswebapp.client.resources.CellTableResources;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.ChartArea;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class TransformationListView extends ViewWithUiHandlers<TransformationListUiHandlers> implements MyView {

	private static TransformationListViewUiBinder uiBinder = GWT
			.create(TransformationListViewUiBinder.class);

	interface TransformationListViewUiBinder extends
			UiBinder<Widget, TransformationListView> {
	}
	
	protected Widget widget;
	private final CellTableResources cellTableResources;
	protected SingleSelectionModel<BaseModel> transformationSelectionModel = new SingleSelectionModel<BaseModel>(); 
	@UiField(provided = true) CellTable<Transformation> transformationTable;
	@UiField HTMLPanel transformation_preview_container;
	@UiField HTMLPanel transformation_detail_container;
	@UiField HTMLPanel results_list_container;
	protected ColumnChart transformation_preview_chart;
	

	@Inject
	public TransformationListView(final CellTableResources cellTableResources) {
		this.cellTableResources = cellTableResources;
		transformationTable = new CellTable<Transformation>(15,cellTableResources);
		initCellTable();
		widget = uiBinder.createAndBindUi(this);
	}

	private void initCellTable() {
		transformationTable.addColumn(new TransformationCellTableColumns.NameColumn(),"Name");
		transformationTable.addColumn(new TransformationCellTableColumns.DescriptionColumn(),"Description");
		
		
		List<HasCell<Transformation,?>> compositeCells = new ArrayList<HasCell<Transformation,?>>();
		
		compositeCells.add(new TransformationCellTableColumns.ActionHasCell(new TransformationCellTableColumns.NewActionCell(new ActionCell.Delegate<Transformation>() {
				@Override
				public void execute(Transformation object) {
					getUiHandlers().startNewTransformation(object);
				}
		})));
		
		compositeCells.add(new TransformationCellTableColumns.SelectionHasCell(new SelectionCell(Transformation.TRANSFORMATIONS), new FieldUpdater<Transformation, String>() {

			@Override
			public void update(int index, Transformation object, String value) {
				object.setNewTransformation(value);
				getUiHandlers().changeNewTransformation(object);
			}
		}));
		
		compositeCells.add(new TransformationCellTableColumns.ActionHasCell(new TransformationCellTableColumns.SaveCancelActionCell("Save",new ActionCell.Delegate<Transformation>() {
			@Override
			public void execute(Transformation object) {
				getUiHandlers().saveNewTransformation(object);
				object.resetNewTransformation();
			}
		})));
		
		compositeCells.add(new TransformationCellTableColumns.ActionHasCell(new TransformationCellTableColumns.SaveCancelActionCell("Cancel",new ActionCell.Delegate<Transformation>() {
			@Override
			public void execute(Transformation object) {
				getUiHandlers().cancelNewTransformation(object);
			}
		})));
		TransformationCellTableColumns.TransformationCompositeCell transformationCell = new TransformationCellTableColumns.TransformationCompositeCell(compositeCells);
		transformationTable.addColumn(new TransformationCellTableColumns.NewTransformationColumn(transformationCell),"Transformation");
		/*selectionModelTransformation.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Transformation transformation = selectionModelTransformation.getSelectedObject();
				getUiHandlers().changeSelectedTransformation(transformation);
			}
		});*/
		
		List<HasCell<Transformation,?>> gwasCells = new ArrayList<HasCell<Transformation,?>>();
		
		gwasCells.add(new TransformationCellTableColumns.ActionHasCell(new TransformationCellTableColumns.GWASActionCell(Analysis.TYPE.EMMAX,Analysis.TYPE.EMMAX.toString(),new ActionCell.Delegate<Transformation>() {
			@Override
			public void execute(Transformation object) {
				performGWAS(Analysis.TYPE.EMMAX,object);
			}
		})));
		
		gwasCells.add(new TransformationCellTableColumns.ActionHasCell(new TransformationCellTableColumns.GWASActionCell(Analysis.TYPE.KW,Analysis.TYPE.KW.toString(),new ActionCell.Delegate<Transformation>() {
			@Override
			public void execute(Transformation object) {
				performGWAS(Analysis.TYPE.KW,object);
			}
		})));
	
		gwasCells.add(new TransformationCellTableColumns.ActionHasCell(new TransformationCellTableColumns.GWASActionCell(Analysis.TYPE.LM,Analysis.TYPE.LM.toString(),new ActionCell.Delegate<Transformation>() {
			@Override
			public void execute(Transformation object) {
				performGWAS(Analysis.TYPE.LM,object);
			}
		})));
		transformationTable.addColumn(new TransformationCellTableColumns.NewTransformationColumn(new CompositeCell<Transformation>(gwasCells)),"GWAS");
		
		List<HasCell<Transformation,?>> actionCells = new ArrayList<HasCell<Transformation,?>>();
		
		actionCells.add(new TransformationCellTableColumns.ActionHasCell(new ActionCell<Transformation>("QQ-Plot",new ActionCell.Delegate<Transformation>() {
			@Override
			public void execute(Transformation object) {
				//getUiHandlers().showQQPlot(object);
			}
		})));
		
		actionCells.add(new TransformationCellTableColumns.ActionHasCell(new ActionCell<Transformation>("Delete",new ActionCell.Delegate<Transformation>() {
			@Override
			public void execute(Transformation object) {
				if (Window.confirm("Do you really want to delete the transformation? (all related GWAS results will also be deleted"))
				{
					getUiHandlers().deleteTransformation(object);
				}
			}
		})));
		
		
		transformationTable.addColumn(new TransformationCellTableColumns.NewTransformationColumn(new CompositeCell<Transformation>(actionCells)),"Action");
		
		//transformationTable.setSelectionModel(selectionModelTransformation);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasData<Transformation> getDisplay() {
		return transformationTable;
	}

	@Override
	public void setSelectionModel(SingleSelectionModel<BaseModel> selectionModel) {
		transformationTable.setSelectionModel(selectionModel);
		
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == TransformationListPresenter.TYPE_SetTransformationDetailContent) {
			if (content != null)
				transformation_detail_container.add(content);
			else
				transformation_detail_container.clear();
		}
		else if (slot == TransformationListPresenter.TYPE_SetResultListContent) {
			if (content != null)
				results_list_container.add(content);
			else 
				results_list_container.clear();
		}
		else 
			super.setInSlot(slot, content);
	}

	@Override
	public void showTransformationPreviewHistogram(DataTable data) {
		if (data == null)
			data = createEmptyDataTable();
		drawTransformationPreviewChart(data);
		
	}
	
	protected DataTable createEmptyDataTable()
	{
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING,"Phenotype Value","x_axis");
		dataTable.addColumn(ColumnType.NUMBER,"Frequency","frequency");
		return dataTable;
	}
	
	protected void drawTransformationPreviewChart(DataTable data) {
		if (transformation_preview_chart != null)
			transformation_preview_container.remove(transformation_preview_chart);
		transformation_preview_chart = new ColumnChart(data, createOptions("Preview"));
		transformation_preview_container.add(transformation_preview_chart);
	}
	
	
	
	private Options createOptions(String title)
	{
		Options options = Options.create();
		options.setTitle(title);
		options.setHeight(400);
		options.setWidth(widget.getOffsetWidth()-18);
		options.setBackgroundColor("#FAFAFA");
		ChartArea area = ChartArea.create();
		area.setLeft("10%");
		area.setTop("20%");
		area.setWidth("75%");
		options.setChartArea(area);
		return options;
	}

	@Override
	public void hideTransformationPreviewHistogram() {
		transformation_preview_container.clear();
		transformation_preview_chart = null;
	}
	
	private void performGWAS(Analysis.TYPE analysis,Transformation transformation)
	{
		if (Window.confirm("Do you really want to run a " + analysis.toString() + " analysis on the transformation " + transformation.getName()))
		{
			getUiHandlers().performGWAS(transformation,analysis);
		}
	}
	

}
