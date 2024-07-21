package org.rebit.auth.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.config.GlobalProperties;
import org.rebit.auth.config.LdapUserAuthProvider;
import org.rebit.auth.entity.ApiMasterDetails;
import org.rebit.auth.entity.ApplicationProperties;
import org.rebit.auth.entity.AuthManagementProperties;
import org.rebit.auth.entity.OTPLogsEntity;
import org.rebit.auth.entity.RoleMastDetails;
import org.rebit.auth.entity.TokenDetails;
import org.rebit.auth.entity.UserMaster;
import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.gateway.ExternalSystemCall;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.jwt.JwtTokenVerifierForOtherApp;
import org.rebit.auth.mapper.AuthMapper;
import org.rebit.auth.model.ApiAndRolesDto;
import org.rebit.auth.model.ApiAndRolesResponseDto;
import org.rebit.auth.model.KeyAndCaptchaDto;
import org.rebit.auth.model.LogOutDto;
import org.rebit.auth.model.OtpResponseDto;
import org.rebit.auth.model.PermitToAllDto;
import org.rebit.auth.model.RefreshTokenWithExpDate;
import org.rebit.auth.model.StatusAndMessage;
import org.rebit.auth.model.SynchDto;
import org.rebit.auth.model.TokenAndUri;
import org.rebit.auth.model.TokenResponse;
import org.rebit.auth.model.UserInfo;
import org.rebit.auth.model.UsernameAndPasswordDto;
import org.rebit.auth.repository.ApiMasterDetailsRepository;
import org.rebit.auth.repository.ApplicationPropertiesRepository;
import org.rebit.auth.repository.AuthManagementPropertiesRepository;
import org.rebit.auth.repository.OTPServiceRepository;
import org.rebit.auth.repository.TokenDetailsRepository;
import org.rebit.auth.repository.UserMasterRepository;
import org.rebit.auth.service.ApplicationUserServices;
import org.rebit.auth.service.AuthServices;
import org.rebit.auth.service.RunTimeConfigrationChangeService;
import org.rebit.auth.util.AlgorithmAndDateUtil;
import org.rebit.auth.util.AuthKavachUtil;
import org.rebit.auth.util.LoggerUtil;
import org.rebit.auth.util.RSAUtil;
import org.rebit.auth.util.ReBITUserManagementConstant;
import org.rebit.auth.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.github.cage.Cage;
import com.github.cage.GCage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AuthServiceImpl implements AuthServices {

	final static Logger logger = LogManager.getLogger();
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtConfig jwtConfig;
	@Autowired
	private AuthKavachUtil authKavachUtil;
	@Autowired
	private ApplicationUserServices applicationUserServices;
	@Autowired
	private JwtConfig configration;
	@Autowired
	private SecretKey secretKey;
	@Autowired
	private TokenDetailsRepository tokenDetailsRepository;
	@Autowired
	private JwtTokenVerifierForOtherApp jwtTokenVerifierForOtherApp;
	@Autowired
	private AuthMapper authMapper;
	@Autowired
	private ApiMasterDetailsRepository apiMasterDetailsRepository;
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ValidationUtil ValidationUtil;
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	
	@Autowired
	private UserMasterRepository userMasterRepository;
	
	@Autowired
	private ExternalSystemCall externalSystemCall;
	
	@Autowired
	private OTPServiceRepository otpServiceRepository;
	
	@Autowired
	private RSAUtil rsaUtil;
	
	@Autowired
	private LdapUserAuthProvider ldapUserAuthProvider;
	
//	@Value("${otp.sms.integrated}")
//	private String otpSmsIntegrated;
	
	@Autowired
	private ApplicationPropertiesRepository applicationPropertiesRepository;
	
	@Autowired
	private GlobalProperties globalProperties;
	
	@Autowired
	private RunTimeConfigrationChangeService runTimeConfigrationChangeService;
	
	@Autowired
	private AuthManagementPropertiesRepository authManagementPropertiesRepository;

	@Override
	public TokenResponse jwtTokenByRefreshToken(String refreshToken) {
		logger.info("Entry into jwtTokenByRefreshToken");
		List<TokenDetails> tokens = tokenDetailsRepository.findByTokenAndType(refreshToken, "refesh");

		if (tokens != null && !tokens.isEmpty()) {
			TokenDetails token = tokens.get(0);
			if (authKavachUtil.compareDateWithPresentDate(token.getExpirationDate())) {
				UserMaster userDetails = userMasterRepository.findByUserNameAndStatus(token.getUserName(),1L);
				if (userDetails != null) {
					List<RoleMastDetails> roles=userDetails.getRolesDetails();
					if (!(roles != null && roles.size()>0)) {
						loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.GET_JWT_TOKEN_BY_REFRESH_TOKEN_FAILED, token.getUserName(), request.getRemoteAddr(), refreshToken, "Role Not present for user " + token.getUserName());
						logger.error("Role Not present for user " + token.getUserName());
						throw new UserManagementException("Role Not present for user " + token.getUserName());
					}
					RoleMastDetails role = roles.get(0);
					if (role == null) {
						loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.GET_JWT_TOKEN_BY_REFRESH_TOKEN_FAILED, token.getUserName(), request.getRemoteAddr(), refreshToken, "Role Not present for user " + token.getUserName());
						logger.error("Role Not present for user " + token.getUserName());
						throw new UserManagementException("Role Not present for user " + token.getUserName());
					}
					String jwtToken = authKavachUtil.generateJwtToken(token.getUserName(),
							getRoleByUserName(role.getRoleName()), configration.getTokenExpirationAfterSeconds(),
							secretKey, token.getExpirationDate());
					TokenResponse tokenResponse = new TokenResponse();
					tokenResponse.setJwtToken(jwtToken);
					tokenResponse.setRefreshToken(refreshToken);
					loggerUtil.printSuccessLogs(logger, ReBITUserManagementConstant.GET_JWT_TOKEN_BY_REFRESH_TOKEN_SUCCESS, token.getUserName(), request.getRemoteAddr(), refreshToken, "refreshToken validated successfully!");
					logger.info("Exit from jwtTokenByRefreshToken");
					return tokenResponse;
				} else {
					loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.GET_JWT_TOKEN_BY_REFRESH_TOKEN_FAILED, token.getUserName(), request.getRemoteAddr(), refreshToken, "User not Found " + token.getUserName());
					logger.info("Exit from jwtTokenByRefreshToken");
					throw new UserManagementException("User not Found " + token.getUserName());
				}
			} else {
				loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.GET_JWT_TOKEN_BY_REFRESH_TOKEN_FAILED, token.getUserName(), request.getRemoteAddr(), refreshToken, "Refresh Token Expired");
				logger.error("Exit from jwtTokenByRefreshToken");
				throw new UserManagementException("Refresh Token Expired");
			}

		} else {
			logger.info("Exit from jwtTokenByRefreshToken");
			loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.GET_JWT_TOKEN_BY_REFRESH_TOKEN_FAILED, "", request.getRemoteAddr(), refreshToken, "Invalid Refresh Token " + refreshToken);
			throw new UserManagementException("Invalid Refresh Token " + refreshToken);
		}
	}

	private Set<SimpleGrantedAuthority> getRoleByUserName(String role) {
		logger.info("Entry into getRoleByUserName");
		Set<SimpleGrantedAuthority> roles = new HashSet<SimpleGrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_" + role));
		logger.info("Exit from getRoleByUserName");
		return roles;
	}

	@Override
	public TokenResponse login(UsernameAndPasswordDto usernameAndPasswordDto) {
		logger.info("Entry into login");
		boolean captcha=jwtConfig.isCapthaEnable();
		boolean otp=jwtConfig.isOtpEnable();
		
		if (otp && !StringUtils.isEmpty(usernameAndPasswordDto.getToken())) {
			String orgUserName = getUserNameByEncUserName(usernameAndPasswordDto.getUserName());
			if(StringUtils.isEmpty(usernameAndPasswordDto.getToken())){
				logger.error("if otp enable otpToken cannot be empty");
				throw new UserManagementException("if otp enable otpToken cannot be empty");
			}
			if(StringUtils.isEmpty(usernameAndPasswordDto.getTokenValue())){
				logger.error("if otp enable otpValue cannot be empty");
				throw new UserManagementException("if otp enable otpValue cannot be empty");
			}
			captcha = false;
			validateOTPToken(usernameAndPasswordDto.getToken(), usernameAndPasswordDto.getTokenValue(), orgUserName);
		}
		
		if(captcha) {
			if(StringUtils.isEmpty(usernameAndPasswordDto.getCaptchaToken())){
				logger.error("if captch enable captchaToken cannot be empty");
				throw new UserManagementException("if captch enable captchaToken cannot be empty");
			}
			if(StringUtils.isEmpty(usernameAndPasswordDto.getCaptchaValue())){
				logger.error("if captch enable captchaValue cannot be empty");
				throw new UserManagementException("if captch enable captchaValue cannot be empty");
			}
			validateCaptchaToken(usernameAndPasswordDto.getCaptchaToken(), usernameAndPasswordDto.getCaptchaValue(),usernameAndPasswordDto.getUserName());
		}
		
		try {
			String customizeUserName = usernameAndPasswordDto.getUserName() + "separator"
					+ usernameAndPasswordDto.getPassword();
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(customizeUserName, usernameAndPasswordDto.getPassword()));
		} catch (BadCredentialsException e) {
			logger.error("error -In  AuthServiceImpl login "+e);
			loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGIN_TYPE, getUserNameByEncUserName(usernameAndPasswordDto.getUserName()), request.getRemoteAddr(), "", "Invalid UserName Or Password!");
			throw new AuthManagementException("Invalid UserName Or Password!");
		} catch (Exception e) {
			logger.error("error -In  AuthServiceImpl login "+ e);
			if(e.getCause() instanceof UserManagementException) {
				loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGIN_TYPE, getUserNameByEncUserName(usernameAndPasswordDto.getUserName()), request.getRemoteAddr(), "", e.getMessage());
				throw new UserManagementException(e.getMessage());
			}else {
				loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGIN_TYPE, getUserNameByEncUserName(usernameAndPasswordDto.getUserName()), request.getRemoteAddr(), "", "Invalid UserName Or Password!");
			}
			throw new AuthManagementException("Invalid UserName Or Password!");
		}
		
		if(otp && StringUtils.isEmpty(usernameAndPasswordDto.getToken())) {
			TokenResponse token = new TokenResponse();
			String orgUserName = getUserNameByEncUserName(usernameAndPasswordDto.getUserName());
			UserMaster userDetails = applicationUserServices.getUserDetails(orgUserName);
			token.setUserName(usernameAndPasswordDto.getUserName());
			token.setPassword(usernameAndPasswordDto.getPassword());
			token.setIsOtpEnable(jwtConfig.isOtpEnable());
			OtpResponseDto  otpResponseDto= processOtp(Long.valueOf(userDetails.getUserMobileNo()), orgUserName);
			token.setTokenObj(otpResponseDto);
			return token;
		}
		
		String orgUserName = getUserNameByEncUserName(usernameAndPasswordDto.getUserName());
		UserMaster userDetails = applicationUserServices.getUserDetails(orgUserName);
		RefreshTokenWithExpDate refreshToken = getRefeshToken(orgUserName);
		String jwtToken = authKavachUtil.generateJwtToken(orgUserName,
				getRoleByUserName(getRolesOfUser(userDetails.getRolesDetails())),
				jwtConfig.getTokenExpirationAfterSeconds(), secretKey, refreshToken.getExptime());
		TokenResponse token = new TokenResponse();
		token.setJwtToken(jwtToken);
		token.setRefreshToken(refreshToken.getRefeshToken());
		token.setIsOtpEnable(jwtConfig.isOtpEnable());
		loggerUtil.printSuccessLogs(logger,ReBITUserManagementConstant.LOGIN_TYPE, orgUserName, request.getRemoteAddr(), "", "User present and successfully authenticated");
		userDetails.setLastLoginDate(new Date());
		userMasterRepository.save(userDetails);
		logger.info("Exit from login");
		return token;
	}

	private String getRolesOfUser(List<RoleMastDetails> roleMastDetails) {
		logger.info("Entry into getRolesOfUser");
		String role = "";
		if (roleMastDetails != null && roleMastDetails.size()>0) {
			RoleMastDetails roleDetails=roleMastDetails.get(0);
			role = roleDetails.getRoleName();
		}
		logger.info("Exit from getRolesOfUser");
		return role;
	}

	private RefreshTokenWithExpDate getRefeshToken(String orgUserName) {
		logger.info("Entry into getRefeshToken");
		
		List<TokenDetails> tokens = tokenDetailsRepository.findByUserNameAndType(orgUserName, "refesh");
		if (tokens != null && !tokens.isEmpty()) {
			List<TokenDetails> toDelete = new ArrayList<TokenDetails>();
			AtomicInteger userAllowedNumber = new AtomicInteger(0);
			for (Iterator iterator = tokens.iterator(); iterator.hasNext();) {
				TokenDetails tokenDetails = (TokenDetails) iterator.next();
				if (!authKavachUtil.compareDateWithPresentDate(tokenDetails.getExpirationDate())) {
					toDelete.add(tokenDetails);
				}else {
					userAllowedNumber.incrementAndGet();
				}
			}
			if(jwtConfig.getAllowedConcurrentUserLogin()!=0 && userAllowedNumber.get()>=jwtConfig.getAllowedConcurrentUserLogin()) {
				String message = jwtConfig.getAllowedConcurrentUserLogin()+" user with UserName "+orgUserName+" is already logged in, at a time only "+jwtConfig.getAllowedConcurrentUserLogin()+" user allow to login with same UserName";
				loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGIN_TYPE, orgUserName, request.getRemoteAddr(), "", message);
				logger.info(message);
				throw new UserManagementException(message);
			}
			tokenDetailsRepository.deleteAll(toDelete);
		}
		Date timeObject = new Date();
		int milliseconds = jwtConfig.getRefreshTokenExpirationAfterSeconds() * 1000;
		timeObject = new Date(timeObject.getTime() + milliseconds);
		String refeshToken = generateRandomString(25);
		TokenDetails refeshTokenDetails = new TokenDetails();
		refeshTokenDetails.setCreatedDate(new Date());
		refeshTokenDetails.setExpirationDate(timeObject);
		refeshTokenDetails.setToken(refeshToken);
		refeshTokenDetails.setType("refesh");
		refeshTokenDetails.setUserName(orgUserName);
		tokenDetailsRepository.save(refeshTokenDetails);
		RefreshTokenWithExpDate dto = new RefreshTokenWithExpDate();
		dto.setRefeshToken(refeshToken);
		dto.setExptime(timeObject);
		logger.info("Exit from getRefeshToken");
		return dto;
	}

	private String generateRandomString(int length) {
		logger.info("Entry into generateRandomString");
		String possibleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i += 1) {
			int position = (int) Math.floor(Math.random() * possibleChars.length());
			result.append(possibleChars.charAt(position));
		}
		logger.info("Exit from generateRandomString");
		return result.toString();
	}

	@Override
	public StatusAndMessage verifyUri(TokenAndUri tokenAndUri) {
		logger.info("Entry into verifyUri");
		
		if(!tokenAndUri.getHandShakeToken().equals(jwtConfig.getExternalSystemHandShakeToken())) {
			logger.info("INVALID_HANDSHAKE_TOKEN");
			return new StatusAndMessage(HttpStatus.FORBIDDEN, "INVALID_HANDSHAKE_TOKEN");
		}
		
		if(jwtConfig.isAllowPreflightRequest() && tokenAndUri.getMethodType().equalsIgnoreCase("OPTIONS")) {
			logger.info("PREFLIGHT_REQUEST_ALLOWED_IN_SYSTEM");
			return new StatusAndMessage(HttpStatus.OK, "PREFLIGHT_REQUEST_ALLOWED_IN_SYSTEM");
		}
		
		String message = "PUBLIC_URI_AUTHORIZED";
		Set<String> rolesOfToken = jwtTokenVerifierForOtherApp.validate(tokenAndUri.getToken(),tokenAndUri.getIpAddress());

		List<ApiMasterDetails> listOfApis=globalProperties.findByMethodType(tokenAndUri.getMethodType());
		for (ApiMasterDetails apiMasterDetails 	: listOfApis) {

			if (!message.equals("AUTHORIZED")) {
				AntPathMatcher matcher = new AntPathMatcher();
				String dbUri=apiMasterDetails.getUri();
				if(!StringUtils.isEmpty(tokenAndUri.getContextPath())) {
					dbUri=tokenAndUri.getContextPath()+dbUri;
				}
				if (matcher.match(dbUri, tokenAndUri.getApiUri())) {
					if(apiMasterDetails.getPermitAll()) {
						return new StatusAndMessage(HttpStatus.OK, "PERMIT_TO_ALL_IN_SYSTEM");
					}
					message = "UNAUTHORIZED";
					List<RoleMastDetails> rolesDetails=apiMasterDetails.getRolesDetails();
					for (RoleMastDetails roleMastDetail : rolesDetails) {
						if (!message.equals("AUTHORIZED") && rolesOfToken!=null && !CollectionUtils.isEmpty(rolesOfToken)) {
							if (rolesOfToken.contains("ROLE_"+roleMastDetail.getRoleName())) {
								message = "AUTHORIZED";
								break;
							}
						}
					}
				}
			}else {
				break;
			}
		}
		HttpStatus httpStatus = null;
		if(message.equals("AUTHORIZED")) {
			loggerUtil.printSuccessLogs(logger, ReBITUserManagementConstant.AUTHENTICITY_CHECK_AUTHORIZED, ValidationUtil.getUserInfoFromTokenForLogin(tokenAndUri.getToken()).getUserId(), request.getRemoteAddr(), tokenAndUri.getApiUri(), "AUTHORIZED!");
			logger.info("AUTHORIZED OK");
			httpStatus=HttpStatus.OK;
		}else if (message.equals("UNAUTHORIZED")) {
			loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.AUTHENTICITY_CHECK_UNAUTHORIZED, ValidationUtil.getUserInfoFromTokenForLogin(tokenAndUri.getToken()).getUserId(), request.getRemoteAddr(), tokenAndUri.getApiUri(), "UNAUTHORIZED!");
			logger.info("UNAUTHORIZED");
			httpStatus=HttpStatus.FORBIDDEN;
		}else if(message.equals("PUBLIC_URI_AUTHORIZED")){
			logger.info("PUBLIC_URI_AUTHORIZED");
			if(jwtConfig.isAllUrlPublicOfExternalSystem()) {
				loggerUtil.printSuccessLogs(logger, ReBITUserManagementConstant.AUTHENTICITY_CHECK_AUTHORIZED, ValidationUtil.getUserInfoFromTokenForLogin(tokenAndUri.getToken()).getUserId(), request.getRemoteAddr(), tokenAndUri.getApiUri(), "PUBLIC_URI_AUTHORIZED");
				logger.info("PUBLIC_URI_AUTHORIZED isAllUrlPublicOfExternalSystem");
			httpStatus=HttpStatus.OK;
			}else {
				loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.AUTHENTICITY_CHECK_UNAUTHORIZED, tokenAndUri.getToken(), ValidationUtil.getUserInfoFromTokenForLogin(tokenAndUri.getToken()).getUserId(), tokenAndUri.getApiUri(), "URI_NOT_CONFIGURE_IN_SYSTEM UNAUTHORIZED!");
				logger.info("URI_NOT_CONFIGURE_IN_SYSTEM");
				message="URI_NOT_CONFIGURE_IN_SYSTEM";
				httpStatus=HttpStatus.FORBIDDEN;
			}
		}
		logger.info("Exit from verifyUri");
		return new StatusAndMessage(httpStatus, message);
	}

	@Override
	public void apiAndRolesMaping(@Valid ApiAndRolesDto apiAndRolesDto) {
		logger.info("Entry into apiAndRolesMaping");
		authMapper.mapApiToRoles(apiAndRolesDto);
		logger.info("Exit from apiAndRolesMaping");
	}

	@Override
	public List<ApiAndRolesResponseDto> retrieveAndRolesMaping() {
		logger.info("Entry into retrieveAndRolesMaping");
		List<ApiAndRolesResponseDto> list=authMapper.mapRetrieveAndRolesMaping();
		logger.info("Exit from retrieveAndRolesMaping");
		return list;
	}

	@Override
	public void apiPermitAll(PermitToAllDto permitToAllDto) {
		logger.info("Entry into apiPermitAll");
		authMapper.mapPermitToAll(permitToAllDto);
		logger.info("Exit from apiPermitAll");
	}
	
	public String getUserNameByEncUserName(String encUserName){
		logger.info("Entry into getUserNameByEncUserName");
		String orgUserName = null;
		try {
			orgUserName = rsaUtil.decrypt(encUserName);
		} catch (Exception e) {
			logger.error("error -In  AuthServiceImpl login "+e.getMessage());
			loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGIN_TYPE, encUserName, request.getRemoteAddr(), "", "Invalid encrypted value for UserName");
			throw new AuthManagementException("Error while decrypting userName");
		}
		logger.info("Exit from getUserNameByEncUserName");
		return orgUserName;
	}

	@Override
	public void logOutUser(LogOutDto logOutDto,String authorization) {
		logger.info("Entry into getUserNameByEncUserName");
		if(StringUtils.isEmpty(authorization)) {
			logger.error("JWT Token is required");
			loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGOUT_TYPE, "", request.getRemoteAddr(), "", "JWT Token is required");
		}
		UserInfo user=ValidationUtil.getUserInfoFromToken(authorization);
		List<TokenDetails> tokens = tokenDetailsRepository.findByTokenAndType(logOutDto.getRefreshToken(), "refesh");
		if (tokens != null && !tokens.isEmpty()) {
			if(!user.getUserId().equalsIgnoreCase(tokens.get(0).getUserName())){
				loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGOUT_TYPE, user.getUserId(), request.getRemoteAddr(), "", "you are not authorized to logout other user, your user id is "+user.getUserId()+" and you are trying to logout "+tokens.get(0).getUserName());
				throw new AuthManagementException("you are not authorized this user");
			}else {
				tokenDetailsRepository.deleteAll(tokens);
				loggerUtil.printSuccessLogs(logger, ReBITUserManagementConstant.LOGOUT_TYPE, user.getUserId(), request.getRemoteAddr(), "", "Successfully logout");
			}
		}else {
			logger.error("Invalid Refresh Token");
			loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.LOGOUT_TYPE, user.getUserId(), request.getRemoteAddr(), "", "Invalid Refresh Token or you already logged out!");
			throw new AuthManagementException("Invalid Refresh Token or you already logged out!");
		}
		logger.info("Exit from getUserNameByEncUserName");
	
	}

	@Override
	public ResponseEntity<Object> captcha() throws FileNotFoundException, IOException, Exception {
		logger.debug("start -  captchaV2");
		Cage cage = new GCage();
		String fileName="captcha.jpg";
		String imageValue=null;
    	String token=null;
        OutputStream os = new FileOutputStream(fileName, false);
        try {
        	imageValue= cage.getTokenGenerator().next();
        	token=AlgorithmAndDateUtil.encrypt(AlgorithmAndDateUtil.tokenExpireDate(jwtConfig.getCaptchaExpireInSeconds())+imageValue,jwtConfig.getCaptchaEncryptionKey());
        int width = 160, height = 35;
  		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.OPAQUE);
  		Graphics graphics = bufferedImage.createGraphics();
  		graphics.setFont(new Font("Arial", Font.BOLD, 20));
  		graphics.setColor(new Color(255, 255, 255));
  		graphics.fillRect(0, 0, width, height);
  		graphics.setColor(new Color(0, 0, 0));
  		graphics.drawString(imageValue, 20, 25);
  		ImageIO.write(bufferedImage, "jpg", os);
        } finally {
          os.close();
        }
   
        File file = new File(fileName);
		
        HttpHeaders headers = ValidationUtil.getHttpHeaders();
        headers.add("Access-Control-Expose-Headers", "token");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-disposition", "attachment; filename="+fileName);
        headers.add("token", token);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));  
     	logger.debug("exit -  captchaV2");
     	return ResponseEntity.ok()
     	            .headers(headers)
     	            .contentLength(file.length())
     	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
     	            .body(resource);
	}
	

	private void validateCaptchaToken(String token, String captchaValue,String userName) {
		logger.debug("start -  validateCaptchaToken");
		try {
			token = AlgorithmAndDateUtil.decrypt(token,jwtConfig.getCaptchaEncryptionKey());
		} catch (Exception e) {
			logger.error(e.getMessage());
			loggerUtil.printFailedLogs(logger,ReBITUserManagementConstant.LOGIN_TYPE, getUserNameByEncUserName(userName), request.getRemoteAddr(), "", "Error while decrypt captcha token");
			throw new UserManagementException("Error while decrypt captcha token");
		}
		if (!(token.substring(19).equals(captchaValue))) {
			loggerUtil.printFailedLogs(logger,ReBITUserManagementConstant.LOGIN_TYPE, getUserNameByEncUserName(userName), request.getRemoteAddr(), "", "Invalid Captcha");
			throw new UserManagementException("Invalid Captcha");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date dt = new Date();
		Date currentDate;
		Date captchaDate;
		try {
			currentDate = sdf.parse(sdf.format(dt));
			captchaDate = sdf.parse(token.substring(0, 19));
		} catch (ParseException e) {
			logger.error(e.getMessage());
			throw new UserManagementException(e.getMessage());
		}
		if (currentDate.compareTo(captchaDate) > 0) {
			loggerUtil.printFailedLogs(logger,ReBITUserManagementConstant.LOGIN_TYPE, getUserNameByEncUserName(userName), request.getRemoteAddr(), "", "Captcha Expired");
			throw new UserManagementException("Captcha Expired");
		}
		logger.debug("exit -  validateCaptchaToken");
	}
	
	private void validateOTPToken(String token, String otpValue,String userName) {
		logger.debug("start -  validateOTPToken");
		try {
			token = AlgorithmAndDateUtil.decrypt(token,jwtConfig.getOtpEncryptionKey());
		} catch (Exception e) {
			logger.error(e.getMessage());
			loggerUtil.printFailedLogs(logger,ReBITUserManagementConstant.LOGIN_TYPE, userName, request.getRemoteAddr(), "", "Error while decrypting otp token");
			throw new UserManagementException("Error while decrypt otp token");
		}
		String[] tokenAndUsername=token.split("separator");
		token=tokenAndUsername[0];
		if(!tokenAndUsername[1].equalsIgnoreCase(userName)) {
			throw new UserManagementException("This OTP is not generated for you!");
		}
		if (!(token.substring(19).equalsIgnoreCase(otpValue))) {
			loggerUtil.printFailedLogs(logger,ReBITUserManagementConstant.LOGIN_TYPE, userName, request.getRemoteAddr(), "", "Invalid OTP");
			throw new UserManagementException("Invalid OTP");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date dt = new Date();
		Date currentDate;
		Date otpDate;
		try {
			currentDate = sdf.parse(sdf.format(dt));
			otpDate = sdf.parse(token.substring(0, 19));
		} catch (ParseException e) {
			logger.error(e.getMessage());
			throw new UserManagementException(e.getMessage());
		}
		if (currentDate.compareTo(otpDate) > 0) {
			loggerUtil.printFailedLogs(logger,ReBITUserManagementConstant.LOGIN_TYPE, userName, request.getRemoteAddr(), "", "OTP Expired");
			throw new UserManagementException("OTP Expired");
		}
		logger.debug("exit -  validateOTPToken");
	}
	
	@Transactional
	public OtpResponseDto processOtp(long mobileNumber, String key) {
		logger.debug("entry in processOtp");
		
		boolean smsIntegrated = false;
		if (jwtConfig.getOtpSmsIntegrated() != null && jwtConfig.getOtpSmsIntegrated().equalsIgnoreCase("true")) {
			smsIntegrated = true;
		}
		String otpValue = new String();
		if(jwtConfig.isOtpDefault()) {
			otpValue="111111";
		}else {
			otpValue=getRandomNumberString();
		}
		String token = null;
		int otpExpiryTime = jwtConfig.getOtpExpireInSeconds();
		try {
			token = AlgorithmAndDateUtil.encrypt(AlgorithmAndDateUtil.tokenExpireDate(otpExpiryTime) + otpValue+"separator"+key,
					jwtConfig.getOtpEncryptionKey());
			if (smsIntegrated) {
				boolean response = runTimeConfigrationChangeService.sendSmsOtpUsingToken(mobileNumber, otpValue,otpExpiryTime+"");
				if (response) {
					OtpResponseDto otpResponse = new OtpResponseDto();
					otpResponse.setToken(token);
					otpResponse.setExpiredTimeInSecond(otpExpiryTime);
					OTPLogsEntity otpEntity = new OTPLogsEntity();
					otpEntity.setMobileNumber(mobileNumber);
					otpEntity.setOtpValue(otpValue);
					otpEntity.setToken(token);
					otpEntity.setKey(key);
					otpServiceRepository.save(otpEntity);
					logger.debug("exit in processOtp success");
					return otpResponse;
				} else {
					logger.debug("exit in processOtp failure" + response);
					throw new UserManagementException("External System Call Failed, Please Contact to Administrator");
				}
			} else {
				OtpResponseDto otpResponse = new OtpResponseDto();
				otpResponse.setToken(token);
				otpResponse.setExpiredTimeInSecond(otpExpiryTime);
				OTPLogsEntity otpEntity = new OTPLogsEntity();
				otpEntity.setMobileNumber(mobileNumber);
				otpEntity.setOtpValue(otpValue);
				otpEntity.setToken(token);
				otpEntity.setKey(key);
				otpServiceRepository.save(otpEntity);
				logger.debug("exit in processOtp success");
				return otpResponse;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UserManagementException("error while generating otp");
		}
	}
	
	private String getRandomNumberString() {
		logger.debug("entry in getRandomNumberString");
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		logger.debug("exit in getRandomNumberString");
		return String.format("%06d", number);
	}
	
	@Override
	public KeyAndCaptchaDto getPublicKey() {
		KeyAndCaptchaDto keyAndCaptchaDto = new KeyAndCaptchaDto();
		keyAndCaptchaDto.setCaptchaEnable(jwtConfig.isCapthaEnable());
		keyAndCaptchaDto.setKey(jwtConfig.getPublicKeyEncryption());
		return keyAndCaptchaDto;
	}

	@Override
	public void getSynch(SynchDto synchDto) {
		try {
			ldapUserAuthProvider.getAllUsersFromLdap(getUserNameByEncUserName(synchDto.getUserName()), getUserNameByEncUserName(synchDto.getPassword()));
		}catch (AuthenticationException ex) {
			ex.printStackTrace();
			logger.error(ex.getLocalizedMessage());
			throw new AuthManagementException("Invalid User or Password!");
		}catch (NamingException e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
			throw new UserManagementException("Error while importing Ldap User!");
		}
	}
	
	@Override
	@EventListener(ApplicationReadyEvent.class)
	public void refreshApplicationProperties() {
		List<ApplicationProperties> propertyList=applicationPropertiesRepository.findAll();
		Map<String,String> set=propertyList.stream().filter(prop->prop.getValue()!=null).collect(Collectors.toMap(ApplicationProperties::getKey, ApplicationProperties::getValue));		
		
		List<AuthManagementProperties> authPropertyList=authManagementPropertiesRepository.findAll();
		Map<String,String> map=authPropertyList.stream().filter(prop->prop.getValue()!=null).collect(Collectors.toMap(AuthManagementProperties::getKey, AuthManagementProperties::getValue));
		
		authMapper.mapApplicationProperties(set);
		authMapper.mapAuthManagementProperties(map);
	}
}
