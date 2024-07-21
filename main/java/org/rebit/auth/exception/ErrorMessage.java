package org.rebit.auth.exception;

import java.util.Date;

/**
 * 
 * @author Anuradha.Phalke
 * Purpose: Case Management System Error Message  class
 * Created Date: 26-03-2021
 * Modification History:     Last Change Date:       Revised By:     Change/Defect description:
 * CR Defect No
 */

public class ErrorMessage {

private Date errorDate;
private String errorMessage;

public ErrorMessage() {
	super();
}
public ErrorMessage(Date errorDate, String errorMessage) {
	super();
	this.errorDate = errorDate;
	this.errorMessage = errorMessage;
}
public Date getErrorDate() {
	return errorDate;
}
public void setErrorDate(Date errorDate) {
	this.errorDate = errorDate;
}
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}
@Override
public String toString() {
	return "ErrorMessage [errorDate=" + errorDate + ", errorMessage=" + errorMessage + "]";
}
}
