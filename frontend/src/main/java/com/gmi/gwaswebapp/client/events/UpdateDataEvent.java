package com.gmi.gwaswebapp.client.events;


import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateDataEvent extends GwtEvent<UpdateDataEvent.UpdateDataHandler> {

	  public interface UpdateDataHandler extends EventHandler
	  {
		  void onUpdateData(UpdateDataEvent event);
	  }
	  
	  private static final Type<UpdateDataHandler> TYPE = new Type<UpdateDataHandler>();

	  public static Type<UpdateDataHandler> getType() {
	    return TYPE;
	  }

	  public static void fire(HasHandlers eventBus) {
	    eventBus.fireEvent(new UpdateDataEvent());
	  }

	  public UpdateDataEvent() {
	   
	  }

		  @Override
	  protected void dispatch(UpdateDataHandler handler) {
	    handler.onUpdateData(this);
	  }

	  @Override
	  public Type<UpdateDataHandler> getAssociatedType() {
	    return getType();
	  }
	}