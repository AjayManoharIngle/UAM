package org.rebit.auth.model;

public class KeyAndCaptchaDto {

	private String key;
	private boolean captchaEnable;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isCaptchaEnable() {
		return captchaEnable;
	}
	public void setCaptchaEnable(boolean captchaEnable) {
		this.captchaEnable = captchaEnable;
	}
	
	
	
}
