package com.otsi.ndap.a4nlpdb.ndapuserservice.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserIdGenerator {

	private final static Logger log = LoggerFactory.getLogger(UserIdGenerator.class);
	
	public static String getUserId() {
		SecureRandom prng;
		String randomNum = null;
		try {
			prng = SecureRandom.getInstance("SHA1PRNG");
			randomNum = Integer.valueOf(prng.nextInt()).toString();
			log.info("UserIdGenerator.getUserId()"+randomNum);
		} catch (NoSuchAlgorithmException e) {
			log.error("UserIdGenerator.getUserId()"+e.getMessage());
			e.printStackTrace();
		}
		return randomNum;
	}
}
