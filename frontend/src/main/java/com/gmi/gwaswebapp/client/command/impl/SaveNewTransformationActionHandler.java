package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.command.SaveNewTransformationAction;
import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
import com.gwtplatform.dispatch.shared.Result;


public class SaveNewTransformationActionHandler extends AbstractRequestBuilderCacheClientActionHandler<SaveNewTransformationAction, Result> {

	
	@Inject
	protected SaveNewTransformationActionHandler(Cache cache,EventBus eventBus) {
		super(SaveNewTransformationAction.class, cache,eventBus,false,false,false);
	}
}