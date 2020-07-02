package com.otsi.ndap.a4nlpdb.ndapuserservice.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.JwtToken;
import com.otsi.ndap.a4nlpdb.ndapuserservice.feignservice.UserServiceFegin;
import com.otsi.ndap.a4nlpdb.ndapuserservice.repositories.UserJwtRepository;
import com.otsi.ndap.a4nlpdb.ndapuserservice.service.UserService;
import com.otsi.ndap.a4nlpdb.ndapuserservice.utils.UserCredentials;
import com.otsi.ndap.a4nlpdb.ndapuserservice.vo.UserVo;

import net.minidev.json.JSONObject;

/*
* @author Samba Siva
*/

@Service
public class CustomUserJwtHandler {

	private final Logger log = LoggerFactory.getLogger(CustomUserJwtHandler.class);
	
	@Autowired
	private UserJwtRepository userJwtRepository;
	
	@Autowired
	private UserServiceFegin userServiceFeign;
	
	
	@Value("${internal.jwt.tokenuri}")
	private String jwtTokenUri;
	
	public Map<String, Object> generateJwtToken(UserVo user,HttpServletResponse response) {
		log.info("CustomUserJwtHandler.generateJwtToken()"+user);
		Map<String, Object> map = new HashMap<>();
		JwtToken jwtToken = new JwtToken();
		UserCredentials obj = new UserCredentials();
		obj.setUsername(user.getUserName().replaceAll("\\s", ""));
		obj.setPassword(user.getPassword());
		ResponseEntity<String> result = userServiceFeign.getToken(obj);
		map.put("username", user.getUserName());
		map.put("email", user.getEmail());
		map.put("userrole", user.getUserRole());
		map.put("displayname", user.getDisplayName());
		map.put("pictureurl", user.getPictureUrl());
		map.put("subscription", user.isSubscription());
		response.setHeader("authenticated", result.getHeaders().get("Authorization").toString().replace("[", "").replace("]", ""));
		response.setHeader("token_expireIn", result.getHeaders().get("tokenexpiretime").toString());
		response.setHeader("status", result.getStatusCode().toString().replace("OK", ""));
		jwtToken.setJwtToken(result.getHeaders().get("Authorization").toString().replace("[", "").replace("]", ""));
		userJwtRepository.save(jwtToken.getJwtToken(),user.getUserName());
		return map;
	}
}
