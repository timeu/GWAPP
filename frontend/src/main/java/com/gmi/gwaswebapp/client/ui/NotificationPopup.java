package com.gmi.gwaswebapp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class NotificationPopup extends PopupPanel {

	private static NotificationPopupUiBinder uiBinder = GWT
			.create(NotificationPopupUiBinder.class);

	interface NotificationPopupUiBinder extends
			UiBinder<Widget, NotificationPopup> {
	}
	
	interface MyStyle extends CssResource {
		String notification();
		String error();
		String warning();
	}
	
	@UiField MyStyle style;
	@UiField Label caption;
	@UiField HTML text;
	
	public static final int LEVEL_MESSAGE = 0;
	public static final int LEVEL_WARNING = 1;
	public static final int LEVEL_ERROR = 2;
	public static final int LEVEL_SEVERE = 3;

	protected int level;

	public NotificationPopup() {
		super();
		setWidget(uiBinder.createAndBindUi(this));
		//setAnimationEnabled(true);
		setAutoHideEnabled(true);
		setStylePrimaryName(style.notification());
		
	}
	
	
	public void setNotificatonContent(String caption,String text,int level){
		this.caption.setText(caption);
		// TODO: Sanitize text
		this.text.setHTML(text);
		//SimpleHtmlSanitizer.sanitizeHtml(text)
		this.level = level;
		switch (level) {
			case 0:
				break;
			case 1:
				addStyleName(style.warning());
				removeStyleName(style.error());
				//setStyleDependentName(style.warning(),true);
				break;
			case 2:
				addStyleName(style.error());
				removeStyleName(style.warning());
			case 3:
				break;
			
		}
	}
}
