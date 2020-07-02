package com.otsi.ndap.a4nlpdb.ndapuserservice.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.JwtToken;
import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.SocialUserVerification;
import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.User;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.TokenInvalidException;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.UserAlreadyException;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.UserNotFoundException;
import com.otsi.ndap.a4nlpdb.ndapuserservice.feignservice.UserServiceFegin;
import com.otsi.ndap.a4nlpdb.ndapuserservice.handler.CustomUserJwtHandler;
import com.otsi.ndap.a4nlpdb.ndapuserservice.handler.SocailUserValidationHandler;
import com.otsi.ndap.a4nlpdb.ndapuserservice.handler.UserDataStore;
import com.otsi.ndap.a4nlpdb.ndapuserservice.repositories.UserJwtRepository;
import com.otsi.ndap.a4nlpdb.ndapuserservice.repositories.UserRepository;
import com.otsi.ndap.a4nlpdb.ndapuserservice.utils.LocalUserRole;
import com.otsi.ndap.a4nlpdb.ndapuserservice.utils.UserCredentials;
import com.otsi.ndap.a4nlpdb.ndapuserservice.vo.CustomUserLoginDetails;
import com.otsi.ndap.a4nlpdb.ndapuserservice.vo.UpdateUserVo;
import com.otsi.ndap.a4nlpdb.ndapuserservice.vo.UserVo;

import net.minidev.json.JSONObject;

/*
* @author Samba Siva
*/

