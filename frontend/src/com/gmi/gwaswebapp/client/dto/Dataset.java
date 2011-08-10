package com.gmi.gwaswebapp.client.dto;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmi.gwaswebapp.client.CurrentUser;
import com.gmi.gwaswebapp.client.util.AbstractDtoPredicate;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;



public class Dataset extends BaseModel {
	
	String id = "_NEW_SUBSET_";
	String name;
	String description;
	String phenotype;
	List<Transformation> transformations;
	List<Integer> accession_ids;
	transient List<Accession> accessions;
	transient private boolean isDirty = false;
	transient private boolean isAdd = false;
	
	public Dataset(String name,String description, String phenotype,List<Integer> accession_ids, List<Transformation> transformations,boolean isAdd) {
		this.name = name;
		this.description = description;
		this.phenotype = phenotype;
		this.accession_ids = accession_ids;
		this.transformations = transformations;
	}
	
	public Dataset() {}
	
	public static Dataset newSubset(Dataset dataset) {
		Transformation trans = new Transformation("raw",dataset.getInternId(),dataset.getPhenotype());
		List<Transformation> transformations = new ArrayList<Transformation>();
		transformations.add(trans);
		return new Dataset("_NEW_SUBSET_","",dataset.phenotype,new ArrayList<Integer>(dataset.getAccessionIds()),transformations,true);
	}

	@Override
	public String getId() {
		return phenotype+"_"+id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPhenotype() {
		return phenotype;
	}
	
	public List<Transformation> getTransformations() {
		if (transformations == null)
			transformations = new ArrayList<Transformation>();
		return transformations;
	}
	
	public List<Integer> getAccessionIds() {
		return accession_ids;
	}
	
	public List<Accession> getAccessions() {
		if (accessions == null)
			initAccessions(CurrentUser.accessions);
		return accessions;
	}

	public void initAccessions(List<Accession> fullset) {
		assert fullset != null;
		accessions = new ArrayList<Accession>();
		for (int i = 0;i<accession_ids.size();i++) {
			int index = Collections.binarySearch(fullset, accession_ids.get(i));
			if (index >= 0) 
				accessions.add(fullset.get(index));
			else {
				String test = "test";
			}
		}
		
	}
	
	public Transformation getTransformationFromName(String name) {
		if (getTransformations() != null) {
			for (Transformation transformation : getTransformations()) 
			{
				if (transformation.getName().equals(name))
					return transformation;
			}
		}
		return null;
	}
	
	public int getResultCount(Analysis.TYPE type) {
		int count = 0;
		for (Transformation transformation : transformations) 
		{
			count+= transformation.getResultCount(type);
		}
		return count;
	}
	
	public int getResultCount() {
		return getResultCount(null);
	}


	public Integer getAccessionCount() {
		return accession_ids.size();
	}
	
	public static DataTable calculateLocDistribution(Collection<Accession> accessions) {
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "Country");
		dataTable.addColumn(ColumnType.NUMBER, "Count");
		HashMap<String, Integer> aggr_countries = new HashMap<String,Integer>();
		for (Accession accession : accessions) {
			if (aggr_countries.containsKey(accession.getCountry())) {
				aggr_countries.put(accession.getCountry(),aggr_countries.get(accession.getCountry())+1);
			}
			else
			{
				aggr_countries.put(accession.getCountry(), 1);
			}
		}
		for (Map.Entry<String, Integer> entry : aggr_countries.entrySet()) {
		    int rowIndex = dataTable.addRow();
		    dataTable.setValue(rowIndex, 0,entry.getKey());
		    dataTable.setValue(rowIndex, 1, entry.getValue());
		}
		return dataTable;
	}
	
	public DataTable calculateLocDistribution() {
		return calculateLocDistribution(getAccessions());
	}
	
	public boolean isDirty() {
		return isAdd || isDirty;
	}
	
	public boolean isAdd() {
		return isAdd;
	}
	
	public <S> List<Accession> filterAccessions(List<AbstractDtoPredicate<Accession,S>> predicates) {
		return Accession.filter(getAccessions(), predicates);
	}

	public void updateAccessions(Set<Accession> selectedSet) {
		List<Integer> accession_id_set = new ArrayList<Integer>();
		for (Accession accession: selectedSet) {
			accession_id_set.add(accession.accession_id);
		}
		accession_ids = accession_id_set;
		accessions = null;
	}

	public String getInternId() {
		return id;
	}

	public void setInternId(String id) {
		this.id = id;
		
	}
	
}
