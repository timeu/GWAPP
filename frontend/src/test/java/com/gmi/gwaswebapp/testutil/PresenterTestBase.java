package com.gmi.gwaswebapp.testutil;

import org.junit.AfterClass;

import com.google.gwt.junit.GWTMockUtilities;

public class PresenterTestBase {

	@AfterClass
	public static void tearDown() {
		GWTMockUtilities.restore();
	}
}
