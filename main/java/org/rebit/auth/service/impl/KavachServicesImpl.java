package org.rebit.auth.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.config.LdapUserAuthProvider;
import org.rebit.auth.entity.RoleMastDetails;
import org.rebit.auth.entity.UserMaster;
import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.model.ApplicationUser;
import org.rebit.auth.repository.UserMasterRepository;
import org.rebit.auth.service.ApplicationUserServices;
import org.rebit.auth.util.HashingAlgorithm;
import org.rebit.auth.util.LoggerUtil;
import org.rebit.auth.util.RSAUtil;
import org.rebit.auth.util.ReBITUserManagementConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service("KawachImpl")
public class KavachServicesImpl implements ApplicationUserServices{
	
	final static Logger logger = LogManager.getLogger();

	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserMasterRepository userMasterRepository;
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private LdapUserAuthProvider ldapUserAuthProvider;
	
	@Autowired
	private RSAUtil rsaUtil;
	
	@Autowired
	private JwtConfig jwtConfig;
	
    @Autowired
    public KavachServicesImpl(PasswordEncoder passwordEncoder) {
    	logger.trace("Entry into KavachServicesImpl");
        this.passwordEncoder = passwordEncoder;
        logger.trace("Exit from KavachServicesImpl");
    }
	
	@Override
	public ApplicationUser selectApplicationUserByUsername(String orgUserName, String customizedName,String password) {
		logger.trace("Entry into selectApplicationUserByUsername");
		return getUserByUserName(orgUserName,customizedName,password);
	}
	
	
	private ApplicationUser getUserByUserName(String orgUserName, String customizedName,String password) {
		logger.trace("Entry into getUserByUserName");
		
//		UserMaster userDetails=userMasterRepository.findByUserNameAndStatus(orgUserName,1L);
		UserMaster userDetails=userMasterRepository.findByUserName(orgUserName);
		
		
		if (userDetails != null) {
			
			if(userDetails.getStatus()==0)
			{
				throw new UserManagementException(jwtConfig.getDisableUser());
			}

			compareDateAndTime(userDetails.getBlockedTill(), jwtConfig.getBlockUserMessage());

			if (userDetails.getIsLDAPUser() == 0L) {
				isPasswordExpired(userDetails.getPassExpired(), jwtConfig.getPassExpiredMessage());
			}
			
			String orgPass = null;
			try {
				orgPass=rsaUtil.decrypt(password);
			} catch (Exception e) {
				logger.error("error -In  AuthServiceImpl login "+e.getMessage());
				loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGIN_TYPE, orgUserName, request.getRemoteAddr(), "", "Invalid encrypted value for PassCode");
				throw new AuthManagementException("Error while decrypting passcode");
			}
			if(userDetails.getIsLDAPUser()==1L) {
				if(!ldapUserAuthProvider.authenticatUser(orgUserName, orgPass)) {
					unSuccessAttempt(userDetails);
					throw new AuthManagementException("Invalid UserName or Password!");
				}
				
			}else {
				if(!HashingAlgorithm.encryptThisString(orgPass).equals(userDetails.getPassCode())) {
					unSuccessAttempt(userDetails);
					throw new AuthManagementException("Invalid UserName or Password!");
				}
			}
			String role = "";
			List<RoleMastDetails> roles=userDetails.getRolesDetails();
			if(roles!=null && roles.size()>0) {
				RoleMastDetails userRole=roles.get(0);
				role = userRole.getRoleName();
				if(jwtConfig.getFirstUserRole().equals(role)) {
					if(!jwtConfig.getFirstUserIpAddressToAccess().equals(request.getRemoteAddr())){
						logger.error("UserNotfound Or Inactive "+orgUserName);
						loggerUtil.printFailedLogs(logger,ReBITUserManagementConstant.LOGIN_TYPE, orgUserName, request.getRemoteAddr(), "", "This ipAddress not allowed to access role " + role);
						throw new UserManagementException(orgUserName+" can only login with specific machine ");
					}
				}
			}
			successAttempt(userDetails);
			return new ApplicationUser(customizedName,passwordEncoder.encode(password),getRoleByUserName(role),true,true,true,true);
		}else {
			logger.error("UserNotfound Or Inactive "+orgUserName);
			loggerUtil.printFailedLogs(logger,ReBITUserManagementConstant.LOGIN_TYPE, orgUserName, request.getRemoteAddr(), "", "UserNotfound Or Inactive");
			throw new AuthManagementException("UserNotfound Or Inactive "+orgUserName);
		}
	}
	
	
	private Set<SimpleGrantedAuthority> getRoleByUserName(String role){
		logger.trace("Entry into getRoleByUserName");
		Set<SimpleGrantedAuthority> roles = new HashSet<SimpleGrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_"+role));
		logger.trace("Exit from getRoleByUserName");
		return roles;
	}

	@Override
	public UserMaster getUserDetails(String userName) {
		logger.trace("Entry into getUserDetails");
		return userMasterRepository.findByUserNameAndStatus(userName,1L);
	}

	
	private void unSuccessAttempt(UserMaster userDetails) {
		userDetails.setUnSuccessAttempt(lastUnsuccessAttempt(userDetails.getLastFailedAttempt(),userDetails.getUnSuccessAttempt()+1));
		userDetails.setLastFailedAttempt(new Date());
		if(jwtConfig.getUserBlockAfterNumberOfUnSuccesAttempt()<=userDetails.getUnSuccessAttempt()) {
			userDetails.setUnSuccessAttempt(0L);
			userDetails.setBlockedTill(blockForNext(jwtConfig.getUserWillBlockForNextSecond()));
			userMasterRepository.save(userDetails);
			throw new AuthManagementException(jwtConfig.getBlockUserMessage());
		}else {
			userMasterRepository.save(userDetails);
		}
		
	}
	
	private void successAttempt(UserMaster userDetails) {
		userDetails.setUnSuccessAttempt(0L);
		userMasterRepository.save(userDetails);
	}
	
	private Date blockForNext(int second) {
    	logger.debug("entry -  blockForNext");
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.SECOND, second);
        logger.debug("exit -  blockForNext");
        return c.getTime();
    }
	
	
	private void compareDateAndTime(Date blockTill,String exceptionMessage){
		if(blockTill!=null) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date dt = new Date();
		Date currentDate;
		Date blockTime;
		try {
			currentDate = sdf.parse(sdf.format(dt));
			blockTime = sdf.parse(sdf.format(blockTill));
		} catch (ParseException e) {
			logger.error(e.getMessage());
			throw new UserManagementException("Internal Error, Please contact application admin");
		}
		if (currentDate.compareTo(blockTime) < 0) {
			throw new UserManagementException(exceptionMessage);
		}
		}
	}
	
	private void isPasswordExpired(Date passExpired,String exceptionMessage){
		if(passExpired!=null) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date dt = new Date();
		Date currentDate;
		Date blockTime;
		try {
			currentDate = sdf.parse(sdf.format(dt));
			blockTime = sdf.parse(sdf.format(passExpired));
		} catch (ParseException e) {
			logger.error(e.getMessage());
			throw new UserManagementException("Internal Error, Please contact application admin");
		}
		if (currentDate.compareTo(blockTime) > 0) {
			throw new UserManagementException(exceptionMessage);
		}
		}
	}
	
	private Long lastUnsuccessAttempt(Date lastUnsuccessAttempt,Long attempt){
		if(lastUnsuccessAttempt!=null) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date dt = new Date();
		Date currentDate;
		Date lastUnsuccessAttemptTime;
		try {
			currentDate = sdf.parse(sdf.format(dt));
			lastUnsuccessAttemptTime = sdf.parse(sdf.format(addConfigTimeInlastUnsuccessAttempt(jwtConfig.getTimeSessionForUnSuccessAttemptInSecond(),lastUnsuccessAttempt)));
		} catch (ParseException e) {
			logger.error(e.getMessage());
			throw new UserManagementException("Internal Error, Please contact application admin");
		}
		if (currentDate.compareTo(lastUnsuccessAttemptTime) > 0) {
			return 1L;
		}
		}
		return attempt;
	}
	
	private Date addConfigTimeInlastUnsuccessAttempt(int second,Date date) {
    	logger.debug("entry -  blockForNext");
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date currentDate = date;
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.SECOND, second);
        logger.debug("exit -  blockForNext");
        return c.getTime();
    }
	

}
