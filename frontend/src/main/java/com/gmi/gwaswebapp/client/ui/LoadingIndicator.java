package com.gmi.gwaswebapp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class LoadingIndicator extends Composite {

	private static LoadingIndicatorUiBinder uiBinder = GWT
			.create(LoadingIndicatorUiBinder.class);

	interface LoadingIndicatorUiBinder extends
			UiBinder<Widget, LoadingIndicator> {
	}

	public LoadingIndicator() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
