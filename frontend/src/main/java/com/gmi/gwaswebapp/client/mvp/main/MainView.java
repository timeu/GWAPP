package com.gmi.gwaswebapp.client.mvp.main;

import com.gmi.gwaswebapp.client.NameTokens;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter.MyView;
import com.gmi.gwaswebapp.client.mvp.progress.ProgressPresenter;
import com.gmi.gwaswebapp.client.ui.LoadingIndicator;
import com.gmi.gwaswebapp.client.ui.NotificationPopup;
import com.gmi.gwaswebapp.client.ui.ProgressBar;
import com.gmi.gwaswebapp.client.ui.SlidingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class MainView extends ViewImpl  implements MyView{

	private static MainViewUiBinder uiBinder = GWT
			.create(MainViewUiBinder.class);

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {
	}
	
	interface MyStyle extends CssResource {
	    String current_page_item();
	}

	public final Widget widget;
	protected final NotificationPopup notificationPopup = new NotificationPopup();
	
	@UiField InlineHyperlink homeLink;
	@UiField InlineHyperlink helpLink;
	@UiField InlineHyperlink analysisLink;
	@UiField InlineHyperlink phenotypeLink;
	@UiField InlineHyperlink accessionLink;
	@UiField SlidingPanel contentContainer;
	@UiField HTMLPanel progressBarContainer;
	@UiField LoadingIndicator loadingIndicator;
	
	@UiField MyStyle style;

	@Inject
	public MainView() {
		widget = uiBinder.createAndBindUi(this);
		loadingIndicator.setVisible(false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setActiveNavigationItem(String nameToken) {
		String currentPageItemStyleName = style.current_page_item();
		homeLink.removeStyleName(currentPageItemStyleName);
		analysisLink.removeStyleName(currentPageItemStyleName);
		helpLink.removeStyleName(currentPageItemStyleName);
		phenotypeLink.removeStyleName(currentPageItemStyleName);
		accessionLink.removeStyleName(style.current_page_item());
		InlineHyperlink currentLink = null;
		if (nameToken.equals(homeLink.getTargetHistoryToken())) 
			currentLink = homeLink;
		else if (nameToken.equals(analysisLink.getTargetHistoryToken()))
			currentLink = analysisLink;
		else if (nameToken.equals(phenotypeLink.getTargetHistoryToken()))
			currentLink = phenotypeLink;
		else if (nameToken.equals(helpLink.getTargetHistoryToken()))
			currentLink = helpLink;
		else if (nameToken.equals(accessionLink.getTargetHistoryToken())) 
			currentLink = accessionLink;
		if (currentLink != null)
			currentLink.addStyleName(currentPageItemStyleName);
	}
	
	@Override
	public void setInSlot(Object slot,Widget content) {
		if (slot == MainPagePresenter.TYPE_SetMainContent) {
			setMainContent(content);
		}
		else if (slot == MainPagePresenter.TYPE_SetProgressContent)
		{
			setProgressContent(content);
		}
		else {
			super.setInSlot(slot,content);
		}
	}
	
	protected void setProgressContent(Widget content) {
		if (content != null) {
			progressBarContainer.add(content);
		}
		else
			progressBarContainer.clear();
	}
	
	protected void setMainContent(Widget content) 
	{
		
	    if (content != null) {
	    	contentContainer.setWidget(content);
	    }
	    //progressBar.setProgress(1);
	}

	@Override
	public void showNotification(String caption, String message, int level,
			int duration) {
		notificationPopup.setNotificatonContent(caption,message,level);
		notificationPopup.show();
		notificationPopup.center();
	}

	@Override
	public void showLoadingIndicator(boolean visible) {
		loadingIndicator.setVisible(visible);
	}

}
