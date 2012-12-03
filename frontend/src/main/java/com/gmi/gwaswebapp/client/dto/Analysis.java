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
	Double med_pval;
	Double ks_stat;
	Double ks_pval;
	
	
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
	
	public Double getMedPval() {
		return med_pval;
	}
	public Double getKsStat() {
		return ks_stat;
	}
	public Double getKsPval() {
		return ks_pval;
	}
}
