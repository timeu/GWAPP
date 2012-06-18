package com.gmi.gwaswebapp.client.mvp.result.details;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LIElement;
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
	@UiField Anchor local_ld_link;
	@UiField Anchor global_ld_link;
	@UiField Anchor local_exact_ld_link;
	@UiField LIElement run_step_wise_link_item;
	protected Integer chromosome;
	protected Integer position;

	public SNPPopup() {
		super();
		setWidget(uiBinder.createAndBindUi(this));
		setAnimationEnabled(true);
		setAutoHideEnabled(true);
		setStylePrimaryName(style.popup());
	}
	
	public void setDataPoint(Integer chromosome,Integer position,boolean showStepWise) {
		this.chromosome = chromosome;
		this.position = position;
		run_step_wise_link_item.setAttribute("style","display:"+(showStepWise ? "list-item" : "none")+";");
	}
	
	public void setPosition(int x, int y) {
		setPopupPosition(x,y);
	}
	
	public HasClickHandlers getRunStepWiseLink() {
		return run_step_wise_link;
	}
	
	public HasClickHandlers getLocalLDLink() {
		return local_ld_link;
	}
	

	public HasClickHandlers getGlobalLDLink() {
		return global_ld_link;
	}
	
	public HasClickHandlers getLocalExactLDLink() {
		return local_exact_ld_link;
	}

}
