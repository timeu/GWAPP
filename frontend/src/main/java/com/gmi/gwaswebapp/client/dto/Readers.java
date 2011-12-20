package com.gmi.gwaswebapp.client.dto;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;


public interface Readers {
	
	public interface ResultReader extends JsonReader<BackendResult> {}
	
	public interface PhenotypeReader extends JsonReader<Phenotype> {}
	
	public interface TransformationReader extends JsonReader<Transformation> {}
	
	public interface TransformationWriter extends JsonWriter<Transformation> {}
	
	public interface AnalysisReader extends JsonReader<Analysis> {}
	
	public interface AnalysisWriter extends JsonWriter<Analysis> {}

	public interface CofactorReader extends JsonReader<Cofactor> {}
	
	public interface UserDataReader extends JsonReader<UserData> {}
	
	public interface BackendResultReader extends JsonReader<BackendResult> {}
	
	public interface GWASResultReader extends JsonReader<GWASResult> {}
	
	public interface ProgressResultReader extends JsonReader<ProgressResult> {}
	
	public interface AccessionReader extends JsonReader<Accession> {}
	
	public interface AccessionsReader extends JsonReader<Accessions> {}
	
	public interface DatasetReader extends JsonReader<Dataset> {}
	
	public interface DatasetWriter extends JsonWriter<Dataset> {}
	
	
}
