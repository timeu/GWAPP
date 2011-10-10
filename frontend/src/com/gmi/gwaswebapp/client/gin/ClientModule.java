package com.gmi.gwaswebapp.client.gin;



import com.gmi.gwaswebapp.client.CurrentUser;
import com.gmi.gwaswebapp.client.GWASPlaceManager;
import com.gmi.gwaswebapp.client.NameTokens;
import com.gmi.gwaswebapp.client.dto.Readers.AccessionReader;
import com.gmi.gwaswebapp.client.dto.Readers.AccessionsReader;
import com.gmi.gwaswebapp.client.dto.Readers.AnalysisReader;
import com.gmi.gwaswebapp.client.dto.Readers.AnalysisWriter;
import com.gmi.gwaswebapp.client.dto.Readers.BackendResultReader;
import com.gmi.gwaswebapp.client.dto.Readers.CofactorReader;
import com.gmi.gwaswebapp.client.dto.Readers.DatasetWriter;
import com.gmi.gwaswebapp.client.dto.Readers.GWASResultReader;
import com.gmi.gwaswebapp.client.dto.Readers.PhenotypeReader;
import com.gmi.gwaswebapp.client.dto.Readers.ProgressResultReader;
import com.gmi.gwaswebapp.client.dto.Readers.ResultReader;
import com.gmi.gwaswebapp.client.dto.Readers.DatasetReader;
import com.gmi.gwaswebapp.client.dto.Readers.TransformationReader;
import com.gmi.gwaswebapp.client.dto.Readers.TransformationWriter;
import com.gmi.gwaswebapp.client.dto.Readers.UserDataReader;
import com.gmi.gwaswebapp.client.mvp.analysis.AnalysisPresenter;
import com.gmi.gwaswebapp.client.mvp.analysis.AnalysisView;
import com.gmi.gwaswebapp.client.mvp.home.HomePresenter;
import com.gmi.gwaswebapp.client.mvp.home.HomeView;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter;
import com.gmi.gwaswebapp.client.mvp.main.MainView;
import com.gmi.gwaswebapp.client.mvp.phenotype.details.PhenotypeDetailPresenter;
import com.gmi.gwaswebapp.client.mvp.phenotype.details.PhenotypeDetailView;
import com.gmi.gwaswebapp.client.mvp.phenotype.list.PhenotypeListPresenter;
import com.gmi.gwaswebapp.client.mvp.phenotype.list.PhenotypeListView;
import com.gmi.gwaswebapp.client.mvp.result.details.ResultDetailPresenter;
import com.gmi.gwaswebapp.client.mvp.result.details.ResultDetailView;
import com.gmi.gwaswebapp.client.mvp.result.list.ResultListPresenter;
import com.gmi.gwaswebapp.client.mvp.result.list.ResultListView;
import com.gmi.gwaswebapp.client.mvp.transformation.details.TransformationDetailPresenter;
import com.gmi.gwaswebapp.client.mvp.transformation.details.TransformationDetailView;
import com.gmi.gwaswebapp.client.mvp.transformation.list.TransformationListPresenter;
import com.gmi.gwaswebapp.client.mvp.transformation.list.TransformationListView;
import com.gmi.gwaswebapp.client.mvp.upload.PhenotypeUploadPresenter;
import com.gmi.gwaswebapp.client.mvp.upload.PhenotypeUploadView;
import com.google.gwt.dom.client.Style;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
import com.gwtplatform.dispatch.client.actionhandler.caching.DefaultCacheImpl;
import com.gwtplatform.mvp.client.annotations.GaAccount;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalyticsNavigationTracker;
import com.gmi.gwaswebapp.client.mvp.progress.ProgressPresenter;
import com.gmi.gwaswebapp.client.mvp.progress.ProgressView;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchCell;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionPresenter;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionView;
import com.gmi.gwaswebapp.client.resources.CellTableResources;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.gmi.gwaswebapp.client.resources.MyResources.MainStyle;
import com.gmi.gwaswebapp.client.mvp.dataset.list.DatasetListPresenter;
import com.gmi.gwaswebapp.client.mvp.dataset.list.DatasetListView;
import com.gmi.gwaswebapp.client.mvp.dataset.detail.DatasetDetailPresenter;
import com.gmi.gwaswebapp.client.mvp.dataset.detail.DatasetDetailView;
import com.gmi.gwaswebapp.client.mvp.help.HelpPresenter;
import com.gmi.gwaswebapp.client.mvp.help.HelpView;
import com.gmi.gwaswebapp.client.mvp.help.sections.HelpSectionFactory;


