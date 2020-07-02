package com.otsi.ndap.a4nlpdb.ndapuserservice.utils;

import javax.servlet.http.HttpServletRequest;

public class JWTTokenUtil {

	private static final String secret = "JwtSecretKey";

	public static String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		System.out.println("bearerToken:" + bearerToken);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
