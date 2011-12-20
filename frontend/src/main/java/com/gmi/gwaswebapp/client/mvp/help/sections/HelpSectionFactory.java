package com.gmi.gwaswebapp.client.mvp.help.sections;

import java.util.HashMap;

import com.google.gwt.user.client.ui.UIObject;

public class HelpSectionFactory {
	
	private static HashMap<String,UIObject> helpsections = null; 
	
	public HelpSectionFactory() {
		if (helpsections == null) {
			helpsections = new HashMap<String,UIObject> ();
			helpsections.put("introduction", new Introduction());
			helpsections.put("phenotypes", new Phenotypes());
			helpsections.put("accessions", new Accessions());
			helpsections.put("analysis", new Analysis());
			helpsections.put("tutorial", new Tutorial());
		}
	}
	
	public UIObject get(String key) {
		return helpsections.get(key);
	}

}
