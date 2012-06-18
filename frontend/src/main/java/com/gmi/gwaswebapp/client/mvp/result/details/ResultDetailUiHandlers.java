package com.gmi.gwaswebapp.client.mvp.result.details;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ResultDetailUiHandlers extends UiHandlers{

	void onSelectSNP(int chromosome, int position, int x, int y);

	void loadStatisticChart(String value);

	void runStepWiseGWAS(Integer chromosome, Integer position);

	void showLocalLD(Integer chromosome, Integer position);

	void runGlobalLD(Integer chromosome, Integer position);

	void calculateLocalExactLD(Integer chromosome, Integer position);

}
