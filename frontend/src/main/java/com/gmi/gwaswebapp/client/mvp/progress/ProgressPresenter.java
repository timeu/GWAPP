package com.gmi.gwaswebapp.client.mvp.progress;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gmi.gwaswebapp.client.command.GetProgressAction;
import com.gmi.gwaswebapp.client.command.GetProgressActionResult;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.gmi.gwaswebapp.client.dto.ProgressResult;
import com.gmi.gwaswebapp.client.dto.Readers.ProgressResultReader;

public class ProgressPresenter extends
		PresenterWidget<ProgressPresenter.MyView> {

	public interface MyView extends View {
		void setVisible(boolean visible);
		void setProgress(Integer progress,String remainingSeconds,String currentTask);
		void showBrowserNotification(String contentUrl);
	}
	
	class ProgressCallBack extends GWASCallback<GetProgressActionResult> {

		@Override
		public void onFailure(Throwable caught) {
			stop();
			super.onFailure(caught);
		}

		public ProgressCallBack(EventBus eventBus) {
			super(eventBus);
		}

		@Override
		public void onSuccess(GetProgressActionResult result) {
			ProgressResult progressResult = result.getResult(); 
			if (progressResult.getStatus() == ProgressResult.STATUS.OK) {
				getView().setProgress(progressResult.getProgress(), progressResult.getRemainingTime(), progressResult.getCurrentTask());
				if (progressResult.getProgress() >= 100)
					stop();
			}
			else
				stop();
		} 
		
	}
	
	private final DispatchAsync dispatch;
	private final ProgressResultReader reader; 
	private int duration = 10000;
	private Timer progressTimer = null;

	@Inject
	public ProgressPresenter(final EventBus eventBus, final MyView view,final DispatchAsync disptach,final ProgressResultReader reader) {
		super(eventBus, view);
		this.dispatch = disptach;
		this.reader = reader;
	}
	
	

	public void start(String url) {
		
		getView().setVisible(true);
		getView().showBrowserNotification("/gwas/getProgressBarHTML");
		final GetProgressAction action = new GetProgressAction(url, reader);
		final ProgressCallBack callback = new ProgressCallBack(getEventBus());
		progressTimer = new Timer() {
			
			@Override
			public void run() {
				dispatch.execute(action, callback);
			}
		};
		progressTimer.scheduleRepeating(duration);
	}
	
	public void stop() {
		if (progressTimer != null)
			progressTimer.cancel();
		Timer hideTimer = new Timer() {

			@Override
			public void run() {
				getView().setProgress(0, null, "");
				getView().setVisible(false);
				
			}
		};
		hideTimer.schedule(3000);
	}
}
