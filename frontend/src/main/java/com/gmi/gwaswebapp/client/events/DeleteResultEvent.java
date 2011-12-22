package com.gmi.gwaswebapp.client.events;


import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;


public class DeleteResultEvent extends GwtEvent<DeleteResultEvent.DeleteResultEventHandler>{

	public interface DeleteResultEventHandler extends EventHandler{
		public void onDeleteResult(DeleteResultEvent event); 
	}
	
	private static final Type<DeleteResultEventHandler> TYPE = new Type<DeleteResultEventHandler>();
	private final String phenotype;
	private final String dataset;
	private final String transformation;
	
	
	public DeleteResultEvent(String phenotype,String dataset,String transformation) {
		this.phenotype  = phenotype;
		this.transformation = transformation;
		this.dataset = dataset;
	}

	public static Type<DeleteResultEventHandler> getType() {
	    return TYPE;
	}
	
	
	public String getPhenotype() {
		return phenotype;
	}
	
	public String getTransformation() {
		return transformation;
	}
	
	public String getDataset() {
		return dataset;
	}
	
	public static void fire(HasHandlers eventBus,String phenotype,String dataset,String transformation) {
	    eventBus.fireEvent(new DeleteResultEvent(phenotype,dataset,transformation));
	}
	
	@Override
	public Type<DeleteResultEventHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(DeleteResultEventHandler handler) {
		handler.onDeleteResult(this);
		
	}

}

