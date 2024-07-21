package org.rebit.auth.connector.model;

import jakarta.validation.constraints.NotEmpty;

public class VerifyUserInfoDto {
	@NotEmpty(message = "{token.not.empty}")
	private String token;
	@NotEmpty(message = "{email.not.empty}")
	private String email;
	@NotEmpty(message = "{captchaValue.not.empty}")
	private String captchaValue;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCaptchaValue() {
		return captchaValue;
	}

	public void setCaptchaValue(String captchaValue) {
		this.captchaValue = captchaValue;
	}
}
