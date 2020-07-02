package com.otsi.ndap.a4nlpdb.ndapuserservice.feignservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.otsi.ndap.a4nlpdb.ndapuserservice.utils.UserCredentials;

@FeignClient(name = "ndap-auth-service")
public interface UserServiceFegin {

	@PostMapping("/auth/")
	ResponseEntity<String> getToken(@RequestBody UserCredentials user);

}

