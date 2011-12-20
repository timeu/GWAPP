package com.gmi.gwaswebapp.client.mvp.home;

import com.gmi.gwaswebapp.client.mvp.home.HomePresenter.MyView;
import com.gmi.gwaswebapp.client.resources.MyResources;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class HomeView extends ViewImpl implements MyView {


	public interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
	}
	
	public final Widget widget;
	
	@UiField(provided=true) final MyResources mainRes;
	@UiField TextBox dataset_key;
	@UiField Button dataset_key_submit;
	

	@Inject
	public HomeView(final HomeViewUiBinder binder,final MyResources resources) {
		
		this.mainRes = resources;
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasText getDatasetKey() {
		return dataset_key;
	}

	@Override
	public HasClickHandlers getSubmitButton() {
		return dataset_key_submit;
	}
	
	
}
