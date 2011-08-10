package com.gmi.gwaswebapp.client.dispatch;


import com.gmi.gwaswebapp.client.command.RequestBuilderAction;
import com.gmi.gwaswebapp.client.events.LoadingIndicatorEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchRequest;
import com.gwtplatform.dispatch.client.actionhandler.ExecuteCommand;
import com.gwtplatform.dispatch.client.actionhandler.caching.AbstractCachingClientActionHandler;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
import com.gwtplatform.dispatch.shared.Result;




public abstract class AbstractRequestBuilderCacheClientActionHandler<A extends RequestBuilderAction<R>, R extends Result>  extends AbstractCachingClientActionHandler<A,R> implements HasHandlers{
	private final EventBus eventBus;
	static int requestCounter = 0;
	static boolean isLoadingIndicator = false;
	private boolean fireLoadingIndicatorEvent = false;
	private boolean isCachable = true;
	private boolean isClearCache = false;
	
	@Inject
	public AbstractRequestBuilderCacheClientActionHandler(Class<A> actionType,Cache cache,final EventBus eventBus,boolean fireLoadingIndicatorEvent, boolean isCachable, boolean isClearCache) {
		super(actionType, cache);
		this.eventBus = eventBus;
		this.fireLoadingIndicatorEvent = fireLoadingIndicatorEvent;
		this.isCachable = isCachable;
		this.isClearCache = isClearCache;
	}
	
	@Override
	public DispatchRequest execute(A action, AsyncCallback<R> resultCallback,ExecuteCommand<A, R> executeCommand)
	{
		executeCommand = new RequestBuilderExecuteCommand<A,R>();
		return super.execute(action, resultCallback, executeCommand);
	}
	

	 @Override
	 protected void postfetch(A action, R result) {
	    // Check if null result
		if (fireLoadingIndicatorEvent)
			updateLoadingIndicator();
	    if (isCachable && isClearCache == false)
	    {
			if (result == null) {
		      getCache().remove(action.getUrl());
		    } else {
		      // Cache
		      getCache().put(action.getUrl(), result);
		    }
	    }
	    if (isClearCache)
	    	getCache().clear();
	  }

	  @SuppressWarnings("unchecked")
	  @Override
	  protected R prefetch(A action) {
		R retval = null;
	    try {
	      // Check if Action available in Cache
	    	if (isCachable)
	    	{
	    		Object value = super.getCache().get(action.getUrl());
	    		if (value != null && value instanceof Result) {
	    			retval = (R) value;
	    		} else {
	    			retval = null;
	    		}
	    	}
		 } catch (Exception e) {
		    	retval = null;
		 }
		 if (fireLoadingIndicatorEvent && retval == null)
	    	startLoadingIndicator();
		 return retval;
	  }
	  
	@Override
	public void fireEvent(GwtEvent<?> event) {
		this.eventBus.fireEvent(event);
	}
	
	private void updateLoadingIndicator()
	{
		requestCounter--;
		if (requestCounter == 0 && isLoadingIndicator)
		{
			isLoadingIndicator = false;
			LoadingIndicatorEvent.fire(this, false);
		}
	}
	
	private void startLoadingIndicator() {
		requestCounter++;
		if (isLoadingIndicator == false)
		{
			isLoadingIndicator = true;
			LoadingIndicatorEvent.fire(this,true);
		}
	}
}
