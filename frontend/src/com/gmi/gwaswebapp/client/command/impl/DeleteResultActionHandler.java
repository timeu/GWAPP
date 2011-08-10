package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.DeleteResultAction;
import com.gmi.gwaswebapp.client.command.GetAssociationDataAction;
import com.gmi.gwaswebapp.client.command.GetUserInfoAction;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
//import com.gwtplatform.mvp.client.EventBus;


public class DeleteResultActionHandler extends AbstractRequestBuilderCacheClientActionHandler<DeleteResultAction, BaseStatusResult> {

	
	@Inject
	protected DeleteResultActionHandler(Cache cache,EventBus eventBus) {
		super(DeleteResultAction.class, cache,eventBus,false,false,false);
	}

	@Override
	protected void postfetch(DeleteResultAction action,
			BaseStatusResult result) {
			super.postfetch(action, result);
			Cache cache = getCache();
			cache.remove(GetUserInfoAction._getUrl(false));
			cache.remove(GetUserInfoAction._getUrl(true));
			//cache.remove(GetAssociationDataAction._getUrl(action.Phenotype, action.Transformation, action.Analysis,action.ResultName));
	}
}