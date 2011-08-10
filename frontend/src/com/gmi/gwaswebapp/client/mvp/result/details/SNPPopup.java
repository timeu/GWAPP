package com.gmi.gwaswebapp.client.mvp.result.details;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class SNPPopup extends PopupPanel {

	private static SNPPopupUiBinder uiBinder = GWT
			.create(SNPPopupUiBinder.class);

	interface SNPPopupUiBinder extends UiBinder<Widget, SNPPopup> {
	}
	
	interface MyStyle extends CssResource {
		String popup();
	}
	
	@UiField MyStyle style;
	@UiField Anchor run_step_wise_link;
	protected Integer chromosome;
	protected Integer position;

	public SNPPopup() {
		super();
		setWidget(uiBinder.createAndBindUi(this));
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		setStylePrimaryName(style.popup());
	}
	
	public void setDataPoint(Integer chromosome,Integer position) {
		this.chromosome = chromosome;
		this.position = position;
	}
	
	public void setPosition(int x, int y) {
		setPopupPosition(x,y);
	}
	
	public HasClickHandlers getRunStepWiseLink() {
		return run_step_wise_link;
	}

}
