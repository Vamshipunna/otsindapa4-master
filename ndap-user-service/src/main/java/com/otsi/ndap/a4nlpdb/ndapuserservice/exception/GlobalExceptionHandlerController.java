package com.otsi.ndap.a4nlpdb.ndapuserservice.exception;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/*
* @author Chandrakanth
*/

@RestControllerAdvice
public class GlobalExceptionHandlerController {

	@Bean
	public ErrorAttributes errorAttributes() {
		return new DefaultErrorAttributes() {
			@Override
			public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
				@SuppressWarnings("deprecation")
				Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
				errorAttributes.remove("exception");
				return errorAttributes;
			}
		};
	}

	@ExceptionHandler(CustomException.class)
	public void handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
		res.sendError(ex.getHttpStatus().value(), ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public void handleException(HttpServletResponse res) throws IOException {
		res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
	}

	@ExceptionHandler(TokenInvalidException.class)
	public void invalidToken(HttpServletResponse res, TokenInvalidException ex) throws IOException {
		res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
	}

	@ExceptionHandler(UserAlreadyException.class)
	public void userHandleException(HttpServletResponse res, UserAlreadyException ex) throws IOException {
		res.sendError(HttpStatus.ALREADY_REPORTED.value(), ex.getMessage());
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public void userHandleException(HttpServletResponse res, UserNotFoundException ex) throws IOException {
		res.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
	}
}
