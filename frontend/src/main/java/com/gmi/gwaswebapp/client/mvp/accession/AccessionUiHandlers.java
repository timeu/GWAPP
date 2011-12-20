package com.gmi.gwaswebapp.client.mvp.accession;

import java.util.List;

import com.gmi.gwaswebapp.client.dto.Accession;
import com.gmi.gwaswebapp.client.mvp.accession.AccessionCellTableColumns.SearchTerm;
import com.gwtplatform.mvp.client.UiHandlers;


public interface AccessionUiHandlers extends UiHandlers {

	void selectAccessionInTable(Accession accession);


}
