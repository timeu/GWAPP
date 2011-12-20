package com.gmi.gwaswebapp.client.mvp.home;

import com.gmi.gwaswebapp.client.mvp.accession.AccessionView.Binder;
import com.gmi.gwaswebapp.client.mvp.home.HomePresenter.MyView;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.gmi.gwaswebapp.client.resources.MyResources.MainStyle;
import com.gmi.gwaswebapp.client.ui.ProgressBar.MyStyle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class HomeView extends ViewImpl implements MyView {


	public interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
	}
	
	public final Widget widget;
	@UiField(provided=true) MyResources mainRes;

	@Inject
	public HomeView(final HomeViewUiBinder binder,final MyResources resources) {
		this.mainRes = resources;
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
