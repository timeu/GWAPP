package com.gmi.gwaswebapp.client;

import java.util.Date;
import java.util.List;

import com.gmi.gwaswebapp.client.gin.UserIDCookie;
import com.gmi.gwaswebapp.client.command.GetUserInfoAction;
import com.gmi.gwaswebapp.client.command.GetUserInfoActionResult;
import com.gmi.gwaswebapp.client.dispatch.GWASCallback;
import com.gmi.gwaswebapp.client.dto.Accession;
import com.gmi.gwaswebapp.client.dto.Phenotype;
import com.gmi.gwaswebapp.client.dto.Readers.UserDataReader;
import com.gmi.gwaswebapp.client.dto.UserData;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;

public class CurrentUser {

	protected UserData userdata;
	private final UserDataReader userdataReader;
	protected final DispatchAsync dispatch;
	protected final EventBus eventBus;
	private final String useridCookie; 
	protected final Cache cache;
	private boolean isLoaded = false;
	public static List<Accession> accessions = null;
	
	  
	@Inject
	public CurrentUser(DispatchAsync dispatch, final @UserIDCookie String useridCookie,final UserDataReader userdataReader,final EventBus eventBus, final Cache cache) {
		this.dispatch = dispatch;
		this.useridCookie = useridCookie;
		this.userdataReader = userdataReader;
		this.eventBus = eventBus;
		this.cache = cache;
	}

	public void load(Runnable onLoad) {
  	    fetchUser(onLoad);
	}
	 
	private void fetchUser(final Runnable onLoad) {
		if (!isLoaded) 
		{
			dispatch.execute(new GetUserInfoAction(userdataReader,true), new GWASCallback<GetUserInfoActionResult>(eventBus) {
				@Override
				public void onSuccess(GetUserInfoActionResult result) {
					isLoaded = true;
					storeUserData(result.getUserData());
					if (onLoad != null)
						onLoad.run();
				}
			});
		}
		else {
			if (onLoad != null)
				onLoad.run();
		}
	}
	
	public void refresh(final Runnable callback) {
		cache.clear();
		dispatch.execute(new GetUserInfoAction(userdataReader), new GWASCallback<GetUserInfoActionResult>(eventBus) {
			@Override
			public void onSuccess(GetUserInfoActionResult result) {
				storeUserData(result.getUserData());
				if (callback != null)
					callback.run();
			}
		});
	}
	
	private void storeUserData(UserData userdata)
	{
		Date now = new Date(2035,1,1); 
		this.userdata = userdata;
		if (userdata.getAccessions() != null)
			this.accessions = userdata.getAccessions();
		if (this.userdata.getUserID() != null)
			Cookies.setCookie(useridCookie, this.userdata.getUserID(),now);
 	}
	
	public void updateCookie(String userID,final Runnable callback) {
		if (userID.isEmpty()) {
			int COOKIE_TIMEOUT = 0;
			Date removeExpire = new Date((new Date()).getTime() + COOKIE_TIMEOUT);         //  //remove the cookie, works.
		    Cookies.setCookie(useridCookie,null, removeExpire);
			Cookies.removeCookie(useridCookie);
		}else {
			Date now = new Date(2035,1,1); 
			Cookies.setCookie(useridCookie, userID,now);
		}
		refresh(callback);
	}

	public UserData getUserData() {
		return userdata;
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}

	public void refresh(List<Phenotype> phenotypes) {
		cache.clear();
		this.userdata.setPhenotypes(phenotypes);
	}

}
