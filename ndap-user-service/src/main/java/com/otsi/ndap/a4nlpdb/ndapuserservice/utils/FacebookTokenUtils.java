package com.otsi.ndap.a4nlpdb.ndapuserservice.utils;

/*
* @author Samba Siva
*/

public class FacebookTokenUtils {
	
	private String errorMessage;
	
	private String message;
	
	private boolean success;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "FacebookTokenUtils [errorMessage=" + errorMessage + ", message=" + message + ", success=" + success
				+ "]";
	}

}
