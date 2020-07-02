package com.otsi.ndap.a4nlpdb.ndapuserservice.utils;

import org.springframework.stereotype.Service;

import com.otsi.ndap.a4nlpdb.ndapuserservice.common.UserRole;

@Service
public class LocalUserRole {

	public UserRole getLocalUserRole(int i) {
		UserRole userRole = null;
		switch (i) {
		case 1:
			userRole = UserRole.Researchers;
			break;
		case 2:
			userRole = UserRole.PolicyMakers;
			break;
		case 3:
			userRole = UserRole.DataScientists;
			break;
		case 4:
			userRole = UserRole.Journalists;
			break;
		case 5:
			userRole = UserRole.GeneralPublic;
			break;
		case 6:
			userRole = UserRole.Corporate;
			break;

		default:
			userRole = UserRole.Default;
			break;
		}
		return userRole;
	}
}
