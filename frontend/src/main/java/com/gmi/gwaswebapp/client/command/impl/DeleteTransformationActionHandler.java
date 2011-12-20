package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.DeleteTransformationAction;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
//import com.gwtplatform.mvp.client.EventBus;


public class DeleteTransformationActionHandler extends AbstractRequestBuilderCacheClientActionHandler<DeleteTransformationAction, BaseStatusResult> {

	
	@Inject
	protected DeleteTransformationActionHandler(Cache cache,EventBus eventBus) {
		super(DeleteTransformationAction.class,cache,eventBus,false,false,true);
	}


}