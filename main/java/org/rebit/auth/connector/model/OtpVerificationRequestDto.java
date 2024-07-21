package org.rebit.auth.connector.model;

import jakarta.validation.constraints.NotEmpty;

public class OtpVerificationRequestDto {
@NotEmpty(message = "{smsToken.not.empty}")	
private String smsToken;
@NotEmpty(message = "{key.not.empty}")
private String key;
@NotEmpty(message = "{value.not.empty}")
private String value;
public String getSmsToken() {
	return smsToken;
}
public void setSmsToken(String smsToken) {
	this.smsToken = smsToken;
}
public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
}
