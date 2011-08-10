package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.gmi.gwaswebapp.client.command.GetAssociationDataAction;
import com.gmi.gwaswebapp.client.command.GetAssociationDataActionResult;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;




public class GetAssociationDataActionHandler extends AbstractRequestBuilderCacheClientActionHandler<GetAssociationDataAction, GetAssociationDataActionResult> {

	
	@Inject
	protected GetAssociationDataActionHandler(Cache cache,EventBus eventBus) {
		super(GetAssociationDataAction.class, cache,eventBus,true,true,false);
	}

}

