package com.otsi.ndap.a4nlpdb.ndapuserservice.dao;

/*
* @author Samba Siva
*/

public class SocialUserTokens {

	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return "SocialUserTokens [accessToken=" + accessToken + "]";
	}
	
}
