package com.otsi.ndap.a4nlpdb.ndapuserservice.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.JwtToken;
import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.User;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.TokenInvalidException;

/*
* @author Samba Siva
*/

@Repository
public class UserJwtRepositoryImpl implements UserJwtRepository {
	
	@Autowired
	private UserRepository userRepository;
	
	private final Logger log = LoggerFactory.getLogger(UserJwtRepositoryImpl.class);

	private Map<String, String>  localUserJwtTokenList;
	
	@Value("${localuser.jwttoken.location}")
	private String jwtTokenDataPath;
	
	public UserJwtRepositoryImpl() {
		localUserJwtTokenList = new HashMap<>();
	}
	
	@Override
	public User existsByJwttoken(String jwtToken) throws TokenInvalidException {
		
		String username = localUserJwtTokenList.get(jwtToken);
		if(username == null || username.isEmpty()) {
			throw new TokenInvalidException("Invalid token");
		}
		return userRepository.findByUsernameOrUserId(username);
		
	}

	@Override
	public boolean save(String jwtToken, String userName) {
		
		String value = this.localUserJwtTokenList.put(jwtToken, userName);
		
		return (value!=null)? true : false;
	}

	
}
