package com.gmi.gwaswebapp.client.mvp.dataset.detail;

import com.gmi.gwaswebapp.client.dto.Accession;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DatasetDetailUiHandlers extends UiHandlers{

	void selectAccessionInTable(Accession accession);

}
