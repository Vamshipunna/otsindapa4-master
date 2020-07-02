package com.otsi.ndap.a4nlpdb.ndapzuulproxy.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
* @author samba siva
*/

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtConfig jwtConfig;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				.addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests().antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll().anyRequest()
				.authenticated();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/userservice/api/user/**").antMatchers("/api/user/register")
				.antMatchers("/api/user/getbyusername/**")
				.antMatchers("/swagger-ui.html/**").antMatchers("/service/**")
				.antMatchers("/userservice/api/socialuser/**").antMatchers("/api/socialuser/login")
				.antMatchers("/userservice/api/authenticate/**").antMatchers("/api/authenticate/login")
				.antMatchers("/api/user/inmemorydata")
				.antMatchers("/userservice/api/validatetoken/**")
				.antMatchers("/api/validatetoken")
				.antMatchers("/userservice/api/twitter/**")
				.antMatchers("/api/twitter/fields").antMatchers(HttpMethod.OPTIONS, "/**");
	}

	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}
}