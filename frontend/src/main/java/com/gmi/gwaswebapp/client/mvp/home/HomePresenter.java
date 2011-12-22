package com.gmi.gwaswebapp.client.mvp.home;


import com.gmi.gwaswebapp.client.CurrentUser;
import com.gmi.gwaswebapp.client.NameTokens;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class HomePresenter extends Presenter<HomePresenter.MyView,HomePresenter.MyProxy> {
	
	@ProxyStandard
	@NameToken(NameTokens.homePage)
	public interface MyProxy extends ProxyPlace<HomePresenter> {
	}
	
	public interface MyView extends View {
		
		HasText getDatasetKey();
		HasClickHandlers getSubmitButton();
		void initBrowserNotificationButton();
	}
	
	private final CurrentUser currentUser;
	private final PlaceManager placeManager;
	
	@Inject
	  public HomePresenter(final EventBus eventBus, final MyView view,
	      final MyProxy proxy, final CurrentUser currentUser, final PlaceManager placeManager) {
	    super(eventBus, view, proxy);
	    this.currentUser = currentUser; 
	    this.placeManager = placeManager;
	  }

	 @Override
	  protected void revealInParent() {
	    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, 
	        this);
	  }
	 
	 @Override
	 public void onBind() {
		 registerHandler(getView().getSubmitButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				currentUser.updateCookie(getView().getDatasetKey().getText(), new Runnable() {
					
					@Override
					public void run() {
						placeManager.revealCurrentPlace();
					}
				});
				
			}
		}));
	 }
	 
	 @Override
	 public void onReset() {
		 getView().getDatasetKey().setText(currentUser.getUserData().getUserID());
		 getView().initBrowserNotificationButton();
	 }
}
