package com.gmi.gwaswebapp.client.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import name.pehl.piriti.commons.client.Format;

import com.gmi.gwaswebapp.client.util.AbstractDtoPredicate;
import com.gmi.gwaswebapp.client.util.Predicate;
import com.google.gwt.view.client.ProvidesKey;

public class Accession extends BaseModel implements Comparable<Object>{
	
	


	public static ProvidesKey<Accession> KEY_PROVIDER = new ProvidesKey<Accession>() {
		@Override
		public Object getKey(Accession item) {
			if (item != null && item.getId() != null) {
				return item.getId();
			}
			return null;
		}
	};
	
	
	Integer accession_id;
	String name;
	Double latitude;
	Double longitude;
	String country;
	String collector;
	@Format("dd.MM.yyyy") Date collection_date;
	
	
	
	@Override
	public String getId() {
		return accession_id.toString();
	}


	public String getName() {
		return name;
	}


	public Integer getAccessionId() {
		return accession_id;
	}


	public Double getLatitude() {
		return latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}


	public String getCountry() {
		return country;
	}


	public String getCollector() {
		return collector;
	}
	
	public Date getCollectionDate() {
		return collection_date;
	}


	@Override
	public int compareTo(Object o) {
		if (o instanceof Accession) {
			return accession_id.compareTo(((Accession)o).accession_id);
		}
		else if (o instanceof Integer) {
			return accession_id.compareTo((Integer)o);
		}
		else
			throw new IllegalArgumentException ("The object that is compared must be either Integer or of type Accession"); 
	}
	
	public static class AccessionNamePredicate extends AbstractDtoPredicate<Accession,String>{
		
		public AccessionNamePredicate(String value) {
			super(value);
		}

		@Override
		public boolean apply(Accession type) {
			
			if (value.isEmpty() || type.getName().indexOf(value) >= 0 )
				return true;
			else
				return false;
		}
	}
	
	public static class AccessionCountryPredicate extends AbstractDtoPredicate<Accession,String>{
		
		public AccessionCountryPredicate(String value) {
			super(value);
		}

		@Override
		public boolean apply(Accession type) {
			if (value.isEmpty() || type.getCountry().indexOf(value) >= 0 )
				return true;
			else
				return false;
		}
	}
	
	public static class AccessionCollectorPredicate extends AbstractDtoPredicate<Accession,String>{
		
		public AccessionCollectorPredicate(String value) {
			super(value);
		}

		@Override
		public boolean apply(Accession type) {
			if (value.isEmpty() || type.getCollector().indexOf(value) >= 0 )
				return true;
			else
				return false;
		}
	}
	
	public static class AccessionIdPredicate extends AbstractDtoPredicate<Accession, Integer> {
		public AccessionIdPredicate(Integer value) {
			super(value,TYPE.OR);
		}

		@Override
		public boolean apply(Accession type) {
			if (value != null && value.equals(type.getAccessionId())) 
				return true;
			else
				return false;
		}

		public Integer getValue() {
			return value;
		}
	}
	
	public static  List<Accession> filter(List<Accession> accessions, List<AbstractDtoPredicate<Accession, ?>> accessionPredicates) {
		List<Accession> filtered_list = new ArrayList<Accession>();
		for (Accession accession : accessions) {
			boolean isAdd = true;
			for (AbstractDtoPredicate<Accession,?> predicate: accessionPredicates) {
				if (predicate.getType() == AbstractDtoPredicate.TYPE.OR) {
					if (predicate.apply(accession)) {
						isAdd = true;
						break;
					}
				}
				else {
					if (!predicate.apply(accession)) {
						isAdd = false;
					}
				}
			 }
			if (isAdd)
				filtered_list.add(accession);
		}
		return filtered_list;
	}
}
