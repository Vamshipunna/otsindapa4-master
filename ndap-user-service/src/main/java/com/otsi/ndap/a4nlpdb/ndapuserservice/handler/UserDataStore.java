package com.otsi.ndap.a4nlpdb.ndapuserservice.handler;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.SocialUserTokens;
import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.SocialUserVerification;
import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.User;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.UserAlreadyException;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.UserNotFoundException;
import com.otsi.ndap.a4nlpdb.ndapuserservice.repositories.UserRepository;
import com.otsi.ndap.a4nlpdb.ndapuserservice.utils.LocalUserRole;
import com.otsi.ndap.a4nlpdb.ndapuserservice.utils.UserIdGenerator;
import com.otsi.ndap.a4nlpdb.ndapuserservice.vo.UserVo;

/*
* @author samba siva
*/

@Service
public class UserDataStore {
	
	private final Logger log = LoggerFactory.getLogger(UserDataStore.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LocalUserRole localUserRole;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	CopyOnWriteArrayList<UserVo> inMemoryUserList = new CopyOnWriteArrayList<UserVo>();

	public boolean inMemoryCustomUserData(UserVo userData) throws UserAlreadyException{
		boolean inMemorySataus = false;
		boolean containsName = userRepository.existsByUsername(userData.getUserName());
		if (!containsName) {
			User user = new User();
			final String token = UUID.randomUUID().toString();
			user.setUserId(token);
			user.setEmail(userData.getEmail());
			user.setSource(userData.getSource());
			user.setUserName(userData.getUserName());
			user.setUserRole(localUserRole.getLocalUserRole(userData.getUserRole()));
			user.setPassword(encoder.encode(userData.getPassword()));
			user.setDisplayName(userData.getDisplayName());
			user.setPictureUrl(userData.getPictureUrl());
			user.setSubscription(userData.isSubscription());
			inMemorySataus = userRepository.save(user);
			log.info("Local user data stored successfully in InMemoryList status:"+inMemorySataus);
			return inMemorySataus;
		}
		log.info("User Already Existed");
		throw new UserAlreadyException("User Already Existed");
	}
	
	public boolean inMemorySocialUserData(SocialUserVerification socialUser) throws UserNotFoundException {
		boolean socialStatus = false;
		boolean containsName = false;
		if(socialUser.getUserId() != null && !socialUser.getUserId().isEmpty()) {
		containsName = userRepository.finedByUserId(socialUser.getUserId()).isPresent();
		if (!containsName) {
			User socialUserObj = new User();
			final String randomId = UUID.randomUUID().toString();
			SocialUserTokens token = new SocialUserTokens();
			socialUserObj.setEmail(socialUser.getEmail());//default is null
			socialUserObj.setSource(socialUser.getSource());
			socialUserObj.setUserName(socialUser.getUserName().replaceAll("\\s", ""));
			socialUserObj.setUserId(socialUser.getUserId());
			socialUserObj.setUserRole(localUserRole.getLocalUserRole(socialUser.getUserRole()));
			socialUserObj.setPassword(encoder.encode(randomId));
			socialUserObj.setDisplayName(socialUser.getDisplayName());
			socialUserObj.setPictureUrl(socialUser.getPictureUrl());
			socialUserObj.setSubscription(socialUser.isSubscription());
			token.setAccessToken(socialUser.getSocialToken());
			socialStatus = userRepository.save(socialUserObj);
			log.info("Social user data stored successfully in InMemoryList status:"+socialStatus);
		}
		} else {
			log.error("UserDataStore.inMemorySocialUserData() Invalid user details.");
			throw new UserNotFoundException("Invali user details");
		}
		return socialStatus;
	}

}
