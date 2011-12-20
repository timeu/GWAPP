package com.gmi.gwaswebapp.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class DeleteDatasetEvent  extends GwtEvent<DeleteDatasetEvent.DeleteDatasetEventHandler>{

	public interface DeleteDatasetEventHandler extends EventHandler{
		public void onDeleteDataset(DeleteDatasetEvent event); 
	}
	
	private static final Type<DeleteDatasetEventHandler> TYPE = new Type<DeleteDatasetEventHandler>();
	private final String phenotype;
	
	public DeleteDatasetEvent(String phenotype) {
		this.phenotype  = phenotype;
	}

	public static Type<DeleteDatasetEventHandler> getType() {
	    return TYPE;
	}
	
	
	public String getPhenotype() {
		return phenotype;
	}
	
	public static void fire(HasHandlers eventBus,String phenotype) {
	    eventBus.fireEvent(new DeleteTransformationEvent(phenotype));
	}
	
	@Override
	public Type<DeleteDatasetEventHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(DeleteDatasetEventHandler handler) {
		handler.onDeleteDataset(this);
	}
}
