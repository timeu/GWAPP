package com.gmi.gwaswebapp.client.dto;




public class BackendResult {
	
	 public enum STATUS {OK,ERROR,WARNING}
	    
     STATUS status;
     String statustext;
	   
     public BackendResult() {}
     
     public BackendResult(STATUS status, String statustext) {
		this.status = status;
		this.statustext = statustext;
	}

	public STATUS getStatus() {
    	 return status;
     }
     
     public String getStatustext() {
    	 return statustext;
     }
}
