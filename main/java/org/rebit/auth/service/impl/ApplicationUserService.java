package org.rebit.auth.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.service.ApplicationUserServices;
import org.rebit.auth.util.LoggerUtil;
import org.rebit.auth.util.RSAUtil;
import org.rebit.auth.util.ReBITUserManagementConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ApplicationUserService implements UserDetailsService{
	
	final static Logger logger = LogManager.getLogger();
	
	@Autowired
	private ApplicationUserServices applicationUserServices;
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private RSAUtil RSAUtil;

	@Override
	public UserDetails loadUserByUsername(String customizedUserName) throws UsernameNotFoundException {
		logger.trace("Entry into loadUserByUsername");
		
		String[] orgUserNameAndPassword = customizedUserName.split("separator");
		String orgUserName = orgUserNameAndPassword[0];
		
		try {
			orgUserName=RSAUtil.decrypt(orgUserName);
		} catch (Exception e) {
			logger.error("error -In  AuthServiceImpl login "+e.getMessage());
			loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGIN_TYPE, orgUserName, request.getRemoteAddr(), "", "Invalid encrypted value for UserName");
			throw new AuthManagementException("Error while decrypting userName");
		}

		String orgPassword = orgUserNameAndPassword[1];
		
		logger.trace("Exit from loadUserByUsername");
		return applicationUserServices.selectApplicationUserByUsername(orgUserName,customizedUserName,orgPassword);
	
		}

}
