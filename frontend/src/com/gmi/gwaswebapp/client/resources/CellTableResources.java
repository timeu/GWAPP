package com.gmi.gwaswebapp.client.resources;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.CellTable.Style;

public interface CellTableResources extends Resources {
	
	@Source({CellTable.Style.DEFAULT_CSS,CellTableStyle.STYLE})
	CellTableStyle cellTableStyle();

	
	public interface CellTableStyle extends Style {
		String STYLE = "CellTable.css";
	}
}
