package org.rebit.auth.model;

public class TokenResponse {
private String jwtToken;
private String refreshToken;
private String userName;
private String password;
private Boolean isOtpEnable;
private OtpResponseDto tokenObj;
public String getJwtToken() {
	return jwtToken;
}
public void setJwtToken(String jwtToken) {
	this.jwtToken = jwtToken;
}
public String getRefreshToken() {
	return refreshToken;
}
public void setRefreshToken(String refreshToken) {
	this.refreshToken = refreshToken;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public Boolean getIsOtpEnable() {
	return isOtpEnable;
}
public void setIsOtpEnable(Boolean isOtpEnable) {
	this.isOtpEnable = isOtpEnable;
}
public OtpResponseDto getTokenObj() {
	return tokenObj;
}
public void setTokenObj(OtpResponseDto tokenObj) {
	this.tokenObj = tokenObj;
}
}
