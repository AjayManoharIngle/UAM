package org.rebit.auth.model;

import java.util.List;

public class UserInfo {
private String userId;
private List<String> roles;
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}

public List<String> getRoles() {
	return roles;
}
public void setRoles(List<String> roles) {
	this.roles = roles;
}
@Override
public String toString() {
	return "TokenUserDetails [userId=" + userId + ", role=" + roles + "]";
}
public UserInfo(String userId, List<String> roles) {
	super();
	this.userId = userId;
	this.roles = roles;
}
public UserInfo() {
	super();
}
}
