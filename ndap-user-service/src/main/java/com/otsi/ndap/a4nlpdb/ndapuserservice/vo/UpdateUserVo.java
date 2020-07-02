package com.otsi.ndap.a4nlpdb.ndapuserservice.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
* @author Samba Siva,Chandrakanth
*/

@Data
public class UpdateUserVo {
	
	@ApiModelProperty(position = 0)
	private String email;
	
	@ApiModelProperty(position = 1)
	private Integer userRole;
	
	@ApiModelProperty(position = 2)
	private String password;
	
	@ApiModelProperty(position = 3)
	private String displayName;
	
	@ApiModelProperty(position = 4)
	private String pictureUrl;
	
	@ApiModelProperty(position = 5)
	private boolean subscription;

}
