package com.gmi.gwaswebapp.client.dto;


public class Cofactor extends BaseModel{

	Integer step;
	Integer chr;
	Integer pos;
	double bic;
	double ebic;
	double mbic;
	double max_cof_pval;
	double pseudo_heritability;
	
	
	@Override
	public String getId() {
		return step.toString();
	}
	
	public Integer getChr() {
		return chr;
	}
	
	public Integer getPos() {
		return pos;
	}

	public Integer getStep() {
		return step;
	}

	public double getBic() {
		return bic;
	}

	public double getMbic() {
		return mbic;
	}

	public double getEbic() {
		return ebic;
	}

	public double getMaxCofPval() {
		return max_cof_pval;
	}

	public double getPseudoHeritability() {
		return pseudo_heritability;
	}
	
	
}
