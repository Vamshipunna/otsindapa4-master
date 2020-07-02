package com.otsi.ndap.a4nlpdb.ndapuserservice.repositories;

import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.User;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.TokenInvalidException;

/*
* @author Samba Siva
*/

public interface UserJwtRepository {

	User existsByJwttoken(String jwtToken) throws TokenInvalidException;

	boolean save(String jwtToken, String userName);

}
