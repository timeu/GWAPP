package com.gmi.gwaswebapp.client.mvp.result.list;

import java.util.List;

import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.Cofactor;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

public interface ResultsCellTableColumns {
	public static class NameColumn extends Column<Analysis,String>
	{

		public NameColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Analysis object) {
			return object.getName();
		}
	}
	
	public static class CommentColumn extends Column<Analysis,String> {

		public CommentColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Analysis object) {
			return object.getComment();
		}
	}
	
	public static class TypeColumn extends TextColumn<Analysis>
	{

		@Override
		public String getValue(Analysis object) {
			return object.getType().toString();
		}
		
	}
	
	public static class SNPColumn extends Column<Analysis,List<Cofactor>>
	{

		public SNPColumn() {
			super(new AbstractCell<List<Cofactor>>() {

				@Override
				public void render(
						com.google.gwt.cell.client.Cell.Context context,
						List<Cofactor> value, SafeHtmlBuilder sb) {
					sb.appendHtmlConstant("<ul>");
					for (int i=0;i<value.size();i++)
					{
						Cofactor snp =  value.get(i);
						if (snp.getStep() > 0)
							sb.appendHtmlConstant("<li>" +snp.getChr()+" | " + snp.getPos());
					}
					sb.appendHtmlConstant("</ul>");
					
				}
			});
		}

		@Override
		public List<Cofactor> getValue(Analysis object) {
			return object.getCofactors();
		}
		
	}
	
	public static class ActionHasCell implements HasCell<Analysis,Analysis>
	{
		private final ActionCell<Analysis> cell;
	
		public ActionHasCell(ActionCell<Analysis> cell)
		{
			this.cell = cell;
		}

		@Override
		public Cell<Analysis> getCell() {
			return cell;
		}

		@Override
		public FieldUpdater<Analysis, Analysis> getFieldUpdater() {
			return null;
		}

		@Override
		public Analysis getValue(Analysis object) {
			return object;
		}
		
		
	}
	
	public static  class ActionColumn extends Column<Analysis,Analysis>
	{

		public ActionColumn(Cell<Analysis> cell) {
			super(cell);
		}

		@Override
		public Analysis getValue(Analysis object) {
			return object;
		}
		
	}

}
