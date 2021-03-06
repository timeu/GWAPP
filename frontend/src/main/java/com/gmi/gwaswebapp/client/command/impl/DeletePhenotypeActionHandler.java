package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.DeletePhenotypeAction;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;



public class DeletePhenotypeActionHandler extends AbstractRequestBuilderCacheClientActionHandler<DeletePhenotypeAction, BaseStatusResult> {

	
	@Inject
	protected DeletePhenotypeActionHandler(Cache cache,EventBus eventBus) {
		super(DeletePhenotypeAction.class,cache, eventBus,false,false,true);
	}
}