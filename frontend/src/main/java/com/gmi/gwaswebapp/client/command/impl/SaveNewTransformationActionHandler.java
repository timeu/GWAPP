package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.SaveNewTransformationAction;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
//import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.dispatch.shared.Result;


public class SaveNewTransformationActionHandler extends AbstractRequestBuilderCacheClientActionHandler<SaveNewTransformationAction, Result> {

	
	@Inject
	protected SaveNewTransformationActionHandler(Cache cache,EventBus eventBus) {
		super(SaveNewTransformationAction.class, cache,eventBus,false,false,false);
	}
}