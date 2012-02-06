package com.gmi.gwaswebapp.client.mvp.help;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gmi.gwaswebapp.client.NameTokens;
import com.gmi.gwaswebapp.client.mvp.help.sections.HelpSectionFactory;
import com.gmi.gwaswebapp.client.mvp.main.MainPagePresenter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.ui.UIObject;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;

public class HelpPresenter extends
		Presenter<HelpPresenter.MyView, HelpPresenter.MyProxy> {

	public interface MyView extends View {

		void showSection(UIObject display,String section,String sub_section);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.helpPage)
	public interface MyProxy extends ProxyPlace<HelpPresenter> {
	}
	
	private final PlaceManager placeManager;
	private final HelpSectionFactory helpSectionFactory;
	
	
	@Inject
	public HelpPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final PlaceManager placeManager, final HelpSectionFactory helpSectionFactory) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.helpSectionFactory = helpSectionFactory;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, 
		        this);
	}

	
	@Override
	protected void onReset() {
		super.onReset();
		PlaceRequest place = placeManager.getCurrentPlaceRequest();
		String section = place.getParameter("section", "introduction");
		String sub_section = place.getParameter("subsection", "");
		getView().showSection(helpSectionFactory.get(section),section,sub_section);
	}
}
