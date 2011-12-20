package com.gmi.gwaswebapp.client.dto;


public class ProgressResult extends BackendResult{

	float progress;
	String remaining_time;
	String currentTask;
	
	ProgressResult() {super(); }
	
	public ProgressResult(STATUS error, String statustext) {
		super(error,statustext);
	}

	public int getProgress() {
		return Math.round(progress*100);
	}
	
	public String getRemainingTime() {
		return remaining_time;
	}
	
	public String getCurrentTask() {
		return currentTask;
	}
}
