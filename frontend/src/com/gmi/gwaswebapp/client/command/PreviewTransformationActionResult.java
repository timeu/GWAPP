package com.gmi.gwaswebapp.client.command;

import com.google.gwt.visualization.client.DataTable;
import com.gwtplatform.dispatch.shared.Result;

public class PreviewTransformationActionResult implements Result {
	public final DataTable transformationTable;

	public PreviewTransformationActionResult(final DataTable transformationTable) {
		super();
		this.transformationTable = transformationTable;
	}
} 