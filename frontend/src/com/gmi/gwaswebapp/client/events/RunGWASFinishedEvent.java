package com.gmi.gwaswebapp.client.events;


import java.util.List;

import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class RunGWASFinishedEvent extends GwtEvent<RunGWASFinishedEvent.RunGWASFinishedEventHandler> {

	  public interface RunGWASFinishedEventHandler extends EventHandler
	  {
		  void onGWASFinished(RunGWASFinishedEvent event);
	  }
	  
	  private static final Type<RunGWASFinishedEventHandler> TYPE = new Type<RunGWASFinishedEventHandler>();
	  
	  public final Integer Chromosome;
	  public final Integer Positon;
	  public final List<Phenotype> Phenotypes;
	  public final String Phenotype;
	  public final String Dataset;
	  public final String Transformation;
	  public final String ResultName;

	  public static Type<RunGWASFinishedEventHandler> getType() {
	    return TYPE;
	  }

	  public static void fire(HasHandlers eventBus,Integer chromosome,Integer position,List<Phenotype> phenotypes,String Phenotype,String Dataset,String Transformation,String ResultName) {
	    eventBus.fireEvent(new RunGWASFinishedEvent(chromosome,position,phenotypes,Phenotype,Dataset,Transformation,ResultName));
	  }

	  public RunGWASFinishedEvent(Integer chromosome,Integer position,List<Phenotype> phenotypes,String Phenotype,String dataset,String Transformation,String ResultName) {
		  this.Chromosome = chromosome;
		  this.Positon = position;
		  this.Phenotypes = phenotypes;
		  this.Dataset = dataset;
		  this.Phenotype = Phenotype;
		  this.ResultName = ResultName;
		  this.Transformation = Transformation;
	  }

		  @Override
	  protected void dispatch(RunGWASFinishedEventHandler handler) {
	    handler.onGWASFinished(this);
	  }

	  @Override
	  public Type<RunGWASFinishedEventHandler> getAssociatedType() {
	    return getType();
	  }
	}