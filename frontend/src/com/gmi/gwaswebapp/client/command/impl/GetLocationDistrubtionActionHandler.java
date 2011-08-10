package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.command.GetLocationDistributionAction;
import com.gmi.gwaswebapp.client.command.GetLocationDistributionActionResult;
import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class GetLocationDistrubtionActionHandler extends AbstractRequestBuilderCacheClientActionHandler<GetLocationDistributionAction, GetLocationDistributionActionResult> {

	
	@Inject
	protected GetLocationDistrubtionActionHandler(Cache cache,EventBus eventBus) {
		super(GetLocationDistributionAction.class, cache,eventBus,false,true,false);
	}
}
