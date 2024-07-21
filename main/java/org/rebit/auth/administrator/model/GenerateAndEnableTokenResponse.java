package org.rebit.auth.administrator.model;
public class GenerateAndEnableTokenResponse {

	private boolean status;
	private String newToken;
	private String oldToken;
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getNewToken() {
		return newToken;
	}
	public void setNewToken(String newToken) {
		this.newToken = newToken;
	}
	public String getOldToken() {
		return oldToken;
	}
	public void setOldToken(String oldToken) {
		this.oldToken = oldToken;
	}
	
}
