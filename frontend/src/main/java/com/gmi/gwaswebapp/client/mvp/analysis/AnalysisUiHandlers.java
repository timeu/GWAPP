package com.gmi.gwaswebapp.client.mvp.analysis;

import com.gmi.gwaswebapp.client.dto.Analysis;
import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.gwtplatform.mvp.client.UiHandlers;

public interface AnalysisUiHandlers extends UiHandlers {

	void onLoadPhenotype(Phenotype phenotype);
	void onLoadTransformation(Transformation transformation);
	void onLoadGWAS(Analysis analysis);
	void onLoadDataset(Dataset dataset);
	
}