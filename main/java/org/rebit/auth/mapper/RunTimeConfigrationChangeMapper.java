package org.rebit.auth.mapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.administrator.model.EventEntityLogDto;
import org.rebit.auth.administrator.model.EventEntityWithPage;
import org.rebit.auth.administrator.model.FileDetailDto;
import org.rebit.auth.administrator.model.GlobalPropertiesDto;
import org.rebit.auth.administrator.model.LogFilesNameDto;
import org.rebit.auth.administrator.model.RunTimeConfigrationChangeDto;
import org.rebit.auth.config.GlobalProperties;
import org.rebit.auth.entity.ApplicationProperties;
import org.rebit.auth.entity.AuthManagementProperties;
import org.rebit.auth.entity.EventEntityLogDetails;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.repository.AuthManagementPropertiesRepository;
import org.rebit.auth.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RunTimeConfigrationChangeMapper {
	
	final static Logger logger = LogManager.getLogger();
	
	@Autowired 
	private JwtConfig jwtConfig;
	
	@Autowired 
	private GlobalProperties globalProperties;
	
	@Autowired
	private AuthManagementPropertiesRepository authManagementPropertiesRepository;
	
	public void updateConfig(RunTimeConfigrationChangeDto runTimeConfigrationChangeDto){
		
		List<AuthManagementProperties> propertyList=authManagementPropertiesRepository.findAll();
		List<AuthManagementProperties> propertyNewList = new ArrayList();
		
		Map<String,String> map = getMapToUpdateValue(runTimeConfigrationChangeDto); 
		
		propertyList.forEach(prop -> {
			String value = map.get(prop.getKey());
			if (value != null) {
				prop.setValue(value);
				propertyNewList.add(prop);
			}
		});
		
		authManagementPropertiesRepository.saveAll(propertyNewList);
		
		jwtConfig.setAllowedConcurrentUserLogin(runTimeConfigrationChangeDto.getAllowedConcurrentUserLogin());
		jwtConfig.setApiRateLimitBy(runTimeConfigrationChangeDto.getApiRateLimitBy());
		jwtConfig.setApiRateLimitCommaTime(runTimeConfigrationChangeDto.getApiRateLimit()+","+runTimeConfigrationChangeDto.getApiRateLimit());
		jwtConfig.setBlockUserMessage(runTimeConfigrationChangeDto.getBlockUserMessage());
		jwtConfig.setCapitalCaseLetters(runTimeConfigrationChangeDto.getCapitalCaseLetters());
		jwtConfig.setCaptchaExpireInSeconds(runTimeConfigrationChangeDto.getCaptchaExpireInSeconds());
		//jwtConfig.setContextRootPath(runTimeConfigrationChangeDto.getContextRootPath());
		jwtConfig.setDisableUser(runTimeConfigrationChangeDto.getDisableUser());
		jwtConfig.setExternalSystemHandShakeToken(runTimeConfigrationChangeDto.getExternalSystemHandShakeToken());
		jwtConfig.setFirstUserIpAddressToAccess(runTimeConfigrationChangeDto.getFirstUserIpAddressToAccess());
		jwtConfig.setFirstUserRole(runTimeConfigrationChangeDto.getFirstUserRole());
		jwtConfig.setLastNumberOfPassCanNotUse(runTimeConfigrationChangeDto.getLastNumberOfPassCanNotUse());
		jwtConfig.setLowerCaseLetters(runTimeConfigrationChangeDto.getLowerCaseLetters());
		jwtConfig.setOneNumbers(runTimeConfigrationChangeDto.getOneNumbers()+"");
		jwtConfig.setOtpExpireInSeconds(runTimeConfigrationChangeDto.getOtpExpireInSeconds());
		jwtConfig.setPassExpiredMessage(runTimeConfigrationChangeDto.getPassExpiredMessage());
		jwtConfig.setPasswordExpiredInDays(runTimeConfigrationChangeDto.getPasswordExpiredInDays());
		jwtConfig.setPasswordLength(runTimeConfigrationChangeDto.getPasswordLength());
		jwtConfig.setPasswordResetSessionExpireInSeconds(runTimeConfigrationChangeDto.getPasswordResetSessionExpireInSeconds());
		jwtConfig.setRefreshTokenExpirationAfterSeconds(runTimeConfigrationChangeDto.getRefreshTokenExpirationAfterSeconds());
		jwtConfig.setSpecialCharacters(runTimeConfigrationChangeDto.getSpecialCharacters());
		jwtConfig.setTimeSessionForUnSuccessAttemptInSecond(runTimeConfigrationChangeDto.getTimeSessionForUnSuccessAttemptInSecond());
		jwtConfig.setTokenExpirationAfterSeconds(runTimeConfigrationChangeDto.getTokenExpirationAfterSeconds());
		jwtConfig.setUserBlockAfterNumberOfUnSuccesAttempt(runTimeConfigrationChangeDto.getUserBlockAfterNumberOfUnSuccesAttempt());
		jwtConfig.setUserWillBlockForNextSecond(runTimeConfigrationChangeDto.getUserWillBlockForNextSecond());
		jwtConfig.setAllowPreflightRequest(runTimeConfigrationChangeDto.isAllowPreflightRequest());
		jwtConfig.setAllUrlPublicOfExternalSystem(runTimeConfigrationChangeDto.isAllUrlPublicOfExternalSystem());
		jwtConfig.setCapthaEnable(runTimeConfigrationChangeDto.isCapthaEnable());
		jwtConfig.setOtpDefault(runTimeConfigrationChangeDto.isOtpDefault());
		jwtConfig.setOtpEnable(runTimeConfigrationChangeDto.isOtpEnable());
		
		//sms otp
		jwtConfig.setNotificationSmsUsername(runTimeConfigrationChangeDto.getNotificationSmsUsername());		
		jwtConfig.setNotificationSmsPassword(runTimeConfigrationChangeDto.getNotificationSmsPassword());
		jwtConfig.setNotificationSmsSenderId(runTimeConfigrationChangeDto.getNotificationSmsSenderId());
		jwtConfig.setNotificationSmsTextTemplate(runTimeConfigrationChangeDto.getNotificationSmsTextTemplate());
		jwtConfig.setNotificationSmsUrlEncodeEnable(runTimeConfigrationChangeDto.getNotificationSmsUrlEncodeEnable());
		jwtConfig.setNotificationSmsVendorUrl(runTimeConfigrationChangeDto.getNotificationSmsVendorUrl());
		jwtConfig.setNotificationSmsDeliveryReportUrl(runTimeConfigrationChangeDto.getNotificationSmsDeliveryReportUrl());
		jwtConfig.setNotificationSmsUdh(runTimeConfigrationChangeDto.getNotificationSmsUdh());
		jwtConfig.setNotificationSmsVendorTokenUrl(runTimeConfigrationChangeDto.getNotificationSmsVendorTokenUrl());
		jwtConfig.setNotificationSmsIsVendorUrlPostBody(runTimeConfigrationChangeDto.getNotificationSmsIsVendorUrlPostBody());
		jwtConfig.setNotificationSmsCategory(runTimeConfigrationChangeDto.getNotificationSmsCategory());
		jwtConfig.setNotificationSmsIsVendorUrlGet(runTimeConfigrationChangeDto.getNotificationSmsIsVendorUrlGet());
		jwtConfig.setNeedToUseCustomeExpiryTime(runTimeConfigrationChangeDto.getNeedToUseCustomeExpiryTime());
		jwtConfig.setNotificationSmsTokenExpiryInMinutes(runTimeConfigrationChangeDto.getNotificationSmsTokenExpiryInMinutes());
		jwtConfig.setProxyIp(runTimeConfigrationChangeDto.getProxyIp());
		jwtConfig.setProxyPort(runTimeConfigrationChangeDto.getProxyPort());
		jwtConfig.setSmsOtpOnDevEnv(runTimeConfigrationChangeDto.getSmsOtpOnDevEnv());
		jwtConfig.setOtpSmsIntegrated(runTimeConfigrationChangeDto.getOtpSmsIntegrated());
		//sms otp
		
	}
	
	
	private Map<String, String> getMapToUpdateValue(RunTimeConfigrationChangeDto runTimeConfigrationChangeDto) {
		Map<String,String> map = new HashMap();
		
		map.put("allowedConcurrentUserLogin",runTimeConfigrationChangeDto.getAllowedConcurrentUserLogin()+"");
		map.put("allowPreflightRequest",runTimeConfigrationChangeDto.isAllowPreflightRequest()+"");
		map.put("allUrlPublicOfExternalSystem",runTimeConfigrationChangeDto.isAllUrlPublicOfExternalSystem()+"");
		map.put("apiRateLimitBy",runTimeConfigrationChangeDto.getApiRateLimitBy());
		map.put("apiRateLimitCommaTime",runTimeConfigrationChangeDto.getApiRateLimit()+","+runTimeConfigrationChangeDto.getApiRateInSecondTime());
		map.put("blockUserMessage",runTimeConfigrationChangeDto.getBlockUserMessage());
		map.put("capitalCaseLetters",runTimeConfigrationChangeDto.getCapitalCaseLetters());
		//map.put("captchaEncryptionKey",runTimeConfigrationChangeDto.getcap);
		map.put("captchaExpireInSeconds",runTimeConfigrationChangeDto.getCaptchaExpireInSeconds()+"");
		map.put("capthaEnable",runTimeConfigrationChangeDto.isCapthaEnable()+"");
		//map.put("contextRootPath",runTimeConfigrationChangeDto.getcon);
		map.put("disableUser",runTimeConfigrationChangeDto.getDisableUser());
		map.put("externalSystemHandShakeToken",runTimeConfigrationChangeDto.getExternalSystemHandShakeToken());
		map.put("firstUserIpAddressToAccess",runTimeConfigrationChangeDto.getFirstUserIpAddressToAccess());
		map.put("firstUserRole",runTimeConfigrationChangeDto.getFirstUserRole());
		map.put("lastNumberOfPassCanNotUse",runTimeConfigrationChangeDto.getLastNumberOfPassCanNotUse()+"");
		//map.put("logFilesLocation",runTimeConfigrationChangeDto);
		map.put("lowerCaseLetters",runTimeConfigrationChangeDto.getLowerCaseLetters());
		map.put("needToUseCustomeExpiryTime",runTimeConfigrationChangeDto.getNeedToUseCustomeExpiryTime());
		map.put("notificationSmsCategory",runTimeConfigrationChangeDto.getNotificationSmsCategory());
		map.put("notificationSmsDeliveryReportUrl",runTimeConfigrationChangeDto.getNotificationSmsDeliveryReportUrl());
		map.put("notificationSmsIsVendorUrlGet",runTimeConfigrationChangeDto.getNotificationSmsIsVendorUrlGet());
		map.put("notificationSmsIsVendorUrlPostBody",runTimeConfigrationChangeDto.getNotificationSmsIsVendorUrlPostBody());
		map.put("notificationSmsPassword",runTimeConfigrationChangeDto.getNotificationSmsPassword());
		map.put("notificationSmsSenderId",runTimeConfigrationChangeDto.getNotificationSmsSenderId());
		map.put("notificationSmsTextTemplate",runTimeConfigrationChangeDto.getNotificationSmsTextTemplate());
		map.put("notificationSmsTokenExpiryInMinutes",runTimeConfigrationChangeDto.getNotificationSmsTokenExpiryInMinutes());
		map.put("notificationSmsUdh",runTimeConfigrationChangeDto.getNotificationSmsUdh());
		map.put("notificationSmsUrlEncodeEnable",runTimeConfigrationChangeDto.getNotificationSmsUrlEncodeEnable());
		map.put("notificationSmsUsername",runTimeConfigrationChangeDto.getNotificationSmsUsername());
		map.put("notificationSmsVendorTokenUrl",runTimeConfigrationChangeDto.getNotificationSmsVendorTokenUrl());
		map.put("notificationSmsVendorUrl",runTimeConfigrationChangeDto.getNotificationSmsVendorUrl());
		map.put("oneNumbers",runTimeConfigrationChangeDto.getOneNumbers()+"");
		map.put("otpDefault",runTimeConfigrationChangeDto.isOtpDefault()+"");
		map.put("otpEnable",runTimeConfigrationChangeDto.isOtpEnable()+"");
		//map.put("otpEncryptionKey",runTimeConfigrationChangeDto.);
		map.put("otpExpireInSeconds",runTimeConfigrationChangeDto.getOtpExpireInSeconds()+"");
		map.put("otpSmsIntegrated",runTimeConfigrationChangeDto.getOtpSmsIntegrated());
		map.put("passExpiredMessage",runTimeConfigrationChangeDto.getPassExpiredMessage());
		map.put("passwordExpiredInDays",runTimeConfigrationChangeDto.getPasswordExpiredInDays()+"");
		map.put("passwordLength",runTimeConfigrationChangeDto.getPasswordLength()+"");
		map.put("passwordResetSessionExpireInSeconds",runTimeConfigrationChangeDto.getPasswordResetSessionExpireInSeconds()+"");
		//map.put("privateKeyEncryption",runTimeConfigrationChangeDto.getprivateKeyEncryption);
		map.put("proxyIp",runTimeConfigrationChangeDto.getProxyIp());
		map.put("proxyPort",runTimeConfigrationChangeDto.getProxyPort()+"");
		//map.put("publicKeyEncryption",runTimeConfigrationChangeDto.getpublicKeyEncryption);
		map.put("refreshTokenExpirationAfterSeconds",runTimeConfigrationChangeDto.getRefreshTokenExpirationAfterSeconds()+"");
		map.put("smsOtpOnDevEnv",runTimeConfigrationChangeDto.getSmsOtpOnDevEnv());
		map.put("specialCharacters",runTimeConfigrationChangeDto.getSpecialCharacters());
		map.put("timeSessionForUnSuccessAttemptInSecond",runTimeConfigrationChangeDto.getTimeSessionForUnSuccessAttemptInSecond()+"");
		map.put("tokenExpirationAfterSeconds",runTimeConfigrationChangeDto.getTokenExpirationAfterSeconds()+"");
		//map.put("tokenPrefix",runTimeConfigrationChangeDto.gettokenPrefix);
		//map.put("tokenRefresh",runTimeConfigrationChangeDto.gettokenRefresh);
		map.put("userBlockAfterNumberOfUnSuccesAttempt",runTimeConfigrationChangeDto.getUserBlockAfterNumberOfUnSuccesAttempt()+"");
		map.put("userWillBlockForNextSecond",runTimeConfigrationChangeDto.getUserWillBlockForNextSecond()+"");	
		
		return map;
	}


	public RunTimeConfigrationChangeDto getConfig(){
		
		RunTimeConfigrationChangeDto runTimeConfigrationChangeDto = new RunTimeConfigrationChangeDto();
		
		runTimeConfigrationChangeDto.setAllowedConcurrentUserLogin(jwtConfig.getAllowedConcurrentUserLogin());
		
		runTimeConfigrationChangeDto.setApiRateLimitBy(jwtConfig.getApiRateLimitBy());
		
		String[] limitCommaTime = jwtConfig.getApiRateLimitCommaTime().split(",");
		
		runTimeConfigrationChangeDto.setApiRateLimit(Integer.parseInt(limitCommaTime[0]));
		
		runTimeConfigrationChangeDto.setApiRateInSecondTime(Integer.parseInt(limitCommaTime[1]));
		
		runTimeConfigrationChangeDto.setBlockUserMessage(jwtConfig.getBlockUserMessage());
		
		runTimeConfigrationChangeDto.setCapitalCaseLetters(jwtConfig.getCapitalCaseLetters());
		
		runTimeConfigrationChangeDto.setCaptchaExpireInSeconds(jwtConfig.getCaptchaExpireInSeconds());
		
		//runTimeConfigrationChangeDto.setContextRootPath(jwtConfig.getContextRootPath());
		
		runTimeConfigrationChangeDto.setDisableUser(jwtConfig.getDisableUser());
		
		runTimeConfigrationChangeDto.setExternalSystemHandShakeToken(jwtConfig.getExternalSystemHandShakeToken());
		
		runTimeConfigrationChangeDto.setFirstUserIpAddressToAccess(jwtConfig.getFirstUserIpAddressToAccess());
		
		runTimeConfigrationChangeDto.setFirstUserRole(jwtConfig.getFirstUserRole());
		
		runTimeConfigrationChangeDto.setLastNumberOfPassCanNotUse(jwtConfig.getLastNumberOfPassCanNotUse());
		
		runTimeConfigrationChangeDto.setLowerCaseLetters(jwtConfig.getLowerCaseLetters());
		
		runTimeConfigrationChangeDto.setOneNumbers(Integer.parseInt(jwtConfig.getOneNumbers()));
		
		runTimeConfigrationChangeDto.setOtpExpireInSeconds(jwtConfig.getOtpExpireInSeconds());
		
		runTimeConfigrationChangeDto.setPassExpiredMessage(jwtConfig.getPassExpiredMessage());
		
		runTimeConfigrationChangeDto.setPasswordExpiredInDays(jwtConfig.getPasswordExpiredInDays());
		
		runTimeConfigrationChangeDto.setPasswordLength(jwtConfig.getPasswordLength());
		
		runTimeConfigrationChangeDto.setPasswordResetSessionExpireInSeconds(jwtConfig.getPasswordResetSessionExpireInSeconds());
		
		runTimeConfigrationChangeDto.setRefreshTokenExpirationAfterSeconds(jwtConfig.getRefreshTokenExpirationAfterSeconds());
		
		runTimeConfigrationChangeDto.setSpecialCharacters(jwtConfig.getSpecialCharacters());
		
		runTimeConfigrationChangeDto.setTimeSessionForUnSuccessAttemptInSecond(jwtConfig.getTimeSessionForUnSuccessAttemptInSecond());
		
		runTimeConfigrationChangeDto.setTokenExpirationAfterSeconds(jwtConfig.getTokenExpirationAfterSeconds());
		
		runTimeConfigrationChangeDto.setUserBlockAfterNumberOfUnSuccesAttempt(jwtConfig.getUserBlockAfterNumberOfUnSuccesAttempt());
		
		runTimeConfigrationChangeDto.setUserWillBlockForNextSecond(jwtConfig.getUserWillBlockForNextSecond());
		
		runTimeConfigrationChangeDto.setAllowPreflightRequest(jwtConfig.isAllowPreflightRequest());
		
		runTimeConfigrationChangeDto.setAllUrlPublicOfExternalSystem(jwtConfig.isAllUrlPublicOfExternalSystem());
		
		runTimeConfigrationChangeDto.setCapthaEnable(jwtConfig.isCapthaEnable());
		
		runTimeConfigrationChangeDto.setOtpDefault(jwtConfig.isOtpDefault());
		
		runTimeConfigrationChangeDto.setOtpEnable(jwtConfig.isOtpEnable());
		
		
		//sms otp
		
		runTimeConfigrationChangeDto.setNotificationSmsUsername(jwtConfig.getNotificationSmsUsername());		
		runTimeConfigrationChangeDto.setNotificationSmsPassword(jwtConfig.getNotificationSmsPassword());
		runTimeConfigrationChangeDto.setNotificationSmsSenderId(jwtConfig.getNotificationSmsSenderId());
		runTimeConfigrationChangeDto.setNotificationSmsTextTemplate(jwtConfig.getNotificationSmsTextTemplate());
		runTimeConfigrationChangeDto.setNotificationSmsUrlEncodeEnable(jwtConfig.getNotificationSmsUrlEncodeEnable());
		runTimeConfigrationChangeDto.setNotificationSmsVendorUrl(jwtConfig.getNotificationSmsVendorUrl());
		runTimeConfigrationChangeDto.setNotificationSmsDeliveryReportUrl(jwtConfig.getNotificationSmsDeliveryReportUrl());
		runTimeConfigrationChangeDto.setNotificationSmsUdh(jwtConfig.getNotificationSmsUdh());
		runTimeConfigrationChangeDto.setNotificationSmsVendorTokenUrl(jwtConfig.getNotificationSmsVendorTokenUrl());
		runTimeConfigrationChangeDto.setNotificationSmsIsVendorUrlPostBody(jwtConfig.getNotificationSmsIsVendorUrlPostBody());
		runTimeConfigrationChangeDto.setNotificationSmsCategory(jwtConfig.getNotificationSmsCategory());
		runTimeConfigrationChangeDto.setNotificationSmsIsVendorUrlGet(jwtConfig.getNotificationSmsIsVendorUrlGet());
		runTimeConfigrationChangeDto.setNeedToUseCustomeExpiryTime(jwtConfig.getNeedToUseCustomeExpiryTime());
		runTimeConfigrationChangeDto.setNotificationSmsTokenExpiryInMinutes(jwtConfig.getNotificationSmsTokenExpiryInMinutes());
		runTimeConfigrationChangeDto.setProxyIp(jwtConfig.getProxyIp());
		runTimeConfigrationChangeDto.setProxyPort(jwtConfig.getProxyPort());
		runTimeConfigrationChangeDto.setSmsOtpOnDevEnv(jwtConfig.getSmsOtpOnDevEnv());
		runTimeConfigrationChangeDto.setOtpSmsIntegrated(jwtConfig.getOtpSmsIntegrated());
		
		//sms otp
		
		return runTimeConfigrationChangeDto;
		
	}

	public GlobalPropertiesDto getGlobalProperties() {
		
		GlobalPropertiesDto globalPropertiesDto = new GlobalPropertiesDto();
		//globalPropertiesDto.setApconnectBackendHost(globalProperties.getApconnectBackendHost());
		globalPropertiesDto.setBaseDn(globalProperties.getBaseDn());
		//globalPropertiesDto.setCorsAllowedHosts(globalProperties.getCorsAllowedHosts());
		globalPropertiesDto.setDnForSearchFilter(globalProperties.getDnForSearchFilter());
		globalPropertiesDto.setDnUsernameAttribute(globalProperties.getDnUsernameAttribute());
		globalPropertiesDto.setDomainName(globalProperties.getDomainName());
		globalPropertiesDto.setEmailAttribute(globalProperties.getEmailAttribute());
		globalPropertiesDto.setFirstnameAttribute(globalProperties.getFirstnameAttribute());
		globalPropertiesDto.setGenerateMobileNumber(globalProperties.getGenerateMobileNumber());
		globalPropertiesDto.setLastnameAttribute(globalProperties.getLastnameAttribute());
		globalPropertiesDto.setLdapUrl(globalProperties.getLdapUrl());
		globalPropertiesDto.setMobileAttribute(globalProperties.getMobileAttribute());
		globalPropertiesDto.setSearchFilterObject(globalProperties.getSearchFilterObject());
		globalPropertiesDto.setUsernameAttribute(globalProperties.getUsernameAttribute());
		
		return globalPropertiesDto;
	}

	public List<ApplicationProperties> updateGlobalProperties(GlobalPropertiesDto globalPropertiesDto) {
		
		//globalProperties.setApconnectBackendHost(globalPropertiesDto.getApconnectBackendHost());
		globalProperties.setBaseDn(globalPropertiesDto.getBaseDn());
		//globalProperties.setCorsAllowedHosts(globalPropertiesDto.getCorsAllowedHosts());
		globalProperties.setDnForSearchFilter(globalPropertiesDto.getDnForSearchFilter());
		globalProperties.setDnUsernameAttribute(globalPropertiesDto.getDnUsernameAttribute());
		globalProperties.setDomainName(globalPropertiesDto.getDomainName());
		globalProperties.setEmailAttribute(globalPropertiesDto.getEmailAttribute());
		globalProperties.setFirstnameAttribute(globalPropertiesDto.getFirstnameAttribute());
		globalProperties.setGenerateMobileNumber(globalPropertiesDto.getGenerateMobileNumber());
		globalProperties.setLastnameAttribute(globalPropertiesDto.getLastnameAttribute());
		globalProperties.setLdapUrl(globalPropertiesDto.getLdapUrl());
		globalProperties.setMobileAttribute(globalPropertiesDto.getMobileAttribute());
		globalProperties.setSearchFilterObject(globalPropertiesDto.getSearchFilterObject());
		globalProperties.setUsernameAttribute(globalPropertiesDto.getUsernameAttribute());
		
		
		List<ApplicationProperties> propertyList= new ArrayList<>();
		propertyList.add(new ApplicationProperties("ldapUrl", globalPropertiesDto.getLdapUrl()));
		propertyList.add(new ApplicationProperties("dnUsernameAttribute", globalPropertiesDto.getDnUsernameAttribute()));
		propertyList.add(new ApplicationProperties("domainName", globalPropertiesDto.getDomainName()));
		propertyList.add(new ApplicationProperties("baseDn", globalPropertiesDto.getBaseDn()));
		propertyList.add(new ApplicationProperties("searchFilterObject", globalPropertiesDto.getSearchFilterObject()));
		propertyList.add(new ApplicationProperties("dnForSearchFilter", globalPropertiesDto.getDnForSearchFilter()));
		propertyList.add(new ApplicationProperties("usernameAttribute", globalPropertiesDto.getUsernameAttribute()));
		propertyList.add(new ApplicationProperties("mobileAttribute", globalPropertiesDto.getMobileAttribute()));
		propertyList.add(new ApplicationProperties("firstnameAttribute", globalPropertiesDto.getFirstnameAttribute()));
		propertyList.add(new ApplicationProperties("lastnameAttribute", globalPropertiesDto.getLastnameAttribute()));
		propertyList.add(new ApplicationProperties("emailAttribute", globalPropertiesDto.getEmailAttribute()));
		propertyList.add(new ApplicationProperties("generateMobileNumber", globalPropertiesDto.getGenerateMobileNumber()));
		//propertyList.add(new ApplicationProperties("corsAllowedHosts", globalPropertiesDto.getCorsAllowedHosts()));
		//propertyList.add(new ApplicationProperties("apconnectBackendHost", globalPropertiesDto.getApconnectBackendHost()));
		return propertyList;
	}

	public LogFilesNameDto getfileNames() {
		LogFilesNameDto logFilesNameDto = new LogFilesNameDto();
		Set<String> fileSet = Stream.of(new File(jwtConfig.getLogFilesLocation()).listFiles())
			      .filter(file -> !file.isDirectory())
			      .map(File::getName)
			      .collect(Collectors.toSet());
		
		Set<FileDetailDto> files = new HashSet<>();
		if(!CollectionUtils.isEmpty(fileSet)) {
			fileSet.forEach(fileName ->{
				files.add(getFileDateAndTime(jwtConfig.getLogFilesLocation()+fileName,fileName));
			});
		}
		logFilesNameDto.setFileNames(files);
		return logFilesNameDto;
	}
	
	private FileDetailDto getFileDateAndTime(String fileFullPath,String fileName){
		
		 File file = new File(fileFullPath);
	     Path filePath = file.toPath();

	        BasicFileAttributes attributes = null;
	        try
	        {
	            attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
	        }
	        catch (IOException exception)
	        {
	        	logger.error("Exception handled when trying to get file attributes: " + exception.getMessage());
	        	throw new UserManagementException("Error while reading file time");
	        }
	      
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date currentDate = new Date();
	        System.out.println(dateFormat.format(currentDate));
	        
	        FileDetailDto fileDetailDto = new FileDetailDto();
	        fileDetailDto.setName(fileName);
	        fileDetailDto.setCreationTime(dateFormat.format(new Date(attributes.creationTime().toMillis())));
	        fileDetailDto.setLastAccessTime(dateFormat.format(new Date(attributes.lastAccessTime().toMillis())));
	        fileDetailDto.setLastModifiedTime(dateFormat.format(new Date(attributes.lastModifiedTime().toMillis())));
	        return fileDetailDto;
	        
	}
	

	public ResponseEntity<Object> download(String fileName)
			throws FileNotFoundException {
		
		File file = new File(jwtConfig.getLogFilesLocation() + "/" + fileName);
		HttpHeaders headers = ValidationUtil.getHttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("Content-disposition", "attachment; filename=" + fileName);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
	
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	public EventEntityWithPage eventEntityLogDetailsToEventEntityLogDto(List<EventEntityLogDetails> eventEntityLogDetails,int totalPage) {
		
		List<EventEntityLogDto> list = new ArrayList();
		eventEntityLogDetails.forEach(eventEntityLog ->{
			EventEntityLogDto eventEntityLogDto = new EventEntityLogDto();
			eventEntityLogDto.setApplicationName(eventEntityLog.getApplicationName());
			eventEntityLogDto.setEventDate(eventEntityLog.getEventDate());
			eventEntityLogDto.setIpAddress(eventEntityLog.getIpAddress());
			eventEntityLogDto.setLevel(eventEntityLog.getLevel());
			eventEntityLogDto.setLogger(eventEntityLog.getLogger());
			eventEntityLogDto.setMessage(eventEntityLog.getMessage());
			eventEntityLogDto.setRequest(eventEntityLog.getRequest());
			eventEntityLogDto.setType(eventEntityLog.getType());
			eventEntityLogDto.setUserId(eventEntityLog.getUserId());
			list.add(eventEntityLogDto);
		});
		
		EventEntityWithPage eventEntityWithPage = new EventEntityWithPage();
		eventEntityWithPage.setRecords(list);
		eventEntityWithPage.setTotalPage(totalPage);
		
		return eventEntityWithPage;
	}
	
}
