package com.gmi.gwaswebapp.client.mvp.help.sections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class Introduction extends UIObject {

	private static IntroductionUiBinder uiBinder = GWT
			.create(IntroductionUiBinder.class);

	interface IntroductionUiBinder extends UiBinder<DivElement, Introduction> {
	}

	public Introduction() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
