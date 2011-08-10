package com.gmi.gwaswebapp.client.mvp.home;


import com.gmi.gwaswebapp.client.NameTokens;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class HomePresenter extends Presenter<HomePresenter.MyView,HomePresenter.MyProxy> {
	
	@ProxyStandard
	@NameToken(NameTokens.homePage)
	public interface MyProxy extends ProxyPlace<HomePresenter> {
	}
	
	public interface MyView extends View {
	}
	
	
	@Inject
	  public HomePresenter(final EventBus eventBus, final MyView view,
	      final MyProxy proxy) {
	    super(eventBus, view, proxy);
	  }

	 @Override
	  protected void revealInParent() {
	    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, 
	        this);
	  }
}