@Service
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDataStore userDatStorage;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserJwtRepository userJwtRepository;

	@Autowired
	private SocailUserValidationHandler socialUserHandler;

	@Autowired
	private CustomUserJwtHandler customUserJwtHandler;
	
	@Autowired
	private UserServiceFegin userServiceFeign;

	@Value("${internal.jwt.tokenuri}")
	private String jwtTokenUri;

	@Value("${twitter.client-id}")
	private String clientId;

	@Value("${twitter.client-secret}")
	private String clientSecret;

	@Value("${twitter.forwarduri}")
	private String forwarUri;

	@Autowired
	private LocalUserRole localUserRole;

	public ResponseEntity<Map<String, Object>> userRegistraionService(UserVo user, HttpServletResponse response)
			throws UserAlreadyException {
		log.info("UserService.userRegistraionService() beginning..."+user.getUserName());
		Map<String, Object> map = new HashMap<>();
		boolean status = userDatStorage.inMemoryCustomUserData(user);
		if (status) {
			Map<String, Object> generateJwtToken = customUserJwtHandler.generateJwtToken(user, response);
			map.put("content", generateJwtToken);
			HttpHeaders responseHeaders = new HttpHeaders();
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			return ResponseEntity.ok().headers(responseHeaders).body(map);
		} else {
			map.put("userdatastatus", status);
			return ResponseEntity.ok().body(map);
		}
	}

	public ResponseEntity<Map<String, Object>> socialUserService(SocialUserVerification socialUser,
			HttpServletResponse response) throws Exception {
		log.info("UserService.socialUserService() beginning"+socialUser.getDisplayName());
		boolean tokeStatus = false;
		tokeStatus = socialUserHandler.validateSocialToken(socialUser);
		ResponseEntity<Map<String, Object>> socialUserLoginDetails = null;
		if (tokeStatus) {
			userDatStorage.inMemorySocialUserData(socialUser);
			socialUserLoginDetails = socialUserHandler.getSocialUserLoginDetails(socialUser, response);
		} else {
			log.error("Invalid social user token:::" + socialUser);
			throw new TokenInvalidException("Invalid social user token.");
		}
		log.info("UserService.socialUserService() end");
		return socialUserLoginDetails;
	}

	public ResponseEntity<Map<String, Object>> authenticateUser(CustomUserLoginDetails user,
			HttpServletResponse response) throws UserNotFoundException {
		log.info("UserService.authenticateUser() beginning...");
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> finalMap = new HashMap<>();
		UserCredentials obj = new UserCredentials();
		obj.setUsername(user.getUsername().replaceAll("\\s", ""));
		obj.setPassword(user.getPassword());
		ResponseEntity<String> result = null;
		try {
			result = userServiceFeign.getToken(obj);
			log.info(result.toString());
			if (result.getStatusCode().toString().replace(" OK", "").equals("200")) {
				JwtToken jwtList = new JwtToken();
				User containsName = userRepository.findByUsernameOrEmailOrId(user.getUsername());
				map.put("username", containsName.getUserName());
				map.put("email", containsName.getEmail());
				map.put("userrole", containsName.getUserRole().ordinal());
				map.put("displayname", containsName.getDisplayName());
				map.put("pictureurl", containsName.getPictureUrl());
				map.put("subscription", containsName.isSubscription());
				jwtList.setJwtToken(
						result.getHeaders().get("Authorization").toString().replace("[", "").replace("]", ""));
				userJwtRepository.save(jwtList.getJwtToken(), containsName.getUserName());
				HttpHeaders responseHeaders = new HttpHeaders();
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setHeader("authenticated",
						result.getHeaders().get("Authorization").toString().replace("[", "").replace("]", ""));
				response.setHeader("token_expireIn", result.getHeaders().get("tokenexpiretime").toString());
				response.setHeader("status", result.getStatusCode().toString().replace("OK", ""));
				finalMap.put("content",map);
				return ResponseEntity.ok().headers(responseHeaders).body(finalMap);
			}
			log.info("UserService.authenticateUser() end");
		} catch (Exception e) {
			e.printStackTrace();
			throw new UserNotFoundException("Username or password are Invalid");
		}
		return ResponseEntity.ok().body(map);
	}

	public ResponseEntity<Map<String, Object>> validateTheJwtToken(String jwtToken, HttpServletResponse response)
			throws TokenInvalidException {
		log.info("UserService.validateTheJwtToken() beginning...");
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> finalMap = new HashMap<>();
		User userDetails = userJwtRepository.existsByJwttoken(jwtToken);
		if (userDetails == null) {
			throw new TokenInvalidException("Invalid Token");
		}
		map.put("username", userDetails.getUserName());
		map.put("email", userDetails.getEmail());
		map.put("userrole", userDetails.getUserRole().ordinal());
		map.put("displayname", userDetails.getDisplayName());
		map.put("pictureUrl", userDetails.getPictureUrl());
		HttpHeaders responseHeaders = new HttpHeaders();
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setHeader("authenticated", jwtToken);
		finalMap.put("content", map);
		log.info("UserService.validateTheJwtToken() end");
		return ResponseEntity.ok().headers(responseHeaders).body(finalMap);

	}

	public User getUserDetails(String usernameoremailorId) {
		return userRepository.findByUsernameOrEmailOrId(usernameoremailorId);
	}

	public Map<String, Object> getTwitterFields() {
		Map<String, Object> map = new HashMap<>();
		map.put("client-id", clientId);
		map.put("client-secret", clientSecret);
		map.put("forwaruri", forwarUri);
		log.info("UserService.getTwitterFields() end");
		return map;
	}

	public CopyOnWriteArrayList<User> fetchAll() {
		return userRepository.findAll();
	}

	public ResponseEntity<Map<String, Object>> updateUserDetails(UpdateUserVo uservo, HttpServletRequest req) throws TokenInvalidException {
		log.info("UserService.updateUserDetails() begining" + uservo);
		String token = req.getHeader("Authorization");
		User user = userJwtRepository.existsByJwttoken(token);
		Map<String, Object> map = new HashMap<>();
		String email = uservo.getEmail();
		Integer userRole = uservo.getUserRole();
		String password = uservo.getPassword();
		String displayName = uservo.getDisplayName();
		String pictureUrl = uservo.getPictureUrl();
		boolean subscription = uservo.isSubscription();

		if (email != null && !email.isEmpty()) {
			user.setEmail(email);
		}
		if (userRole != null) {
			user.setUserRole(localUserRole.getLocalUserRole(userRole));
		}
		if (password != null && !password.isEmpty()) {
			user.setPassword(password);
		}
		if (displayName != null && !displayName.isEmpty()) {
			user.setDisplayName(displayName);
		}
		if (pictureUrl != null && !pictureUrl.isEmpty()) {
			user.setPictureUrl(pictureUrl);
		}
		if (subscription) {
			user.setSubscription(subscription);
		}
		user = userRepository.update(user);

		UserVo userVo = new UserVo();
		//
		userVo.setUserId(user.getUserId());
		userVo.setEmail(user.getEmail());
		userVo.setDisplayName(user.getDisplayName());
		userVo.setUserRole(user.getUserRole().ordinal());
		userVo.setPictureUrl(user.getPictureUrl());
		userVo.setSubscription(user.isSubscription());
		log.info("UserService.updateUserDetails() end");
		map.put("content", userVo);
		return ResponseEntity.ok().body(map);
	}

	public ResponseEntity<Map<String, Object>> getUserProfile(HttpServletRequest req) throws TokenInvalidException {
		log.info("UserService.getUserProfile() begining"+req.getHeader("Authorization"));
		Map<String, Object> map = new HashMap<>();
		String token = req.getHeader("Authorization");
		User user = userJwtRepository.existsByJwttoken(token);
		System.out.println(user.toString());
		UserVo userVo = new UserVo();
		userVo.setUserId(user.getUserId());
		userVo.setSource(user.getSource());
		userVo.setEmail(user.getEmail());
		userVo.setDisplayName(user.getDisplayName());
		userVo.setUserRole(user.getUserRole().ordinal());
		userVo.setPictureUrl(user.getPictureUrl());
		userVo.setUserName(user.getUserName());
		userVo.setSubscription(user.isSubscription());
		log.info("UserService.getUserProfile() end");
		map.put("content", userVo);
		return ResponseEntity.ok().body(map);
	}

}
