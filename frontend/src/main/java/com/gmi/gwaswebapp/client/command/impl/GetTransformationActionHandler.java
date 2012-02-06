package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.gmi.gwaswebapp.client.command.GetTransformationAction;
import com.gmi.gwaswebapp.client.command.GetTransformationActionResult;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;


public class GetTransformationActionHandler extends AbstractRequestBuilderCacheClientActionHandler<GetTransformationAction, GetTransformationActionResult> {

	
	@Inject
	protected GetTransformationActionHandler(Cache cache,EventBus eventBus) {
		super(GetTransformationAction.class, cache,eventBus,false,true,false);
	}

	
}