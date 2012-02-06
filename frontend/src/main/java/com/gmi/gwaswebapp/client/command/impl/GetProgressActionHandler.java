package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.command.GetProgressAction;
import com.gmi.gwaswebapp.client.command.GetProgressActionResult;
import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class GetProgressActionHandler extends AbstractRequestBuilderCacheClientActionHandler<GetProgressAction, GetProgressActionResult> {

	
	@Inject
	protected GetProgressActionHandler(Cache cache,EventBus eventBus) {
		super(GetProgressAction.class, cache,eventBus,false,false,false);
	}

	
}
