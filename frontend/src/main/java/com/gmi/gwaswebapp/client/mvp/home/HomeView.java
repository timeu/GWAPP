package com.gmi.gwaswebapp.client.mvp.home;

import com.gmi.gwaswebapp.client.mvp.home.HomePresenter.MyView;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.gmi.gwaswebapp.client.util.notifications.Notification;
import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class HomeView extends ViewImpl implements MyView {


	public interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
	}
	
	public final Widget widget;
	
	@UiField(provided=true) final MyResources mainRes;
	@UiField TextBox dataset_key;
	@UiField Button dataset_key_submit;
	@UiField Button request_permission_btn;
	@UiField HTMLPanel notification_blocked_text;

	@Inject
	public HomeView(final HomeViewUiBinder binder,final MyResources resources) {
		
		this.mainRes = resources;
		widget = binder.createAndBindUi(this);
		request_permission_btn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (Notification.isNotificationNotAllowed()) {
					Notification.requestPermission(new Callback<Void,Void>() {
	
						@Override
						public void onFailure(Void reason) {
							
						}
	
						@Override
						public void onSuccess(Void result) {
							initBrowserNotificationButton();
						}
					});
				}
				else
				{
					Window.open("chrome://settings/contentExceptions#notifications","Browser notifications settings","");
				}
			};
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasText getDatasetKey() {
		return dataset_key;
	}

	@Override
	public HasClickHandlers getSubmitButton() {
		return dataset_key_submit;
	}

	@Override
	public void initBrowserNotificationButton() {
		if (Notification.isSupported() && !Notification.isNotificationAllowed()) {
			request_permission_btn.setVisible(true);
			if (Notification.isNotificationDenied()) {
				notification_blocked_text.setVisible(true);
				request_permission_btn.setVisible(false);
			}
			else
			{
				request_permission_btn.setText("Activate browser notifications");
				notification_blocked_text.setVisible(false);
			}
			
		}
		else {
			request_permission_btn.setVisible(false);
		}
	}
	
	
}
