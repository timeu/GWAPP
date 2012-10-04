package com.gmi.gwaswebapp.client;

import at.gmi.nordborglab.widgets.geneviewer.client.datasource.GenomeStat;

import com.gmi.gwaswebapp.client.gin.DefaultPlace;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.GeoMap;
import com.google.gwt.visualization.client.visualizations.MotionChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

public class GWASPlaceManager extends PlaceManagerImpl {
	

	private final PlaceRequest defaultPlaceRequest;
	private final CurrentUser currentUser;
	
	@Inject
	public GWASPlaceManager(final EventBus eventBus,
			final TokenFormatter tokenFormatter, @DefaultPlace String defaultNameToken,final CurrentUser currentUser) {
		super(eventBus, tokenFormatter);
		this.currentUser = currentUser;
	    this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace(defaultPlaceRequest);
	}

	@Override
	public void revealErrorPlace(String invalidHistoryToken) {
		// TODO Auto-generated method stub
		//super.revealErrorPlace(invalidHistoryToken);
		
	}
	
	@Override
	public void revealCurrentPlace() {
		VisualizationUtils.loadVisualizationApi(new Runnable() {
			
			@Override
			public void run() {
				if (currentUser.isLoaded())
					GWASPlaceManager.super.revealCurrentPlace();
				else {
					currentUser.load(new Runnable() {

						@Override
						public void run() {
							GWASPlaceManager.super.revealCurrentPlace();
						}
					});
				}
			}
		}, CoreChart.PACKAGE, MotionChart.PACKAGE,GeoMap.PACKAGE);	
	}
}
