package com.gmi.gwaswebapp.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;


public class RefreshDataEvent extends GwtEvent<RefreshDataEvent.RefreshDataEventHandler> {

	  public interface RefreshDataEventHandler extends EventHandler
	  {
		  void onRefreshData(RefreshDataEvent event);
	  }
	  
	  private static final Type<RefreshDataEventHandler> TYPE = new Type<RefreshDataEventHandler>();

	  public static Type<RefreshDataEventHandler> getType() {
	    return TYPE;
	  }

	  public static void fire(HasHandlers eventBus) {
	    eventBus.fireEvent(new RefreshDataEvent());
	  }

	  public RefreshDataEvent() { }

		  @Override
	  protected void dispatch(RefreshDataEventHandler handler) {
	    handler.onRefreshData(this);
	  }

	  @Override
	  public Type<RefreshDataEventHandler> getAssociatedType() {
	    return getType();
	  }
	}
