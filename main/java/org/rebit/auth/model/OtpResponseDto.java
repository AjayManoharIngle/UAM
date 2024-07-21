package org.rebit.auth.model;

public class OtpResponseDto {
private String token;
private int expiredTimeInSecond;
public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public int getExpiredTimeInSecond() {
	return expiredTimeInSecond;
}
public void setExpiredTimeInSecond(int expiredTimeInSecond) {
	this.expiredTimeInSecond = expiredTimeInSecond;
}
}
