package com.otsi.ndap.a4nlpdb.ndapuserservice.repositories;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.User;

/*
* @author Chandrakanth
*/

public interface UserRepository {

	boolean existsByUsername(String username);
	
	Optional<User> finedByUserId(String userId);

	User findByUsername(String username);

	void deleteByUsername(String username);

	boolean save(User user);
	
	CopyOnWriteArrayList<User> findAll();

	User update(User user);

	User findByUsernameOrUserId(String usernameorid);
	
	//id for social user and username or email for local user
	User findByUsernameOrEmailOrId(String usernameoremailorId);

}
