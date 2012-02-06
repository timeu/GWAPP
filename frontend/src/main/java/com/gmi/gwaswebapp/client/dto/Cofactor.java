package com.gmi.gwaswebapp.client.dto;


public class Cofactor extends BaseModel{

	Integer step;
	Integer chr;
	Integer pos;
	double bic;
	double ebic;
	double max_cof_pval;
	double pseudo_heritability;
	double perc_var_expl;
	double remain_perc_gen_var;
	double remain_perc_err_var;
	
	
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

	public double getEbic() {
		return ebic;
	}

	public double getMaxCofPval() {
		return max_cof_pval;
	}

	public double getPseudoHeritability() {
		return pseudo_heritability;
	}
	
	public double getPercVarExpl() {
		return perc_var_expl;
	}
	
	public double getRemainingPercGenVar() {
		return remain_perc_gen_var;
	}
	
	public double getRemainingPercErrVar() {
		return remain_perc_err_var;
	}
	
}
