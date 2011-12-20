package com.gmi.gwaswebapp.client.dto;

import java.util.List;


public class Phenotype extends BaseModel {
	
	String name;
	Integer num_values;
	Float std_dev;  
	String growth_conditions;
	String phenotype_scoring;
	String method_description;
	String measurement_scale;
	List<Dataset> datasets;

	public String getName() {
		return name;
	}
	

	public List<Dataset> getDatasets() {
		return datasets;
	}


	public Integer getNumValues() {
		return num_values;
	}


	public Float getStdDev() {
		return std_dev;
	}



	public String getGrowthConditions() {
		return growth_conditions;
	}



	public String getPhenotypeScoring() {
		return phenotype_scoring;
	}



	public String getMethodDescription() {
		return method_description;
	}



	public String getMeasurementScale() {
		return measurement_scale;
	}
	
	
	
	public Dataset getDatasetFromId(String id) {
		for (Dataset dataset : getDatasets()) 
		{
			if (id != null && dataset.getInternId().equals(id))
				return dataset;
		}
		return null;
	}

	public int getTransformationCount() {
		int count = 0;
		for (Dataset subset : getDatasets()) 
		{
			count+= subset.getTransformations().size();
		}
		return count;
	}
	
	public int getResultCount() {
		return getResultCount(null);
	}
	
	public int getResultCount(Analysis.TYPE type) {
		int count = 0;
		for (Dataset subset : getDatasets()) 
		{
			count+= subset.getResultCount(type);
		}
		return count;
	}
	
	public Dataset createSubset(Dataset dataset) {
		Dataset subset = Dataset.newSubset(dataset);
		datasets.add(subset);
		return subset;
	}
}
