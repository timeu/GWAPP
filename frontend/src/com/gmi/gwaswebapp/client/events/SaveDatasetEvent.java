package com.gmi.gwaswebapp.client.events;

import com.gmi.gwaswebapp.client.dto.Dataset;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;


public class SaveDatasetEvent extends GwtEvent<SaveDatasetEvent.SaveDatasetEventHandler>{

	public interface SaveDatasetEventHandler extends EventHandler{
		public void onSaveDataset(SaveDatasetEvent event); 
	}
	
	private static final Type<SaveDatasetEventHandler> TYPE = new Type<SaveDatasetEventHandler>();
	private final Dataset dataset;
	
	public SaveDatasetEvent(Dataset dataset) {
		this.dataset  = dataset;
	}

	public static Type<SaveDatasetEventHandler> getType() {
	    return TYPE;
	}
	
	
	public Dataset getDataset() {
		return dataset;
	}
	
	public static void fire(HasHandlers eventBus,Dataset dataset) {
	    eventBus.fireEvent(new SaveDatasetEvent(dataset));
	}
	
	@Override
	public Type<SaveDatasetEventHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(SaveDatasetEventHandler handler) {
		handler.onSaveDataset(this);
		
	}

}

