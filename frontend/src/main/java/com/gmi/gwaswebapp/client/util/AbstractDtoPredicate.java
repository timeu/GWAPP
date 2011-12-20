package com.gmi.gwaswebapp.client.util;

public abstract class AbstractDtoPredicate<T,S> implements Predicate<T> {
	
	public enum TYPE {AND,OR} 
	protected S value;
	protected TYPE type;
	
	
	public AbstractDtoPredicate(S value) {
		this(value,TYPE.AND);
	}
	
	public AbstractDtoPredicate(S value, TYPE type) {
		this.value = value;
		this.type = type;
	}
	
	public void setValue(S value) {
		this.value = value;
	}
	
	public S getvalue() {
		return value;
	}
	
	public void setType(TYPE type) {
		this.type = type;
	}
	
	public TYPE getType() {
		return type;
	}
}
