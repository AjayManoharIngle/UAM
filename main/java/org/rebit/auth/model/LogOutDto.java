package org.rebit.auth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LogOutDto {

	@NotBlank(message="{token.notempty}" )
	@Pattern(regexp = "^[A-Za-z0-9]+$",message="{token.pattern}")
	private String refreshToken;
	
	
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}



}
