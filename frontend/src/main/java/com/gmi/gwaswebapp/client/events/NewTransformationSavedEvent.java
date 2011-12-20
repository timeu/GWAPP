package com.gmi.gwaswebapp.client.events;


import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class NewTransformationSavedEvent extends GwtEvent<NewTransformationSavedEvent.NewTransformationSavedHandler> {

	  public interface NewTransformationSavedHandler extends EventHandler
	  {
		  void onNewTransformationSaved(NewTransformationSavedEvent event);
	  }
	  
	  private static final Type<NewTransformationSavedHandler> TYPE = new Type<NewTransformationSavedHandler>();

	  public static Type<NewTransformationSavedHandler> getType() {
	    return TYPE;
	  }

	  public static void fire(HasHandlers eventBus,String phenotype,String dataset,String transformation) {
	    eventBus.fireEvent(new NewTransformationSavedEvent(phenotype,dataset,transformation));
	  }

	  private final String dataset;
	  private final String transformation;
	  private final String phenotype;

	  public NewTransformationSavedEvent(String phenotype,String dataset,String transformation) {
		  this.dataset = dataset;
		  this.transformation = transformation;
		  this.phenotype = phenotype;
	  }

	  public String getTransformation() {
	    return transformation;
	  }
	  
	  public String getPhenotype() {
		  return phenotype;
	  }

	  @Override
	  protected void dispatch(NewTransformationSavedHandler handler) {
		  handler.onNewTransformationSaved(this);
	  }

	  @Override
	  public Type<NewTransformationSavedHandler> getAssociatedType() {
		  return getType();
	  }

	  public String getDataset() {
		  return dataset;
	  }
	}