package com.otsi.ndap.a4nlpdb.ndapuserservice.dao;

import java.io.Serializable;

import com.otsi.ndap.a4nlpdb.ndapuserservice.common.UserRole;

import lombok.Data;

/*
* @author Samba Siva
*/

@Data
public class User implements Serializable {

	private static final long serialVersionUID = 685500839133939441L;
	
	private String userId;

	private String email;
	
	private String source;
	
	private String userName;
	
	private UserRole userRole;
	
	private String password;
	
	private String displayName;
	
	private String pictureUrl;
	
	private boolean subscription;

}
