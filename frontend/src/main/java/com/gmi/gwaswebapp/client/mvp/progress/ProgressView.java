package com.gmi.gwaswebapp.client.mvp.progress;

import com.gwtplatform.mvp.client.ViewImpl;
import com.gmi.gwaswebapp.client.command.GetProgressActionResult;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.ui.ProgressBar;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ProgressView extends ViewImpl implements ProgressPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ProgressView> {
	}
	
	@UiField ProgressBar progressBar;

	@Inject
	public ProgressView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		progressBar.setVisible(false);
	}
	

	@Override
	public Widget asWidget() {
		return widget;
	}


	@Override
	public void setVisible(boolean visible) {
		progressBar.setVisible(visible);
		
	}


	@Override
	public void setProgress(int progress,String remainingSeconds,String currentTask) {
		progressBar.setProgress(progress,currentTask);
	}
}
