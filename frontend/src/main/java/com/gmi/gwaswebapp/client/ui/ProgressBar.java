package com.gmi.gwaswebapp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ProgressBar extends Composite {

	private static ProgressBarUiBinder uiBinder = GWT
			.create(ProgressBarUiBinder.class);

	interface ProgressBarUiBinder extends UiBinder<Widget, ProgressBar> {
	}
	
	public interface MyStyle extends CssResource {
		String progress_bar_status_complete();
		String progress_bar_status();
		String progress_bar_container();
		String progress_bar_container_visible();
	}
	
	@UiField HTMLPanel container;
	@UiField HTMLPanel progress_status;
	@UiField SpanElement progress_label;
	@UiField SpanElement progress_task;
	@UiField MyStyle style;
	
	
	public ProgressBar() {
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName(style.progress_bar_container());
		setWidth("300px");
		setHeight("100%");
	}
	
	
	public void setProgress(int progress) {
		setProgress(progress,null,null);
	}
	
	public void setProgress(int progress,String currentTask) {
		setProgress(progress, currentTask, null);
	}
	
	public void setProgress(int progress,String currentTask,String time_remaining) {
		if (progress == 100) {
			progress_task.addClassName(style.progress_bar_status_complete());
			progress_status.setWidth("100%");
			progress_task.setInnerText("Done");
		}
		else
		{
			progress_task.removeClassName(style.progress_bar_status_complete());
			progress_status.setWidth(progress+"%");
		}
		progress_label.setInnerText(progress+"%");
		if (currentTask != null && progress < 100) 
			progress_task.setInnerText(currentTask);
		
	}
	
	
	@Override
	public void setVisible(boolean visible) {
		//super.setVisible(visible);
		if (visible)
			addStyleName(style.progress_bar_container_visible());
		else
			removeStyleName(style.progress_bar_container_visible());
	}
	
}
