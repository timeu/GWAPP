package com.gmi.gwaswebapp.client.dto;

import com.google.gwt.view.client.ProvidesKey;

public class BaseModelKeyProvider implements ProvidesKey<BaseModel> {

	@Override
	public Object getKey(BaseModel item) {
		if (item != null && item.getId() != null) {
			return item.getId();
		}
		return null;
	}
}
