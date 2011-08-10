package com.gmi.gwaswebapp.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.gmi.gwaswebapp.client.events.ProgressBarEvent.ProgressBarHandler;

public class ProgressBarEvent extends GwtEvent<ProgressBarHandler> {
	
	private static final Type<ProgressBarHandler> TYPE = new Type<ProgressBarHandler>();
	private String url;
	private boolean isStop = false;
	
	public interface ProgressBarHandler extends EventHandler {
		  void onShowProgressBar(ProgressBarEvent event);
	}
	
	public static Type<ProgressBarHandler> getType() {
		return TYPE;
	}

	 public static void fire(HasHandlers source,String url) {
		 fire(source,url,false);
	 }
	 public static void fire(HasHandlers source,String url,boolean isStop) {
		 source.fireEvent(new ProgressBarEvent(url, isStop));
	 }
	 
	 public ProgressBarEvent(String url,boolean isStop) {
		 this.url = url;
		 this.isStop = isStop;
	 }
	 public ProgressBarEvent(String url) {
		 this.url = url;
	 }
	 
	

	  @Override
	  protected void dispatch(ProgressBarHandler handler) {
	    handler.onShowProgressBar(this);
	  }

	  @Override
	  public Type<ProgressBarHandler> getAssociatedType() {
	    return getType();
	  }
	  
	  public String getUrl()
	  {
		  return this.url;
	  }


	public boolean IsStop() {
		return isStop;
	}
}

