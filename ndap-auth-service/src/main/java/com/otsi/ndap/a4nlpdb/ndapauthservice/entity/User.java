package com.otsi.ndap.a4nlpdb.ndapauthservice.entity;

import java.io.Serializable;

import lombok.Data;

/*
* @author Samba Siva
*/

@Data
public class User implements Serializable {

	private static final long serialVersionUID = -2769663888481477234L;
	
	private String userId;
	
	private String email;
	
	private String source;
	
	private String userName;
	
	private String userRole;
	
	private String password;

	private String displayName;
	
	private String pictureUrl;
	
	private boolean subscription;

	public User(String userId, String email, String source, String userName, String userRole, String password,
			String displayName, String pictureUrl,boolean subscription) {
		super();
		this.userId = userId;
		this.email = email;
		this.source = source;
		this.userName = userName;
		this.userRole = userRole;
		this.password = password;
		this.displayName = displayName;
		this.pictureUrl = pictureUrl;
		this.subscription = subscription;
	}
	
}
