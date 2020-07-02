package com.otsi.ndap.a4nlpdb.ndapuserservice.dao;

import java.io.Serializable;

import lombok.Data;

/*
* @author Samba Siva
*/

@Data
public class SocialUserVerification implements Serializable {


	private static final long serialVersionUID = -1701380562772455556L;
	
	private String userId;
	
	private String email;
	
	private String source;
	
	private String userName;
	
	private int userRole;
	
	private String socialToken;
	
	private String displayName;
	
	private String pictureUrl;
	
	private boolean subscription;

	public SocialUserVerification(String userId, String email, String source, String userName, int userRole,
			String socialToken, String displayName, String pictureUrl,boolean subscription) {
		super();
		this.userId = userId;
		this.email = email;
		this.source = source;
		this.userName = userName;
		this.userRole = userRole;
		this.socialToken = socialToken;
		this.displayName = displayName;
		this.pictureUrl = pictureUrl;
		this.subscription = subscription;
	}
	
}
