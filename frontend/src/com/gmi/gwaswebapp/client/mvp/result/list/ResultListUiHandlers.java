package com.gmi.gwaswebapp.client.mvp.result.list;

import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gwtplatform.mvp.client.UiHandlers;

public interface ResultListUiHandlers extends  UiHandlers{

	void deleteAnalysis(Analysis analysis);

}
