package org.rebit.auth.connector.model;

import jakarta.validation.constraints.NotEmpty;

public class ResetPasswordDto {
@NotEmpty(message = "{currentPassword.not.empty}")
private String currentPassword;
@NotEmpty(message = "{newPassword.not.empty}")
private String newPassword;
@NotEmpty(message = "{userName.not.empty}")
private String userName;

public String getCurrentPassword() {
	return currentPassword;
}
public void setCurrentPassword(String currentPassword) {
	this.currentPassword = currentPassword;
}
public String getNewPassword() {
	return newPassword;
}
public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
}
