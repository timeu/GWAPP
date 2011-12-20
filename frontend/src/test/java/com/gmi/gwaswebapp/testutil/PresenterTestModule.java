package com.gmi.gwaswebapp.testutil;

import org.jukito.JukitoModule;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.testing.CountingEventBus;



public abstract class PresenterTestModule extends JukitoModule{

	@Override
	protected void configureTest() {
		GWTMockUtilities.disarm();
	    bind(EventBus.class).to(CountingEventBus.class);
	    
	    configurePresenterTest();
	}
	
	abstract protected void configurePresenterTest();

}
