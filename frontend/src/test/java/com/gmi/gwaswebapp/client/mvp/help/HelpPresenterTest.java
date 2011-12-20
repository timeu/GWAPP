package com.gmi.gwaswebapp.client.mvp.help;

import org.jukito.JukitoRunner;
import org.jukito.TestSingleton;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gmi.gwaswebapp.client.mvp.help.sections.HelpSectionFactory;
import com.gmi.gwaswebapp.testutil.PresenterTestBase;
import com.gmi.gwaswebapp.testutil.PresenterTestModule;
import com.google.gwt.user.client.ui.UIObject;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import static org.mockito.Mockito.*;


@RunWith(JukitoRunner.class)
public class HelpPresenterTest extends PresenterTestBase {
	
	public static class Module extends PresenterTestModule {

		@Override
		protected void configurePresenterTest() {
			bindMock(HelpSectionFactory.class).in(TestSingleton.class);
		}
		
		
	}
	
	@Inject
	HelpPresenter presenter;
	
	@Inject
	HelpPresenter.MyView view;
	@Inject
	PlaceManager placeManager;
	
	@Inject HelpSectionFactory helpSectionFactory;

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void test() {
		
	    PlaceRequest request = new PlaceRequest("").with("section", "introduction").with("subsection","");
	    placeManager.revealPlace(request);
	    when(placeManager.getCurrentPlaceRequest()).thenReturn(request);
	    
	    
	    presenter.onReset();
	    verify(helpSectionFactory).get("introduction");
		verify(view).showSection((UIObject)any(), anyString(),anyString());
		
	}

}
