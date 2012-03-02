package com.gmi.gwaswebapp.client.command;

import com.google.gwt.visualization.client.DataTable;
import com.gwtplatform.dispatch.shared.Result;

public class PreviewTransformationActionResult implements Result {
	private final DataTable transformationTable;
	private final Double spPval;

	public PreviewTransformationActionResult(final DataTable transformationTable,final Double spPval) {
		super();
		this.transformationTable = transformationTable;
		this.spPval = spPval;
	}

	public DataTable getTransformationTable() {
		return transformationTable;
	}

	public Double getSpPval() {
		return spPval;
	}
} 