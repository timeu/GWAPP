package com.gmi.gwaswebapp.client.mvp.main;

import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent;
import com.gmi.gwaswebapp.client.events.DisplayNotificationEvent.DisplayNotificationHandler;
import com.gmi.gwaswebapp.client.events.LoadingIndicatorEvent;
import com.gmi.gwaswebapp.client.events.LoadingIndicatorEvent.LoadingIndicatorHandler;
import com.gmi.gwaswebapp.client.events.ProgressBarEvent.ProgressBarHandler;
import com.gmi.gwaswebapp.client.events.ProgressBarEvent;
import com.gmi.gwaswebapp.client.mvp.progress.ProgressPresenter;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;

public class MainPagePresenter extends Presenter<MainPagePresenter.MyView,MainPagePresenter.MyProxy> implements DisplayNotificationHandler, LoadingIndicatorHandler,ProgressBarHandler{

	@ProxyStandard
	public interface MyProxy extends Proxy<MainPagePresenter>{}
	
	public interface MyView extends View {
		void setActiveNavigationItem(String nameToken);
		void showNotification(String caption,String message,int level,int duration);
		void showLoadingIndicator(boolean visible);
	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();
	public static final Object TYPE_SetProgressContent = new Object();

	protected final PlaceManager placeManager;
	private final ProgressPresenter progressPresenter;
	
	@Inject
	public MainPagePresenter(EventBus eventBus, MyView view, MyProxy proxy,PlaceManager placeManager,ProgressPresenter progressPresenter) {
		super(eventBus, view, proxy);
		this.progressPresenter = progressPresenter;
		this.placeManager = placeManager;
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		setInSlot(TYPE_SetProgressContent, progressPresenter);
	}
	
	@Override 
	protected void onUnbind() {
		super.onUnbind();
		clearSlot(TYPE_SetProgressContent);
	}

	@Override
	protected void onReset() {
		super.onReset();
		PlaceRequest request = placeManager.getCurrentPlaceRequest();
		getView().setActiveNavigationItem(request.getNameToken());
	}

	@ProxyEvent
	@Override
	public void onDisplayNotifcation(DisplayNotificationEvent event) {
		getView().showNotification(event.getCaption(), event.getMessage(), event.getLevel(), event.getDuration());
	}

	@ProxyEvent
	@Override
	public void onProcessLoadingIndicator(LoadingIndicatorEvent event) {
		getView().showLoadingIndicator(event.getShow());
	}
	
	@ProxyEvent
	@Override
	public void onShowProgressBar(ProgressBarEvent event) {
		if (event.IsStop()) 
			progressPresenter.stop();
		else
			progressPresenter.start(event.getUrl());
	}

}
