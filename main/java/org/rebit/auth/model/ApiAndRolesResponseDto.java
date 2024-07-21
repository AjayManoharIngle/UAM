package org.rebit.auth.model;

import java.util.List;

public class ApiAndRolesResponseDto {

private String uri;
private String methodType;
private List<String> roles;
private boolean permitAll;


public boolean isPermitAll() {
	return permitAll;
}

public void setPermitAll(boolean permitAll) {
	this.permitAll = permitAll;
}

public String getMethodType() {
	return methodType;
}

public void setMethodType(String methodType) {
	this.methodType = methodType;
}

public String getUri() {
	return uri;
}

public void setUri(String uri) {
	this.uri = uri;
}

public List<String> getRoles() {
	return roles;
}

public void setRoles(List<String> roles) {
	this.roles = roles;
}

}
