package com.gmi.gwaswebapp.client.mvp.help.sections;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class Accessions extends UIObject {

	private static AccessionsUiBinder uiBinder = GWT
			.create(AccessionsUiBinder.class);

	interface AccessionsUiBinder extends UiBinder<Element, Accessions> {
	}

	public Accessions() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
