package com.otsi.ndap.a4nlpdb.ndapuserservice.dao;

/*
* @author Samba Siva
*/

public class JwtToken {

	public String jwtToken;

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	@Override
	public String toString() {
		return "JwtTokenVo [jwtToken=" + jwtToken + "]";
	}
}
