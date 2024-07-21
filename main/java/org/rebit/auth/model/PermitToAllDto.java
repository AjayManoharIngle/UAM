package org.rebit.auth.model;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PermitToAllDto {

	@Ascii
	@NotBlank(message = "{apiAndRolesDto.uri.not.empty}")
	private String uri;
	@Ascii
	@NotBlank(message = "{apiAndRolesDto.methodType.not.empty}")
	@Pattern(regexp="^(GET|POST|PUT|PATCH|DELETE)$",message="{methodType.only.allowed}")
	private String methodType;
	
	private boolean forceFully;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public boolean isForceFully() {
		return forceFully;
	}

	public void setForceFully(boolean forceFully) {
		this.forceFully = forceFully;
	}
	
	
}
