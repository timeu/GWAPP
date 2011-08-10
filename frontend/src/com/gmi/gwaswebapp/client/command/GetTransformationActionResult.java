package com.gmi.gwaswebapp.client.command;

import com.google.gwt.visualization.client.DataTable;
import com.gwtplatform.dispatch.shared.Result;

public class GetTransformationActionResult implements Result {
	private final DataTable transformationTable;
	private final DataTable motionchartTable;

	public GetTransformationActionResult(final DataTable transformationTable,final DataTable motionchartTable) {
		super();
		this.transformationTable = transformationTable;
		this.motionchartTable = motionchartTable;
	}
	
	public DataTable getTransformationDataTable() {
		return transformationTable;
	}
	
	public DataTable getMotionchartDataTable() {
		return motionchartTable;
	}
} 
