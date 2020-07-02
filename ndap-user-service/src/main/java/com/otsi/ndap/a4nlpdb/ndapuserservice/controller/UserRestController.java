package com.otsi.ndap.a4nlpdb.ndapuserservice.controller;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.SocialUserVerification;
import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.User;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.TokenInvalidException;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.UserAlreadyException;
import com.otsi.ndap.a4nlpdb.ndapuserservice.exception.UserNotFoundException;
import com.otsi.ndap.a4nlpdb.ndapuserservice.service.UserService;
import com.otsi.ndap.a4nlpdb.ndapuserservice.vo.CustomUserLoginDetails;
import com.otsi.ndap.a4nlpdb.ndapuserservice.vo.UpdateUserVo;
import com.otsi.ndap.a4nlpdb.ndapuserservice.vo.UserVo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/*
* @author Samba Siva,Chandrakanth
*/

@RestController
@RequestMapping("/api")
public class UserRestController {

	private final Logger log = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	private Environment env;

	@Autowired
	private UserService userService;

	@GetMapping("/testing")
	@CrossOrigin(origins = "*", allowedHeaders = "Authorization")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Something went wrong"),
			@ApiResponse(code = 403, message = "Access denied"),
			@ApiResponse(code = 500, message = "Expired or invalid  token") })
	public String home(HttpServletRequest req,HttpServletResponse resp) {
		System.out.println("authorization" + req.getHeader("Authorization"));
		return "Hello from UserManagement Service running at port: " + env.getProperty("local.server.port");
	}

	@PostMapping(path = "/user/register")
	@ApiOperation(value = "${UserRestController.register}")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Something went wrong"),
			@ApiResponse(code = 403, message = "Access denied"),
			@ApiResponse(code = 208, message = "Username is already in use"), })
	public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserVo user, HttpServletResponse response)
			throws UserAlreadyException {
		log.info("UserRestController.createUser() : " + user);
		return userService.userRegistraionService(user, response);
	}

	@PostMapping(path = "/socialuser/login", consumes = "application/json", produces = "application/json")
	@ApiOperation(value = "${UserRestController.socialregister}")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Something went wrong"),
			@ApiResponse(code = 403, message = "Access denied"),
			@ApiResponse(code = 500, message = "Expired or invalid token")

	})
	public ResponseEntity<Map<String, Object>> registerSocialUser(@RequestBody SocialUserVerification socialUser,
			HttpServletResponse response) throws Exception {
		log.info("UserRestController.registerSocialUser()");
		return userService.socialUserService(socialUser, response);
	}

	@PostMapping(path = "/authenticate/login", consumes = "application/json", produces = "application/json")
	@ApiOperation(value = "${UserRestController.login}")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Something went wrong"),
			@ApiResponse(code = 403, message = "Access denied"),
			@ApiResponse(code = 401, message = "Username or password wrong"), })
	public ResponseEntity<Map<String, Object>> customUserLogin(@RequestBody CustomUserLoginDetails user,
			HttpServletResponse response) throws Exception {
		log.info("UserRestController.customUserLogin()");
		return userService.authenticateUser(user, response);
	}

	@PostMapping(path = "/validatetoken")
	@ApiOperation(value = "${UserRestController.tokenvalidate}")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Something went wrong"),
			@ApiResponse(code = 403, message = "Access denied"),
			@ApiResponse(code = 500, message = "Expired or invalid  token") })
	public ResponseEntity<Map<String, Object>> TokenValidation(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("UserRestController.jwtTokenValidation() : ");
		String tokenVal = request.getHeader("token");
		return userService.validateTheJwtToken(tokenVal, response);
	}

	@GetMapping(path = "/twitter/fields")
	@ApiOperation(value = "${UserRestController.twitter}")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Something went wrong"),
			@ApiResponse(code = 403, message = "Access denied"), })
	public Map<String, Object> getTwitterFields() {
		log.info("UserRestController.getTwitterFields()");
		return Collections.singletonMap("content_fields", userService.getTwitterFields());
	}

	@GetMapping("/user/inmemorydata")
	@ApiOperation(value = "${UserRestController.allusers}")
	@ApiResponses(value = { //
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 403, message = "Access denied"), //
	})
	public CopyOnWriteArrayList<User> getUserInMemoryData() {
		log.info("UserRestController.getUserInMemoryData()");
		return userService.fetchAll();
	}

	@PostMapping(path = "/user/getbyusername/{userName}")
	public User getUserByNameorEmailorId(@PathVariable("userName") String userName) {
		log.info("UserRestController.getUserByName()" + userName);
		return userService.getUserDetails(userName);
	}
	
	@GetMapping(path = "/profile")
	@ApiOperation(value = "${UserRestController.profile}")
	@ApiResponses(value = { //
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 403, message = "Access denied"), //
			@ApiResponse(code = 500, message = "Expired or invalid  token") })
	public ResponseEntity<Map<String, Object>> getProfile(HttpServletRequest req,HttpServletResponse resp) throws TokenInvalidException {
		log.info("UserRestController.getProfile()");
		return userService.getUserProfile(req);
	}
	

	@PostMapping(path = "/update")
	@ApiOperation(value = "${UserRestController.update}")
	@ApiResponses(value = { //
			@ApiResponse(code = 400, message = "Something went wrong"), //
			@ApiResponse(code = 403, message = "Access denied"), //
			@ApiResponse(code = 500, message = "Expired or invalid  token") })
	public ResponseEntity<Map<String, Object>> updateUserDetails(@RequestBody UpdateUserVo uservo, HttpServletRequest req) throws Exception {
		log.info("UserRestController.createUser() : " + uservo);
		return userService.updateUserDetails(uservo, req);
	}
}
