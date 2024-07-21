package org.rebit.auth.model;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.NotBlank;

public class UsernameAndPasswordDto {

	@NotBlank(message = "{userName.notempty}")
	@Ascii
	private String userName;

	@NotBlank(message = "{password.notempty}")
	@Ascii
	private String password;
	@Ascii
	private String captchaToken;
	@Ascii
	private String captchaValue;
	@Ascii
	private String token;
	@Ascii
	private String tokenValue;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public String getCaptchaToken() {
		return captchaToken;
	}

	public void setCaptchaToken(String captchaToken) {
		this.captchaToken = captchaToken;
	}

	public String getCaptchaValue() {
		return captchaValue;
	}

	public void setCaptchaValue(String captchaValue) {
		this.captchaValue = captchaValue;
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
}
