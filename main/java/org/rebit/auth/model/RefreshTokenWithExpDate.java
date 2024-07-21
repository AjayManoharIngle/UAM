package org.rebit.auth.model;

import java.util.Date;

public class RefreshTokenWithExpDate {
	private String refeshToken;
	private Date exptime;
	public String getRefeshToken() {
		return refeshToken;
	}
	public void setRefeshToken(String refeshToken) {
		this.refeshToken = refeshToken;
	}
	public Date getExptime() {
		return exptime;
	}
	public void setExptime(Date exptime) {
		this.exptime = exptime;
	}
}
