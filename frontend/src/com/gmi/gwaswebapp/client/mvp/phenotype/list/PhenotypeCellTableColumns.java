package com.gmi.gwaswebapp.client.mvp.phenotype.list;

import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;

public interface PhenotypeCellTableColumns {
	
	public static class NameColumn extends Column<Phenotype,String>
	{

		public NameColumn()
		{
			super(new TextCell());
		}
		
		@Override
		public String getValue(Phenotype object) {
			return object.getName();
		}
	}
	
	public static class NumberColumn extends Column<Phenotype,Number>
	{
		public NumberColumn()
		{
			super(new NumberCell());
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getNumValues();
		}
	}
	
	public static class SubsetColumn extends Column<Phenotype,Number> {

		public SubsetColumn() {
			super(new NumberCell());
			
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getDatasets().size();
		}
		
		
	}
	
	public static class StdevColumn extends Column<Phenotype,Number>
	{
		public StdevColumn()
		{
			super(new NumberCell());
		}

		@Override
		public Number getValue(Phenotype object) {
			return object.getStdDev();
		}
	}
	
	public static class GrowthConditionColumn extends Column<Phenotype,String>
	{
		public GrowthConditionColumn()	{
			super(new TextCell());
		}

		@Override
		public String getValue(Phenotype object) {
			return object.getGrowthConditions();
		}
	}
	
	public static class PhenotypScoringColumn extends Column<Phenotype,String>
	{
		public PhenotypScoringColumn()	{
			super(new TextCell());
		}

		@Override
		public String getValue(Phenotype object) {
			return object.getPhenotypeScoring();
		}
	}
	
	public static class MethodDescriptionColumn extends Column<Phenotype,String>
	{
		public MethodDescriptionColumn()	{
			super(new TextCell());
		}

		@Override
		public String getValue(Phenotype object) {
			return object.getMethodDescription();
		}
	}
	
	public static class MeasurementScaleColumn extends Column<Phenotype,String>
	{
		public MeasurementScaleColumn()	{
			super(new TextCell());
		}

		@Override
		public String getValue(Phenotype object) {
			return object.getMeasurementScale();
		}
	}
	
	public static class ActionHasCell implements HasCell<Phenotype,Phenotype>
	{
		private final ActionCell<Phenotype> cell;
	
		public ActionHasCell(ActionCell<Phenotype> cell)
		{
			this.cell = cell;
		}

		@Override
		public Cell<Phenotype> getCell() {
			return cell;
		}

		@Override
		public FieldUpdater<Phenotype, Phenotype> getFieldUpdater() {
			return null;
		}

		@Override
		public Phenotype getValue(Phenotype object) {
			return object;
		}
	}
	
	
}
