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
	
	public DeleteTransformationEvent(String phenotype) {
		this.phenotype  = phenotype;
	}

	public static Type<DeleteTransformationEventHandler> getType() {
	    return TYPE;
	}
	
	
	public String getPhenotype() {
		return phenotype;
	}
	
	public static void fire(HasHandlers eventBus,String phenotype) {
	    eventBus.fireEvent(new DeleteTransformationEvent(phenotype));
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
