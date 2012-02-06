package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.DeleteDatasetAction;
import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class DeleteDatasetActionHandler extends AbstractRequestBuilderCacheClientActionHandler<DeleteDatasetAction, BaseStatusResult> {

	
	@Inject
	protected DeleteDatasetActionHandler(Cache cache,EventBus eventBus) {
		super(DeleteDatasetAction.class,cache,eventBus,false,false,true);
	}


}
