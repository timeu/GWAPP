package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.command.CheckGWASAction;
import com.gmi.gwaswebapp.client.command.CheckGWASActionResult;
import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class CheckGWASActionHandler extends AbstractRequestBuilderCacheClientActionHandler<CheckGWASAction, CheckGWASActionResult> {

	
	@Inject
	protected CheckGWASActionHandler(Cache cache,EventBus eventBus) {
		super(CheckGWASAction.class, cache,eventBus,true,false,false);
	}
}
