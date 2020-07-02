package com.otsi.ndap.a4nlpdb.ndapuserservice.vo;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.User;

/*
* @author Samba Siva,Chandrakanth
*/

@Service
public class UserInMemoryData {

	CopyOnWriteArrayList<User> userInMemoryData;

	public CopyOnWriteArrayList<User> getUserInMemoryData() {
		return userInMemoryData;
	}

	public void setUserInMemoryData(CopyOnWriteArrayList<User> userInMemoryData) {
		this.userInMemoryData = userInMemoryData;
	}
	
	
}
