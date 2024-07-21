package org.rebit.auth.connector.model;

import java.util.Date;
import java.util.List;

import lombok.Data;


@Data
public class UserProfileInfo {

	private String userEmailId;
	private String userName;
	private String firstName;
	private String lastName;
	private Date lastLoginDate;
	private String regionalOfficeDetails;		
	private String officeLocationDetails;
    private List<String> rolesDetails;
	private String userMobileNo;
	private Long status;
	private String creatorUserId;
	private Date createdAt;
	private String updaterUserId;
	private Date updatedAt;		
	private Long isLDAPUser;
	private Long unSuccessAttempt;
	private Date blockedTill;
	private Date passExpired;
	private Date lastFailedAttempt;
}
