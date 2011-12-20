package com.gmi.gwaswebapp.client.mvp.dataset.list;

import com.gmi.gwaswebapp.client.dto.Dataset;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;

public interface DatasetCellTableColumns {
	public static class NameColumn extends Column<Dataset,String> {

		public NameColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Dataset object) {
			return object.getName();
		}
	}
	
	public static class DescriptionColumn extends Column<Dataset,String> {

		public DescriptionColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Dataset object) {
			return object.getDescription();
		}
	}
	
	public static class AccessionCountColumn extends Column<Dataset,Number> {

		public AccessionCountColumn() {
			super(new NumberCell());
		}

		@Override
		public Number getValue(Dataset object) {
			return object.getAccessionCount();
		}
	}
	
	
	public static class ActionHasCell implements HasCell<Dataset,Dataset> {
		
		private ActionCell<Dataset> cell;

		public ActionHasCell(ActionCell<Dataset> cell) {
			this.cell = cell;
		}
		
		@Override
		public Cell<Dataset> getCell() {
			return cell;
		}

		@Override
		public FieldUpdater<Dataset, Dataset> getFieldUpdater() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Dataset getValue(Dataset object) {
			return object;
		}
	}
	
	public static class ActionColumn extends Column<Dataset,Dataset> {

		public ActionColumn(Cell<Dataset> cell) {
			super(cell);
			
		}

		@Override
		public Dataset getValue(Dataset object) {
			return object;
		}
	}
	
	public static class DeleteActionCell extends ActionCell<Dataset> {

		public DeleteActionCell(String text,
				com.google.gwt.cell.client.ActionCell.Delegate<Dataset> delegate) {
			super(text, delegate);
		}
		
		@Override
		public void render(Context context, Dataset value, SafeHtmlBuilder sb) {
			if (!value.getName().equals("Fullset"))
				super.render(context, value, sb);
		}
	}
	
	public static class NewActionCell extends ActionCell<Dataset> {

		public NewActionCell(String text,
				com.google.gwt.cell.client.ActionCell.Delegate<Dataset> delegate) {
			super(text, delegate);
		}
		
	}
	
	
	
}
