package com.gmi.gwaswebapp.client.gin;


import com.gmi.gwaswebapp.client.command.impl.CheckGWASActionHandler;
import com.gmi.gwaswebapp.client.command.impl.DeleteDatasetActionHandler;
import com.gmi.gwaswebapp.client.command.impl.DeletePhenotypeActionHandler;
import com.gmi.gwaswebapp.client.command.impl.DeleteResultActionHandler;
import com.gmi.gwaswebapp.client.command.impl.DeleteTransformationActionHandler;
import com.gmi.gwaswebapp.client.command.impl.GetAccessionsActionHandler;
import com.gmi.gwaswebapp.client.command.impl.GetAssociationDataActionHandler;
import com.gmi.gwaswebapp.client.command.impl.GetLocationDistrubtionActionHandler;
import com.gmi.gwaswebapp.client.command.impl.GetProgressActionHandler;
import com.gmi.gwaswebapp.client.command.impl.GetTransformationActionHandler;
import com.gmi.gwaswebapp.client.command.impl.GetUserInfoActionHandler;
import com.gmi.gwaswebapp.client.command.impl.PreviewTransformationActionHandler;
import com.gmi.gwaswebapp.client.command.impl.RunGWASActionHandler;
import com.gmi.gwaswebapp.client.command.impl.SaveDatasetActionHandler;
import com.gmi.gwaswebapp.client.command.impl.SaveNewTransformationActionHandler;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.DefaultClientActionHandlerRegistry;

public class GWASClientActionHandlerRegistry extends DefaultClientActionHandlerRegistry{

	@Inject
	public GWASClientActionHandlerRegistry(GetUserInfoActionHandler getUserInfoActionHandler,
			GetTransformationActionHandler getTransformationActionHandler,
			PreviewTransformationActionHandler previewTransformationHandler,
			SaveNewTransformationActionHandler saveNewTransformationActionHandler,
			DeleteTransformationActionHandler deleteTransformationActionHandler,
			DeletePhenotypeActionHandler deletePhenotypeActionHandler,
			RunGWASActionHandler runGWASActionHandler,
			DeleteResultActionHandler deleteResultActionHandler,
			GetAssociationDataActionHandler getAssociationDataActionHandler,
			GetProgressActionHandler getProgressActionHandler,
			GetAccessionsActionHandler getAccessionsActionHandler,
			GetLocationDistrubtionActionHandler getLocationDistributionActionHandler,
			SaveDatasetActionHandler saveDatasetActionHandler,
			DeleteDatasetActionHandler deleteDatasetActionHandler,
			CheckGWASActionHandler checkGWASActionHandler)
	{
		register(getUserInfoActionHandler);
		register(getTransformationActionHandler);
		register(previewTransformationHandler);
		register(saveNewTransformationActionHandler);
		register(deleteTransformationActionHandler);
		register(deletePhenotypeActionHandler);
		register(runGWASActionHandler);
		register(deleteResultActionHandler);
		register(getAssociationDataActionHandler);
		register(getProgressActionHandler);
		register(getAccessionsActionHandler);
		register(getLocationDistributionActionHandler);
		register(saveDatasetActionHandler);
		register(deleteDatasetActionHandler);
		register(checkGWASActionHandler);
	}
}
