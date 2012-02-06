package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.GetUserInfoAction;
import com.gmi.gwaswebapp.client.command.RunGWASAction;
import com.gmi.gwaswebapp.client.command.RunGWASActionResult;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;



public class RunGWASActionHandler extends AbstractRequestBuilderCacheClientActionHandler<RunGWASAction, RunGWASActionResult> {

	
	@Inject
	protected RunGWASActionHandler(Cache cache,EventBus eventBus) {
		super(RunGWASAction.class, cache,eventBus,true,false,false);
	}

	@Override
	protected void postfetch(RunGWASAction action, RunGWASActionResult result) {
		super.postfetch(action, result);
		Cache cache = getCache();
		cache.remove(GetUserInfoAction._getUrl(true));
		cache.remove(GetUserInfoAction._getUrl(false));
		//cache.remove(Get._getUrl(action.Phenotype,action.Transformation));
	}

}