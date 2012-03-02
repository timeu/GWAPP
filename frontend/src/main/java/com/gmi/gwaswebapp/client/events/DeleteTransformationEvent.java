package com.gmi.gwaswebapp.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;


public class DeleteTransformationEvent extends GwtEvent<DeleteTransformationEvent.DeleteTransformationEventHandler>{

	public interface DeleteTransformationEventHandler extends EventHandler{
		public void onDeleteTransformation(DeleteTransformationEvent event); 
	}
	
	private static final Type<DeleteTransformationEventHandler> TYPE = new Type<DeleteTransformationEventHandler>();
	private final String phenotype;
	private final String dataset;
	
	public DeleteTransformationEvent(String phenotype,String dataset) {
		this.phenotype  = phenotype;
		this.dataset = dataset;
	}

	public static Type<DeleteTransformationEventHandler> getType() {
	    return TYPE;
	}
	
	
	public String getPhenotype() {
		return phenotype;
	}
	
	public String getDataset() {
		return dataset;
	}
	
	public static void fire(HasHandlers eventBus,String phenotype,String dataset) {
	    eventBus.fireEvent(new DeleteTransformationEvent(phenotype,dataset));
	}
	
	@Override
	public Type<DeleteTransformationEventHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(DeleteTransformationEventHandler handler) {
		handler.onDeleteTransformation(this);
		
	}

}
