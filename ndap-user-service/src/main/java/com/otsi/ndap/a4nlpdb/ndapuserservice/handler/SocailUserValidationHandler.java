package com.otsi.ndap.a4nlpdb.ndapuserservice.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.otsi.ndap.a4nlpdb.ndapuserservice.controller.UserRestController;
import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.SocialUserVerification;
import com.otsi.ndap.a4nlpdb.ndapuserservice.feignservice.UserServiceFegin;
import com.otsi.ndap.a4nlpdb.ndapuserservice.repositories.UserJwtRepository;
import com.otsi.ndap.a4nlpdb.ndapuserservice.utils.FacebookTokenUtils;
import com.otsi.ndap.a4nlpdb.ndapuserservice.utils.GoogleTokenUtils;
import com.otsi.ndap.a4nlpdb.ndapuserservice.utils.UserCredentials;

import net.minidev.json.JSONObject;

/*
* @author Samba Siva
*/

@Service
public class SocailUserValidationHandler {

	private final Logger log = LoggerFactory.getLogger(SocailUserValidationHandler.class);
	
	@Autowired
	private UserServiceFegin userServiceFeign;

	@Value("${google.token.validation}")
	private String googleRestUri;

	@Value("${facebook.token.validation}")
	private String facebookRestUri;

	@Value("${twitter.token.validation}")
	private String twitterRestUri;

	@Value("${internal.jwt.tokenuri}")
	private String jwtTokenUri;
	
	@Autowired
	private UserJwtRepository userJwtRepository;

	private final CloseableHttpClient httpClient = HttpClients.createDefault();

	public boolean validateSocialToken(SocialUserVerification user) throws Exception {
		log.info("SocailUserValidationHandler.validateSocialToken() beginning...");
		boolean status = false;
		switch (user.getSource()) {
		case "google":
			HttpPost googlePost = new HttpPost(googleRestUri);
			List<NameValuePair> googlePrams = new ArrayList<>();
			googlePrams.add(new BasicNameValuePair("access_token", user.getSocialToken()));
			googlePost.setEntity(new UrlEncodedFormEntity(googlePrams));
			try (CloseableHttpClient httpClient = HttpClients.createDefault();
					CloseableHttpResponse response = httpClient.execute(googlePost)) {
				String googleTokenResp = EntityUtils.toString(response.getEntity());
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				GoogleTokenUtils tokenInfo = gson.fromJson(googleTokenResp, GoogleTokenUtils.class);
				if (tokenInfo.getError() != null || tokenInfo.getError_description() != null) {
					status = false;
				} else if (tokenInfo.getUser_id() != null) {
					status = true;
				}
				log.info("SocailUserValidationHandler:::case:::google" + googleTokenResp);
				close();
			}
			break;
		case "facebook":
			HttpPost facebookPost = new HttpPost(facebookRestUri);
			List<NameValuePair> facebookParam = new ArrayList<>();
			facebookParam.add(new BasicNameValuePair("access_token", user.getSocialToken()));
			facebookPost.setEntity(new UrlEncodedFormEntity(facebookParam));
			try (CloseableHttpClient httpClient = HttpClients.createDefault();
					CloseableHttpResponse response = httpClient.execute(facebookPost)) {
				String facebookTokenResp = EntityUtils.toString(response.getEntity());
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				FacebookTokenUtils tokenInfo = gson.fromJson(facebookTokenResp, FacebookTokenUtils.class);
				if (tokenInfo.isSuccess()) {
					status = true;
				} else {
					status = false;
				}
				System.out.println("SocailUserValidationHandler:::case:::facebook" + facebookTokenResp);
				close();
			}
			break;
		case "twitter":
			HttpPost twitterPost = new HttpPost(facebookRestUri);
			List<NameValuePair> twitterParam = new ArrayList<>();
			twitterParam.add(new BasicNameValuePair("oauth_consumer_key", user.getSocialToken()));
			twitterPost.setEntity(new UrlEncodedFormEntity(twitterParam));
			try (CloseableHttpClient httpClient = HttpClients.createDefault();
					CloseableHttpResponse response = httpClient.execute(twitterPost)) {

				String twitterTokenResp = EntityUtils.toString(response.getEntity());
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				FacebookTokenUtils tokenInfo = gson.fromJson(twitterTokenResp, FacebookTokenUtils.class);
				//code need to write here
				
				System.out.println("SocailUserValidationHandler:::case:::twitter" + twitterTokenResp);
				close();
			}
			break;
		default:
			log.info("SocailUserValidationHandler:::validateSocialToken(arg1) social user type is not available.");
			status = false;
		}
		log.info("SocailUserValidationHandler.validateSocialToken() end");
		return status;
	}

	public ResponseEntity<Map<String, Object>> getSocialUserLoginDetails(SocialUserVerification socialUser,HttpServletResponse response) {
		log.info("SocailUserValidationHandler.getSocialUserLoginDetails() beginning ...");
		Map<String, Object> map = new HashMap<>();
		UserCredentials obj = new UserCredentials();
		obj.setUsername(socialUser.getUserId());
		obj.setPassword(socialUser.getUserId());
		ResponseEntity<String> result = userServiceFeign.getToken(obj);
		HttpHeaders responseHeaders = new HttpHeaders();
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		map.put("username", socialUser.getUserName());
		map.put("userrole", socialUser.getUserRole());
		map.put("email", socialUser.getEmail());
		map.put("displayname", socialUser.getDisplayName());
		map.put("pictureurl", socialUser.getPictureUrl());
		map.put("subscription", socialUser.isSubscription());
		response.setHeader("authenticated", result.getHeaders().get("Authorization").toString().replace("[", "").replace("]", ""));
		response.setHeader("token_expireIn", result.getHeaders().get("tokenexpiretime").toString());
		response.setHeader("status", result.getStatusCode().toString().replace("OK", ""));
		userJwtRepository.save(result.getHeaders().get("Authorization").toString().replace("[", "").replace("]", ""),
				socialUser.getUserId());
		log.info("SocailUserValidationHandler.generateJwtToken() end");
		return ResponseEntity.ok().headers(responseHeaders).body(map);
	}
	
	private void close() throws IOException {
		httpClient.close();
	}
}
