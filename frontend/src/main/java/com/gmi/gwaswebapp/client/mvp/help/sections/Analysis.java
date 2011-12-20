package com.gmi.gwaswebapp.client.mvp.help.sections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class Analysis extends UIObject {

	private static AnalysisUiBinder uiBinder = GWT
			.create(AnalysisUiBinder.class);

	interface AnalysisUiBinder extends UiBinder<Element, Analysis> {
	}

	public Analysis() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
