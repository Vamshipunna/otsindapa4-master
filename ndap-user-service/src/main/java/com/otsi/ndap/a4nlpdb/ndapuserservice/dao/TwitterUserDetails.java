package com.otsi.ndap.a4nlpdb.ndapuserservice.dao;

import java.io.Serializable;

public class TwitterUserDetails implements Serializable {

	private static final long serialVersionUID = 6482725360106193458L;
	
	private String oauth_consumer_key;
	
	private String oauth_nonce;
	
	private String oauth_signature_method;
	
	private String oauth_token;
	
	private long oauth_timestamp;
	
	private String oauth_version;//1.0
	
	private String oauth_signature;//HMAC-SHA1
	
	public String getOauth_consumer_key() {
		return oauth_consumer_key;
	}

	public void setOauth_consumer_key(String oauth_consumer_key) {
		this.oauth_consumer_key = oauth_consumer_key;
	}

	public String getOauth_nonce() {
		return oauth_nonce;
	}

	public void setOauth_nonce(String oauth_nonce) {
		this.oauth_nonce = oauth_nonce;
	}

	public String getOauth_signature_method() {
		return oauth_signature_method;
	}

	public void setOauth_signature_method(String oauth_signature_method) {
		this.oauth_signature_method = oauth_signature_method;
	}

	public String getOauth_token() {
		return oauth_token;
	}

	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	public long getOauth_timestamp() {
		return oauth_timestamp;
	}

	public void setOauth_timestamp(long oauth_timestamp) {
		this.oauth_timestamp = oauth_timestamp;
	}

	public String getOauth_version() {
		return oauth_version;
	}

	public void setOauth_version(String oauth_version) {
		this.oauth_version = oauth_version;
	}

	public String getOauth_signature() {
		return oauth_signature;
	}

	public void setOauth_signature(String oauth_signature) {
		this.oauth_signature = oauth_signature;
	}

	@Override
	public String toString() {
		return "TwitterUserDetails [oauth_consumer_key=" + oauth_consumer_key + ", oauth_nonce=" + oauth_nonce
				+ ", oauth_signature_method=" + oauth_signature_method + ", oauth_token=" + oauth_token
				+ ", oauth_timestamp=" + oauth_timestamp + ", oauth_version=" + oauth_version + ", oauth_signature="
				+ oauth_signature + "]";
	}


}
