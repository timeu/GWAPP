package com.gmi.gwaswebapp.client.command.impl;

import com.gmi.gwaswebapp.client.dispatch.AbstractRequestBuilderCacheClientActionHandler;
import com.gmi.gwaswebapp.client.command.PreviewTransformationAction;
import com.gmi.gwaswebapp.client.command.PreviewTransformationActionResult;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;


public class PreviewTransformationActionHandler extends AbstractRequestBuilderCacheClientActionHandler<PreviewTransformationAction, PreviewTransformationActionResult> {

	
	@Inject
	protected PreviewTransformationActionHandler(Cache cache,EventBus eventBus) {
		super(PreviewTransformationAction.class, cache,eventBus,false,true,false);
	}

	
}