package com.otsi.ndap.a4nlpdb.ndapauthservice.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.otsi.ndap.a4nlpdb.ndapauthservice.feignservice.UserServiceFegin;

/*
* @author samba siva
*/

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {
	
	private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	/*
	 * @Autowired private BCryptPasswordEncoder encoder;
	 */
	
	@Autowired
	private UserServiceFegin userServiceFeign;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("UserDetailsServiceImpl.loadUserByUsername()"+username);
		com.otsi.ndap.a4nlpdb.ndapauthservice.entity.User appUser = userServiceFeign.getUserByNameorEmailorId(username);
		System.out.println(appUser);
				if(!appUser.getSource().equals("email")) {
					//this for social user login
					if (appUser.getUserId().equals(username)) {
						List<GrantedAuthority> grantedAuthorities = AuthorityUtils
								.commaSeparatedStringToAuthorityList("ROLE_"+appUser.getUserRole());
						return new User(appUser.getUserId(), appUser.getPassword(), grantedAuthorities);
					}
				} else if (appUser.getUserName().equals(username) || appUser.getEmail().equals(username)) {
					List<GrantedAuthority> grantedAuthorities = AuthorityUtils
							.commaSeparatedStringToAuthorityList("ROLE_"+appUser.getUserRole());
					return new User(appUser.getUserName(), appUser.getPassword(), grantedAuthorities);
				}
		
		throw new UsernameNotFoundException("Username: " + username + " not found");
	}
}