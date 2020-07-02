package com.otsi.ndap.a4nlpdb.ndapauthservice.feignservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.otsi.ndap.a4nlpdb.ndapauthservice.entity.User;

@FeignClient(name = "ndap-user-service")
public interface UserServiceFegin {

	@PostMapping("/api/user/getbyusername/{userName}")
	User getUserByNameorEmailorId(@PathVariable("userName") String userName);
	
}

