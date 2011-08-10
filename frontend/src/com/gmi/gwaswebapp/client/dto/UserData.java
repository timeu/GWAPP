package com.gmi.gwaswebapp.client.dto;

import java.util.List;

public class UserData {

	String userid;
	List<Phenotype> phenotypes;
	Accessions accessions;
	
	public String getUserID() {
		return userid;
	}
	
	public List<Phenotype> getPhenotypes() {
		return phenotypes;
	}
	
	public Phenotype getPhenotypeFromName(String name) {
		for (Phenotype item : phenotypes) {
			if (item.getName().equals(name))
				return item;
		}
		return null;
	}
	
	public void setPhenotypes(List<Phenotype> phenotypes)  {
		this.phenotypes = phenotypes;
	}
	
	public List<Accession> getAccessions() {
		if (accessions == null)
			return null;
		return accessions.getAccessions();
	}
}
