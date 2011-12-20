package com.gmi.gwaswebapp.client.mvp.transformation.list;

import java.util.List;

import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;

public interface TransformationCellTableColumns {
	
	public static class NameColumn extends Column<Transformation,String>
	{
		public NameColumn()
		{
			super(new TextCell());
		}
		
		@Override
		public String getValue(Transformation object) {
			return object.getName();
		}
	}
	
	public static class DescriptionColumn extends Column<Transformation,String>
	{
		public DescriptionColumn()
		{
			super(new TextCell());
		}
		
		@Override
		public String getValue(Transformation object) {
			return object.getDescription();
		}
	}
	
	public static class NewTransformationColumn extends Column<Transformation,Transformation>
	{

		public NewTransformationColumn(Cell<Transformation> cell) {
			super(cell);
		}

		@Override
		public Transformation getValue(Transformation object) {
			return object;
		}
	}
	
	public static class HideableSelectionCell extends SelectionCell
	{
		protected boolean isVisible = true; 
		
		public HideableSelectionCell(List<String> options) {
			super(options);
		}
		
		@Override
		public void render(Context ctx,String value, SafeHtmlBuilder sb) {
			if (!isVisible)
				sb.appendHtmlConstant("<span style=\"display:none\">");
			super.render(ctx,value, sb);
			
			if (!isVisible)
				sb.appendHtmlConstant("</span>");
		}

		public void setIsVisible(boolean isVisible)
		{
			this.isVisible = isVisible;
		}
		
		public boolean getIsVisible()
		{
			return this.isVisible;
		}
	}
	
	
	public static class TransformationCompositeCell extends CompositeCell<Transformation>
	{

		public TransformationCompositeCell(List<HasCell<Transformation, ?>> hasCells) {
			super(hasCells);
		}

		@Override
		protected <X> void render(Context ctx,Transformation value, 
				SafeHtmlBuilder sb, HasCell<Transformation, X> hasCell) {
			if (hasCell.getCell() instanceof SelectionCell)
			{
				Cell<X> cell = hasCell.getCell();
				String span = "";
				if (value.isNewTransformation())
					span = "<span>"; 
				else
					span = "<span style=\"display:none\">";
					
				sb.appendHtmlConstant(span);
				cell.render(ctx,hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</span>");
			}
			else
				super.render(ctx,value, sb, hasCell);
			
		}
	}
	
	public static class NewActionCell extends ActionCell<Transformation> 
	{
		public NewActionCell(com.google.gwt.cell.client.ActionCell.Delegate<Transformation> delegate) {
			super("New", delegate);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void render(Context ctx,Transformation value, SafeHtmlBuilder sb) {
			if (!value.isNewTransformation() && value.getName().equals("raw"))
			{
				super.render(ctx,value,sb);
			}
		}
	}
	
	public static class SaveCancelActionCell extends ActionCell<Transformation> 
	{

		public SaveCancelActionCell(String text,com.google.gwt.cell.client.ActionCell.Delegate<Transformation> delegate) {
			super(text, delegate);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void render(Context ctx,Transformation value,  SafeHtmlBuilder sb) {
			if (value.isNewTransformation())
				super.render(ctx,value, sb);
		}
	}
	
	public static class GWASActionCell extends ActionCell<Transformation>
	{
		protected Analysis.TYPE analysis;
		
		public GWASActionCell(Analysis.TYPE analysis,String text,com.google.gwt.cell.client.ActionCell.Delegate<Transformation> delegate) {
			super(text, delegate);
			this.analysis = analysis;
		}

		@Override
		public void render(Context ctx,Transformation value, SafeHtmlBuilder sb) {
			for (Analysis analysis : value.getAnalysisMethods())
			{
				if (analysis.getType() == this.analysis)
					return;
			}
			super.render(ctx,value, sb);
		}
	}
	
	public static interface GWASActionDelegate extends ActionCell.Delegate<Transformation> {
		
		Analysis.TYPE getAnalysis();
	}
	
		
	
	public static class SelectionHasCell implements HasCell<Transformation,String>
	{
		private final SelectionCell cell;
		private final FieldUpdater<Transformation,String> fieldUpdater;
		
		public SelectionHasCell(SelectionCell cell,FieldUpdater<Transformation, String> fieldUpdater)
		{
			this.cell = cell;
			this.fieldUpdater = fieldUpdater;
		}

		@Override
		public Cell<String> getCell() {
			return cell;
		}

		@Override
		public FieldUpdater<Transformation, String> getFieldUpdater() {
			return fieldUpdater;
		}

		@Override
		public String getValue(Transformation object) {
			// TODO Auto-generated method stub
			return object.getNewTransformation();
		}
	}
	
	
	public static class ActionHasCell implements HasCell<Transformation,Transformation>
	{
		private final ActionCell<Transformation> cell;
	
		public ActionHasCell(ActionCell<Transformation> cell)
		{
			this.cell = cell;
		}

		@Override
		public Cell<Transformation> getCell() {
			return cell;
		}

		@Override
		public FieldUpdater<Transformation, Transformation> getFieldUpdater() {
			return null;
		}

		@Override
		public Transformation getValue(Transformation object) {
			return object;
		}
	}
	
}