package com.gmi.gwaswebapp.client.dispatch;

import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public abstract class GWASCallback<T> implements AsyncCallback<T>{
	
	private final EventBus eventBus;


	@Inject
	public GWASCallback(EventBus eventBus) {
		this.eventBus = eventBus;
	}


	@Override
	public void onFailure(Throwable caught) {
		DisplayNotificationEvent.fireError(eventBus, "Backend-Error", caught.getMessage());
	}
	
}
