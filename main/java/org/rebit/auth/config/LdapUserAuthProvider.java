package org.rebit.auth.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.entity.UserMaster;
import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.repository.UserMasterRepository;
import org.rebit.auth.util.LoggerUtil;
import org.rebit.auth.util.ReBITUserManagementConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;


@Component
public class LdapUserAuthProvider {
	
	final static Logger logger = LogManager.getLogger();
	
	@Autowired
	private UserMasterRepository userMasterRepository;
	
	private String factoryClassName = "com.sun.jndi.ldap.LdapCtxFactory";
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private GlobalProperties globalProperties;
	

	public void getAllUsersFromLdap(String adminUser,String adminToken) throws NamingException {
		logger.trace("Entry into getAllUsers");
		Properties env = new Properties();
		if(!StringUtils.isEmpty(globalProperties.getDnUsernameAttribute())) {
			env.put(Context.INITIAL_CONTEXT_FACTORY, factoryClassName);
			env.put(Context.PROVIDER_URL, globalProperties.getLdapUrl());
			env.put(Context.SECURITY_PRINCIPAL, globalProperties.getDnUsernameAttribute()+"="+adminUser+","+globalProperties.getBaseDn());
			env.put(Context.SECURITY_CREDENTIALS, adminToken);
		}else if(!StringUtils.isEmpty(globalProperties.getDomainName())){
			env.put(Context.INITIAL_CONTEXT_FACTORY, factoryClassName);
			env.put(Context.PROVIDER_URL, globalProperties.getLdapUrl());
			env.put("LDAP_BASEDN", globalProperties.getBaseDn());
			env.put(Context.SECURITY_PRINCIPAL, adminUser+"@"+globalProperties.getDomainName());
			env.put(Context.SECURITY_CREDENTIALS, adminToken);
		}else {
			throw new AuthManagementException("please provide userNameAttribute or domainName");
		}
		DirContext connection = null;
		try {
			connection = new InitialDirContext(env);
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration users = connection.search(globalProperties.getDnForSearchFilter(), globalProperties.getSearchFilterObject(), controls);
			List<UserMaster> existingUserMasterList=userMasterRepository.findByIsLDAPUser(1L);
			Map<String, UserMaster> existingUserMasterMap = new HashMap<String, UserMaster>();
			if(existingUserMasterList!=null) {
				for (UserMaster userMaster : existingUserMasterList) {
					existingUserMasterMap.put(userMaster.getUserName(), userMaster);
				}
			}

			List<UserMaster> userMasterList = new ArrayList<UserMaster>();
			SearchResult result = null;
			while (users.hasMore()) {
				result = (SearchResult) users.next();
				Attributes attr = result.getAttributes();
				
				logger.debug("All Attributes Name \n "+attr);
				loggerUtil.printSuccessLogs(logger, ReBITUserManagementConstant.LDAP_SYNC, adminUser, request.getRemoteAddr(), attr.toString(), "All Attributes Name");
		
				
				UserMaster userMaster = null;
				if(!StringUtils.isEmpty(globalProperties.getUsernameAttribute()) && attr.get(globalProperties.getUsernameAttribute())!=null) {
				
					if(attr.get(globalProperties.getUsernameAttribute()).get()!=null) {
						userMaster = existingUserMasterMap.remove(attr.get(globalProperties.getUsernameAttribute()).get().toString());
						if(userMaster==null) {
							userMaster = new UserMaster();
							userMaster.setIsLDAPUser(1L);
							userMaster.setCreatorUserId(adminUser);
							userMaster.setCreatedAt(new Date());
						}else {
							userMaster.setUpdaterUserId(adminUser);
							userMaster.setUpdatedAt(new Date());
						}
						userMaster.setStatus(1L);
					}

					if (!StringUtils.isEmpty(globalProperties.getUsernameAttribute()) && attr.get(globalProperties.getUsernameAttribute()) != null) {
						userMaster.setUserName(attr.get(globalProperties.getUsernameAttribute()).get().toString());
					}

					if (!StringUtils.isEmpty(globalProperties.getFirstnameAttribute()) && attr.get(globalProperties.getFirstnameAttribute()) != null) {
						userMaster.setFirstName(attr.get(globalProperties.getFirstnameAttribute()).get().toString());
					}

					if (!StringUtils.isEmpty(globalProperties.getLastnameAttribute()) && attr.get(globalProperties.getLastnameAttribute()) != null) {
						userMaster.setLastName(attr.get(globalProperties.getLastnameAttribute()).get().toString());
					}

					if (!StringUtils.isEmpty(globalProperties.getEmailAttribute()) && attr.get(globalProperties.getEmailAttribute()) != null) {
						userMaster.setUserEmailId(attr.get(globalProperties.getEmailAttribute()).get().toString());
					}else {
						userMaster.setUserEmailId(attr.get(globalProperties.getUsernameAttribute()).get().toString()+"@"+globalProperties.getDomainName());
					}

					if (!StringUtils.isEmpty(globalProperties.getMobileAttribute()) && attr.get(globalProperties.getMobileAttribute()) != null) {
						userMaster.setUserMobileNo(attr.get(globalProperties.getMobileAttribute()).get().toString());
					}else {
						if("true".equalsIgnoreCase(globalProperties.getGenerateMobileNumber())) {
							userMaster.setUserMobileNo(getUniquMobileNumber());
						}
					}
				}
				if(userMaster!=null) {	
				userMasterList.add(userMaster);
				}
			}
			for (String userName : existingUserMasterMap.keySet()) {	
				UserMaster userDetailsToDelete = existingUserMasterMap.get(userName);
				userDetailsToDelete.setStatus(0L);
				userDetailsToDelete.setUpdaterUserId(adminUser);
				userDetailsToDelete.setUpdatedAt(new Date());
				userMasterList.add(userDetailsToDelete);
			}
			userMasterRepository.saveAll(userMasterList);
		}finally {
			if(connection!=null) {
				connection.close();
			}
		}
		logger.trace("Exit into getAllUsers");
	}
	
	private String getUniquMobileNumber() {
		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		return Long.toString(number);
	}
	public boolean authenticatUser(String userName,String token) {
		logger.trace("Entry into authenticatUser");
		Properties env = new Properties();
		if(!StringUtils.isEmpty(globalProperties.getDnUsernameAttribute())) {
			env.put(Context.INITIAL_CONTEXT_FACTORY, factoryClassName);
			env.put(Context.PROVIDER_URL, globalProperties.getLdapUrl());
			env.put(Context.SECURITY_PRINCIPAL, globalProperties.getDnUsernameAttribute()+"="+userName+","+globalProperties.getBaseDn());
			env.put(Context.SECURITY_CREDENTIALS, token);
		}else if(!StringUtils.isEmpty(globalProperties.getDomainName())){
			env.put(Context.INITIAL_CONTEXT_FACTORY, factoryClassName);
			env.put(Context.PROVIDER_URL, globalProperties.getLdapUrl());
			env.put("LDAP_BASEDN", globalProperties.getBaseDn());
			env.put(Context.SECURITY_PRINCIPAL, userName+"@"+globalProperties.getDomainName());
			env.put(Context.SECURITY_CREDENTIALS, token);
		}else {
			throw new AuthManagementException("please provide userNameAttribute or domainName");
		}
		DirContext connection = null;
		try {
			connection = new InitialDirContext(env);
			connection.close();
			return true;
		} catch (AuthenticationException ex) {
			logger.trace("Exit into authenticatUser");
			logger.error(ex.getMessage());
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			return false;
		} catch (NamingException e) {
			logger.trace("Exit into authenticatUser");
			logger.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
	}
	
}
