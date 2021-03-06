package com.gmi.gwaswebapp.client.mvp.progress;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.storage.client.Storage;
import com.gmi.gwaswebapp.client.command.GetProgressActionResult;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.ui.ProgressBar;
import com.gmi.gwaswebapp.client.util.notifications.Notification;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ProgressView extends ViewImpl implements ProgressPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ProgressView> {
	}
	
	private Notification notification;
	@UiField ProgressBar progressBar;
	
	private Storage storage = null;

	@Inject
	public ProgressView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		progressBar.setVisible(false);
   	    storage = Storage.getLocalStorageIfSupported();
	}
	

	@Override
	public Widget asWidget() {
		return widget;
	}


	@Override
	public void setVisible(boolean visible) {
		progressBar.setVisible(visible);
		if (storage != null) {
			storage.removeItem("progress");
			storage.removeItem("task");
		}
	}


	@Override
	public void setProgress(Integer progress,String remainingSeconds,String currentTask) {
		progressBar.setProgress(progress,currentTask);
		if (storage != null) {
			storage.setItem("progress", progress.toString());
			storage.setItem("task",currentTask);
		}
	}


	@Override
	public void showBrowserNotification(String contentUrl) {
		//createHTMLNotification depricated from Chrome
		/*
		if (Notification.isSupported() && Notification.isNotificationAllowed()) {
			notification = Notification.createIfSupported(contentUrl);
			notification.show();
		}*/
	}
}
