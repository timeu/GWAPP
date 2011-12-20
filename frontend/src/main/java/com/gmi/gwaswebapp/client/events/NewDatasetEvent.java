package com.gmi.gwaswebapp.client.events;

import com.gmi.gwaswebapp.client.dto.Dataset;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class NewDatasetEvent extends GwtEvent<NewDatasetEvent.NewDatasetEventHandler>{

	public interface NewDatasetEventHandler extends EventHandler {
		void onNewDataset(NewDatasetEvent event);
	}
	
	private static final Type<NewDatasetEventHandler> TYPE = new Type<NewDatasetEventHandler>();
	
	private final Dataset dataset;
	
	
	public NewDatasetEvent(final Dataset dataset) {
		this.dataset = dataset;
	}

	public static Type<NewDatasetEventHandler> getType() {
		return TYPE;
	}
	
    public static void fire(HasHandlers eventBus,final Dataset dataset) {
    	eventBus.fireEvent(new NewDatasetEvent(dataset));
	}
	
	@Override
	public Type<NewDatasetEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NewDatasetEventHandler handler) {
		handler.onNewDataset(this);
	}
	
	public Dataset getDataset() {
		return dataset;
	}
}
