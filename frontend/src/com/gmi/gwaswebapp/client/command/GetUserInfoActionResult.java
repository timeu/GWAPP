package com.gmi.gwaswebapp.client.command;


import com.gmi.gwaswebapp.client.dto.UserData;
import com.gwtplatform.dispatch.shared.Result;

public class GetUserInfoActionResult implements  Result {
	
	private final UserData userData;

	public GetUserInfoActionResult(final UserData userData) {
		super();
		this.userData = userData;
	}

	public UserData getUserData() {
		return userData;
	}
}