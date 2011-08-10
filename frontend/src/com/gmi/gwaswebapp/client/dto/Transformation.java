package com.gmi.gwaswebapp.client.dto;

import java.util.Arrays;
import java.util.List;

import name.pehl.piriti.commons.client.Transient;


public class Transformation extends BaseModel {
	
	public static final List<String>TRANSFORMATIONS = Arrays.asList("","log","sqrt");
	
	String name;
	String description;
	String dataset;
	String phenotype;
	@Transient boolean isNewTransformation;
	@Transient String newTransformation;
	
	List<Analysis> analysis_methods;
	
	public Transformation() {
		
	}

	public Transformation(String name,String dataset, String phenotype) {
		this.name = name;
		this.dataset = dataset;
		this.phenotype = phenotype;
	}
	
	@Override
	public String getId() {
		return phenotype+"_"+dataset+"_"+name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhenotype() {
		return phenotype;
	}
	
	public String getDataset() {
		return dataset;
	}

	public List<Analysis> getAnalysisMethods() {
		return analysis_methods;
	}

	public String getDescription() {
		return description;
	}

	public boolean isNewTransformation() {
		return this.isNewTransformation;
	}

	public String getNewTransformation() {
		return newTransformation;
	}
	
	public void setIsNewTransformation(boolean isNewTransformation)
	{
		this.isNewTransformation = isNewTransformation;
	}

	public void setNewTransformation(String value) {
		newTransformation = value;
	}

	public void resetNewTransformation() {
		newTransformation = null;
		isNewTransformation = false;
	}

	public Analysis getAnalysisFromName(String resultName) {
		for (Analysis analysis : analysis_methods) {
			if (analysis.getName().equals(resultName))
				return analysis;
		}
		return null;
	}

	public int getResultCount(Analysis.TYPE type) {
		int count = 0;
		if (type == null) 
			count = analysis_methods.size();
		else {
			if (analysis_methods != null) {
				for (Analysis analysis : analysis_methods) {
					if (analysis.getType() == type) 
						count++;
				}
			}
		}
		return count;
	}

}
