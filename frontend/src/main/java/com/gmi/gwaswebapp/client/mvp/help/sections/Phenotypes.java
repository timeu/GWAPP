package com.gmi.gwaswebapp.client.mvp.help.sections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class Phenotypes extends UIObject {

	private static PhenotypesUiBinder uiBinder = GWT
			.create(PhenotypesUiBinder.class);

	interface PhenotypesUiBinder extends UiBinder<Element, Phenotypes> {
	}

	public Phenotypes() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
