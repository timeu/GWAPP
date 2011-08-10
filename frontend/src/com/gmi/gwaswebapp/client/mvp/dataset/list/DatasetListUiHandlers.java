package com.gmi.gwaswebapp.client.mvp.dataset.list;

import com.gmi.gwaswebapp.client.dto.Dataset;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DatasetListUiHandlers extends UiHandlers {
	void deleteDataset(Dataset dataset);

	void createSubset(Dataset object);
}
