package org.rebit.auth.model;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.NotBlank;

public class TokenAndUri {
@Ascii
private String token;
@Ascii
@NotBlank(message = "{apiUri.not.empty}")
private String apiUri;
@Ascii
@NotBlank(message = "{handShakeToken.not.empty}")
private String handShakeToken;
@Ascii
private String contextPath;
@Ascii
@NotBlank(message = "{methodType.not.empty}")
private String methodType;
@Ascii
@NotBlank(message = "{ipAddress.not.empty}")
private String ipAddress;

public String getIpAddress() {
	return ipAddress;
}
public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
}
public String getMethodType() {
	return methodType;
}
public void setMethodType(String methodType) {
	this.methodType = methodType;
}
public String getContextPath() {
	return contextPath;
}
public void setContextPath(String contextPath) {
	this.contextPath = contextPath;
}
public String getHandShakeToken() {
	return handShakeToken;
}
public void setHandShakeToken(String handShakeToken) {
	this.handShakeToken = handShakeToken;
}
public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public String getApiUri() {
	return apiUri;
}
public void setApiUri(String apiUri) {
	this.apiUri = apiUri;
}
}
