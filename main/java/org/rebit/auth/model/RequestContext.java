package org.rebit.auth.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class RequestContext {
	 private String reqInfo;
	  private boolean failed;
	  private String errorMessage;
	public String getReqInfo() {
		return reqInfo;
	}
	public void setReqInfo(String reqInfo) {
		this.reqInfo = reqInfo;
	}
	public boolean isFailed() {
		return failed;
	}
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
