package com.gmi.gwaswebapp.client.util;

public abstract class AbstractDtoPredicate<T,S> implements Predicate<T> {
	
	protected S value;
	
	public AbstractDtoPredicate(S value) {
		this.value = value;
	}
	
	public void setValue(S value) {
		this.value = value;
	}
}
