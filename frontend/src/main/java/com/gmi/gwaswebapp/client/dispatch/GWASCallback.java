package com.gmi.gwaswebapp.client.dispatch;

import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public abstract class GWASCallback<T> implements AsyncCallback<T>,HasHandlers{
	
	private final EventBus eventBus;


	@Inject
	public GWASCallback(EventBus eventBus) {
		this.eventBus = eventBus;
	}


	@Override
	public void onFailure(Throwable caught) {
		DisplayNotificationEvent.fireError(this, "Backend-Error", caught.getMessage());
	}
	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}
	
}
