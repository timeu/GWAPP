package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.command.GetAccessionsAction;
import com.gmi.gwaswebapp.client.command.GetAccessionsActionResult;
import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class GetAccessionsActionHandler extends AbstractRequestBuilderCacheClientActionHandler<GetAccessionsAction, GetAccessionsActionResult> {

	
	@Inject
	protected GetAccessionsActionHandler(Cache cache,EventBus eventBus) {
		super(GetAccessionsAction.class, cache,eventBus,true,true,false);
	}
}
