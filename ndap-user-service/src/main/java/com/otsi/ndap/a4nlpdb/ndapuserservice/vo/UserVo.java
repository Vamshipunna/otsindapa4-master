package com.otsi.ndap.a4nlpdb.ndapuserservice.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVo implements Serializable{

	private static final long serialVersionUID = 321509176896415642L;

	@ApiModelProperty(position = 0)
	private String userId;
	
	@ApiModelProperty(position = 1)
	private String email;
	
	@ApiModelProperty(position = 2)
	private String source;
	
	@ApiModelProperty(position = 3)
	private String userName;
	
	@ApiModelProperty(position = 4)
	private int userRole;
	
	@ApiModelProperty(position = 5)
	private String password;
	
	@ApiModelProperty(position = 6)
	private String displayName;
	
	@ApiModelProperty(position = 7)
	private String pictureUrl;
	
	@ApiModelProperty(position = 7)
	private boolean subscription;

	public UserVo() {
	}
	
	public UserVo(String email, String source, String userName, int userRole, String password,
			String displayName, String pictureUrl,boolean subscription) {
		super();
		this.email = email;
		this.source = source;
		this.userName = userName;
		this.userRole = userRole;
		this.password = password;
		this.displayName = displayName;
		this.pictureUrl = pictureUrl;
		this.subscription = subscription;
	}
	

}
