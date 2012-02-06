package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.command.BaseStatusResult;
import com.gmi.gwaswebapp.client.command.SaveDatasetAction;
import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class SaveDatasetActionHandler extends AbstractRequestBuilderCacheClientActionHandler<SaveDatasetAction, BaseStatusResult> {

	
	@Inject
	protected SaveDatasetActionHandler(Cache cache,EventBus eventBus) {
		super(SaveDatasetAction.class,cache,eventBus,false,false,true);
	}


}
