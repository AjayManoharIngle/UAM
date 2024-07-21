package org.rebit.auth.exception.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.model.RequestContext;
import org.rebit.auth.util.LoggerUtil;
import org.rebit.auth.util.ReBITUserManagementConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import io.micrometer.core.instrument.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

public class RequestContextFilter extends AbstractRequestLoggingFilter {
	final static Logger logger = LogManager.getLogger();

	@Autowired
	private RequestContext requestContext;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private LoggerUtil loggerUtil;

	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		requestContext.setReqInfo(message);
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		if (requestContext.isFailed()) {
			String reqInfo = StringUtils.isNotBlank(requestContext.getReqInfo())? requestContext.getReqInfo().concat(message): message;
			requestContext.setReqInfo(reqInfo);
			logger.error("Request processing failed,{}", getFormattedRequest());
			loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.REQUEST_BODY, "", request.getRemoteAddr(), getFormattedRequest(), "Request processing failed");
		}
	}
	
	
	private String getFormattedRequest()
	{
		String formattedRequest="Error Message: "+requestContext.getErrorMessage()+",Request Info:"+requestContext.getReqInfo();
		return formattedRequest;
	}
}