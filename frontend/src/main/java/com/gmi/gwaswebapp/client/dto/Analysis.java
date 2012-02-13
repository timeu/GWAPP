package com.gmi.gwaswebapp.client.dto;

import java.util.List;




public class Analysis extends BaseModel{

	public static enum TYPE {EMMAX,AMM,KW,LM}
	
	String name;
	String resultName;
	String transformation;
	String dataset;
	String phenotype;
	String comment;
	TYPE type;
	List<Cofactor> cofactors;
	
	
	
	public String getName() {
		return name;
	}

	public TYPE getType() {
		return type;
	}

	@Override
	public String getId() {
		return phenotype+"_"+dataset+"_"+transformation+"_"+name;
	}

	public String getComment() {
		return comment;
	}

	public List<Cofactor> getCofactors() {
		return cofactors;
	}

	public String getPhenotype() {
		return phenotype;
	}
	
	public String getDataset() {
		return dataset;
	}

	public String getTransformation() {
		return transformation;
	}

	public String getResultName() {
		return resultName;
	}
}
