package com.otsi.ndap.a4nlpdb.ndapuserservice.utils;

/*
* @author Samba Siva
*/

public class GoogleTokenUtils {

	private String error;
	
	private String error_description;
	
	private String user_id;
	
	private String expires_in;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		return "GoogleTokenUtils [error=" + error + ", error_description=" + error_description + ", user_id=" + user_id
				+ ", expires_in=" + expires_in + "]";
	}
	
	
}
