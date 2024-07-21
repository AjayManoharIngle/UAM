package org.rebit.auth.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.rebit.auth.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoggerUtil {
	
	@Autowired
	private JwtConfig jwtConfig;
	
	public void printSuccessLogs(Logger logger,String type,String userName,String ipAddress,String jsonRequest,String message) {
		ThreadContext.put("type", type);
		ThreadContext.put("userName", userName);
		ThreadContext.put("ipAddress", ipAddress);
		ThreadContext.put("jsonRequest", jsonRequest);
		ThreadContext.put("applicationName", jwtConfig.getExternalSystemHandShakeToken());
		logger.log(Level.forName("SUCCESS", 10),message);
		ThreadContext.clearAll();
	}
	
	public void printFailedLogs(Logger logger,String type,String userName,String ipAddress,String jsonRequest,String message) {
		ThreadContext.put("type", type);
		ThreadContext.put("userName", userName);
		ThreadContext.put("ipAddress", ipAddress);
		ThreadContext.put("jsonRequest", jsonRequest);
		ThreadContext.put("applicationName", jwtConfig.getExternalSystemHandShakeToken());
		logger.log(Level.forName("FAILED", 20),message);
		ThreadContext.clearAll();
	}
	
	public void printFailedLogsObjectCreatedMyDev(Logger logger,String type,String userName,String ipAddress,String jsonRequest,String message,JwtConfig jwtConfigPass) {
		ThreadContext.put("type", type);
		ThreadContext.put("userName", userName);
		ThreadContext.put("ipAddress", ipAddress);
		ThreadContext.put("jsonRequest", jsonRequest);
		ThreadContext.put("applicationName", jwtConfigPass.getExternalSystemHandShakeToken());
		logger.log(Level.forName("FAILED", 20),message);
		ThreadContext.clearAll();
	}
}
