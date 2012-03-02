package com.gmi.gwaswebapp.client.command;

import com.google.gwt.visualization.client.DataTable;
import com.gwtplatform.dispatch.shared.Result;

public class GetTransformationActionResult implements Result {
	private final DataTable transformationTable;
	private final DataTable motionchartTable;
	private final Double spPval;

	public GetTransformationActionResult(final DataTable transformationTable,final DataTable motionchartTable,final Double spPval) {
		super();
		this.transformationTable = transformationTable;
		this.motionchartTable = motionchartTable;
		this.spPval = spPval;
	}
	
	public DataTable getTransformationDataTable() {
		return transformationTable;
	}
	
	public DataTable getMotionchartDataTable() {
		return motionchartTable;
	}
	
	public Double getSpPval() {
		return spPval;
	}
} 
