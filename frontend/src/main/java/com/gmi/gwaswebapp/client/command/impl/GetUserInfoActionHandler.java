package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.command.GetUserInfoAction;
import com.gmi.gwaswebapp.client.command.GetUserInfoActionResult;
import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class GetUserInfoActionHandler extends AbstractRequestBuilderCacheClientActionHandler<GetUserInfoAction, GetUserInfoActionResult>{

	@Inject
	protected GetUserInfoActionHandler(Cache cache,EventBus eventBus) {
		super(GetUserInfoAction.class, cache,eventBus,false,true,false);
	}
	

}
