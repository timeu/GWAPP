package com.gmi.gwaswebapp.client.gin;

import com.gmi.gwaswebapp.client.mvp.analysis.AnalysisPresenter;
import com.gmi.gwaswebapp.client.mvp.home.HomePresenter;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter;
import com.gmi.gwaswebapp.client.mvp.upload.PhenotypeUploadPresenter;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionPresenter;
import com.gmi.gwaswebapp.client.resources.CellTableResources;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.gmi.gwaswebapp.client.mvp.help.HelpPresenter;
import com.gmi.gwaswebapp.client.mvp.help.sections.HelpSectionFactory;

@GinModules({ClientDispatchModule.class,ClientModule.class})
public interface ClientGinjector extends Ginjector {
	  
	  EventBus getEventBus();
	  AsyncProvider<AnalysisPresenter> getAnalysisPresenter();
	  Provider<MainPagePresenter> getRootPresenter();
	  Provider<HomePresenter> getHomePresenter();
	  AsyncProvider<PhenotypeUploadPresenter> getPhenotypeUploadPresenter();
	  PlaceManager getPlaceManager();
	  //CurrentUser getCurrentUser();
	  AsyncProvider<AccessionPresenter> getAccessionPresenter();
	  MyResources getResource();
	  CellTableResources getCellTableResource();
	  AsyncProvider<HelpPresenter> getHelpPresenter();
	  HelpSectionFactory getHelpSectionFactory();
}
