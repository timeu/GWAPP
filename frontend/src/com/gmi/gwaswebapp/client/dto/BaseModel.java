package com.gmi.gwaswebapp.client.dto;


public abstract class BaseModel {

	public static BaseModelKeyProvider KEY_PROVIDER = new BaseModelKeyProvider(); 
	
	private String id;
	
	public String getId()
    {
        return id;
    }

    /**
     * Necessary for the Piriti mapping.
     * 
     * @param id
     *            The id to set.
     */
    void setId(String id)
    {
        this.id = id;
    }
}
