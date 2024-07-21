package org.rebit.auth.connector.model;

public class OtpResponseDto {
private String smsToken;
private int expiredTimeInSecond;
private String key;
public String getSmsToken() {
	return smsToken;
}
public void setSmsToken(String smsToken) {
	this.smsToken = smsToken;
}
public int getExpiredTimeInSecond() {
	return expiredTimeInSecond;
}
public void setExpiredTimeInSecond(int expiredTimeInSecond) {
	this.expiredTimeInSecond = expiredTimeInSecond;
}
public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}
}
