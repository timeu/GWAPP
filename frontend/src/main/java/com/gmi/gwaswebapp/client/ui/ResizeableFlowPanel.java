package com.gmi.gwaswebapp.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class ResizeableFlowPanel extends FlowPanel implements RequiresResize, ProvidesResize{

	@Override
	public void onResize() {
		for (Widget widget: getChildren()) {
			if (widget instanceof RequiresResize)
				((RequiresResize) widget).onResize();
		}
	}

}

