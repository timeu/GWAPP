package com.gmi.gwaswebapp.client.mvp.phenotype.list;

import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.gwtplatform.mvp.client.UiHandlers;

public interface PhenotypeListUIHandlers extends UiHandlers{

	void deletePhenotype(Phenotype phenotype);

}
