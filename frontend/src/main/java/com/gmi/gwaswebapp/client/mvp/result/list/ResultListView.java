package com.gmi.gwaswebapp.client.mvp.result.list;

import java.util.ArrayList;
import java.util.List;

import at.gmi.nordborglab.widgets.geneviewer.client.datasource.impl.JBrowseDataSourceImpl;

import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.BaseModel;
import com.gmi.gwaswebapp.client.mvp.result.list.ResultListPresenter.MyView;
import com.gmi.gwaswebapp.client.resources.CellTableResources;
import com.gmi.gwaswebapp.client.ui.QQPlotPopup;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ResultListView extends ViewWithUiHandlers<ResultListUiHandlers> implements MyView{

	private static ResultListViewUiBinder uiBinder = GWT
			.create(ResultListViewUiBinder.class);

	interface ResultListViewUiBinder extends UiBinder<Widget, ResultListView> {
	}
	
	protected JBrowseDataSourceImpl geneDataSource = new JBrowseDataSourceImpl("/gwas/");
	private final Widget widget;
	private final CellTableResources cellTableResources;
	private PopupPanel popup = new PopupPanel(true);
	private QQPlotPopup qqPlotPopup = new QQPlotPopup();
	@UiField(provided = true) CellTable<Analysis> resultsTable;

	@Inject
	public ResultListView(final CellTableResources cellTableResources) {
		this.cellTableResources = cellTableResources;
		resultsTable = new CellTable<Analysis>(15,cellTableResources);
		initCellTable();
		widget = uiBinder.createAndBindUi(this);
		popup.setSize("1100px","500px");
		popup.add(qqPlotPopup);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasData<Analysis> getDisplay() {
		return resultsTable;
	}

	@Override
	public void setSelectionModel(SingleSelectionModel<BaseModel> selectionModel) {
		resultsTable.setSelectionModel(selectionModel);
	}
	
	private void initCellTable() {
		resultsTable.addColumn(new ResultsCellTableColumns.NameColumn(),"Name");
		resultsTable.addColumn(new ResultsCellTableColumns.TypeColumn(),"Type");
		resultsTable.addColumn(new ResultsCellTableColumns.SNPColumn(),"SNPs");
		resultsTable.addColumn(new ResultsCellTableColumns.CommentColumn(),"Comments");
		
		List<HasCell<Analysis,?>> actionCells = new ArrayList<HasCell<Analysis,?>>();
		actionCells.add(new ResultsCellTableColumns.ActionHasCell(new ActionCell<Analysis>("QQ-Plot",new ActionCell.Delegate<Analysis>() {
			@Override
			public void execute(Analysis object) {
				qqPlotPopup.setData(object);
				popup.show();
				popup.center();
			}
		})));
		
		actionCells.add(new ResultsCellTableColumns.ActionHasCell(new ActionCell<Analysis>("Delete",new ActionCell.Delegate<Analysis>() {
			@Override
			public void execute(Analysis object) {
				if (Window.confirm("Do you really want to delete the GWAS-result?"))
				{
					getUiHandlers().deleteAnalysis(object);
				}
			}
		})));
		resultsTable.addColumn(new ResultsCellTableColumns.ActionColumn(new CompositeCell<Analysis>(actionCells)),"Action");
	}

}
