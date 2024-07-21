package org.rebit.auth.model;

import java.util.List;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ApiAndRolesDto {
@Ascii
@NotBlank(message = "{apiAndRolesDto.uri.not.empty}")
private String uri;
@Ascii
@NotBlank(message = "{apiAndRolesDto.methodType.not.empty}")
@Pattern(regexp="^(GET|POST|PUT|PATCH|DELETE|OPTIONS)$",message="{methodType.only.allowed}")
private String methodType;
@Valid
@NotNull(message = "{apiAndRolesDto.roles.one.role}")
private List<String> roles;

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
