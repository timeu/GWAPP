package com.gmi.gwaswebapp.client.mvp.help.sections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class Tutorial extends UIObject {

	private static TutorialUiBinder uiBinder = GWT
			.create(TutorialUiBinder.class);

	interface TutorialUiBinder extends UiBinder<Element, Tutorial> {
	}

	public Tutorial() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