public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		
		install(new DefaultModule(GWASPlaceManager.class));
		
	    
	    bind(CurrentUser.class).asEagerSingleton();
	    
	    bind(Cache.class).to(DefaultCacheImpl.class).in(Singleton.class);
	    
	    bindConstant().annotatedWith(GaAccount.class).to("UA-26150757-1");
	    bind(GoogleAnalyticsNavigationTracker.class).asEagerSingleton();
	    
	    bindConstant().annotatedWith(UserIDCookie.class).to(NameTokens.useridCookie);
	    
	    bind(MyResources.class).in(Singleton.class);
	    bind(CellTableResources.class).in(Singleton.class);
	    bind(HelpSectionFactory.class).in(Singleton.class);
	    //Readers
	    
	    bind(CofactorReader.class).asEagerSingleton();
	    bind(AnalysisReader.class).asEagerSingleton();
	    bind(TransformationReader.class).asEagerSingleton();
	    bind(ResultReader.class).asEagerSingleton();
	    bind(PhenotypeReader.class).asEagerSingleton();
	    bind(UserDataReader.class).asEagerSingleton();
	    bind(BackendResultReader.class).asEagerSingleton();
	    bind(GWASResultReader.class).asEagerSingleton();
	    bind(ProgressResultReader.class).asEagerSingleton();
	    bind(AccessionReader.class).asEagerSingleton();
	    bind(AccessionsReader.class).asEagerSingleton();
	    bind(DatasetReader.class).asEagerSingleton();
	    bind(DatasetWriter.class).asEagerSingleton();
	    bind(TransformationWriter.class).asEagerSingleton();
	    bind(AnalysisWriter.class).asEagerSingleton();
	    
	   
	    
	    // Constants
	    bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.homePage);
	    //bindConstant().annotatedWith(SecurityCookie.class).to(NameTokens.securityCookie);
	    
	    
	    bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
		        MainView.class, MainPagePresenter.MyProxy.class);
	    
	    bindPresenter(HomePresenter.class,HomePresenter.MyView.class,HomeView.class,HomePresenter.MyProxy.class);
	    bindPresenter(PhenotypeUploadPresenter.class,PhenotypeUploadPresenter.MyView.class,PhenotypeUploadView.class,PhenotypeUploadPresenter.MyProxy.class);
	    bindPresenter(AnalysisPresenter.class,AnalysisPresenter.MyView.class,AnalysisView.class,AnalysisPresenter.MyProxy.class);
	    bindSingletonPresenterWidget(PhenotypeListPresenter.class, PhenotypeListPresenter.MyView.class, PhenotypeListView.class);
	    bindSingletonPresenterWidget(PhenotypeDetailPresenter.class, PhenotypeDetailPresenter.MyView.class, PhenotypeDetailView.class);
	    bindSingletonPresenterWidget(TransformationListPresenter.class, TransformationListPresenter.MyView.class, TransformationListView.class);
	    bindSingletonPresenterWidget(TransformationDetailPresenter.class, TransformationDetailPresenter.MyView.class, TransformationDetailView.class);
	    bindSingletonPresenterWidget(ResultListPresenter.class, ResultListPresenter.MyView.class, ResultListView.class);
	    bindSingletonPresenterWidget(ResultDetailPresenter.class, ResultDetailPresenter.MyView.class, ResultDetailView.class);

		bindPresenterWidget(ProgressPresenter.class,
				ProgressPresenter.MyView.class, ProgressView.class);

		bindPresenter(AccessionPresenter.class,
				AccessionPresenter.MyView.class, AccessionView.class,
				AccessionPresenter.MyProxy.class);

		bindSingletonPresenterWidget(DatasetListPresenter.class,
				DatasetListPresenter.MyView.class, DatasetListView.class);

		bindSingletonPresenterWidget(DatasetDetailPresenter.class,
				DatasetDetailPresenter.MyView.class, DatasetDetailView.class);

		bindPresenter(HelpPresenter.class, HelpPresenter.MyView.class,
				HelpView.class, HelpPresenter.MyProxy.class);
	}
	

}
