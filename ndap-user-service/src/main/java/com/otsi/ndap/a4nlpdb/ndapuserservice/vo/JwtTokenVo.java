package com.otsi.ndap.a4nlpdb.ndapuserservice.vo;

/*
* @author Samba Siva
*/

public class JwtTokenVo {

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
