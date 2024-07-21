package org.rebit.auth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RefreshTokenDto {

	@NotBlank(message="{token.notempty}" )
	@Pattern(regexp = "^[A-Za-z0-9]+$",message="{token.pattern}")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
