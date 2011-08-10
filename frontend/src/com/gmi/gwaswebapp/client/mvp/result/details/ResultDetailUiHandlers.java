package com.gmi.gwaswebapp.client.mvp.result.details;

import com.google.gwt.user.client.Event;
import com.gwtplatform.mvp.client.UiHandlers;

public interface ResultDetailUiHandlers extends UiHandlers{

	void onSelectSNP(int chromosome, int position, int x, int y);

	void loadStatisticChart(String value);

	void runStepWiseGWAS(Integer chromosome, Integer position);

}
