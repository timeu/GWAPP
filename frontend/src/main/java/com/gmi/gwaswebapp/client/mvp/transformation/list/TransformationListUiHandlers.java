package com.gmi.gwaswebapp.client.mvp.transformation.list;

import com.gmi.gwaswebapp.client.dto.Analysis.TYPE;
import com.gmi.gwaswebapp.client.dto.Transformation;
import com.gwtplatform.mvp.client.UiHandlers;

public interface TransformationListUiHandlers extends UiHandlers{

	void startNewTransformation(Transformation transformation);

	void changeNewTransformation(Transformation transformation);

	void cancelNewTransformation(Transformation transformation);

	void saveNewTransformation(Transformation transformation);

	void deleteTransformation(Transformation transformation);

	void performGWAS(Transformation transformation, TYPE analysis);

}
