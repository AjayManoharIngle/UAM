package org.rebit.auth.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.config.GlobalProperties;
import org.rebit.auth.entity.ApiMasterDetails;
import org.rebit.auth.entity.RoleMastDetails;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.model.ApiAndRolesDto;
import org.rebit.auth.model.ApiAndRolesResponseDto;
import org.rebit.auth.model.PermitToAllDto;
import org.rebit.auth.repository.ApiMasterDetailsRepository;
import org.rebit.auth.repository.RoleMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@Component
public class AuthMapper {
	final static Logger logger = LogManager.getLogger();
	
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	
	@Autowired
	private ApiMasterDetailsRepository apiMasterDetailsRepository;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	@Autowired
	private GlobalProperties globalProperties;

	public void mapApiToRoles(@Valid ApiAndRolesDto apiAndRolesDto) {
		logger.trace("Entry into mapApiToRoles");
		ApiMasterDetails apiMasterDetails=apiMasterDetailsRepository.findByUriAndMethod(apiAndRolesDto.getUri(),apiAndRolesDto.getMethodType());
		
		if(apiMasterDetails!=null) {	
			if (apiMasterDetails.getRolesDetails() != null) {
				apiMasterDetails.getRolesDetails().removeAll(apiMasterDetails.getRolesDetails());
			}
			apiMasterDetailsRepository.delete(apiMasterDetails);
		}
		
		ApiMasterDetails apiMaster = new ApiMasterDetails();
		
		apiMaster.setUri(apiAndRolesDto.getUri());
		apiMaster.setStatus(1);
		apiMaster.setCreatedBy("Admin");
		apiMaster.setCreatedDate(new Date());
		apiMaster.setMethod(apiAndRolesDto.getMethodType());
		apiMaster.setUpdateBy("Admin");
		apiMaster.setUpdateDate(new Date());
		apiMaster.setPermitAll(false);
		
		List<RoleMastDetails> listOfRole = new ArrayList<RoleMastDetails>();
		apiAndRolesDto.getRoles().forEach(role -> {
			RoleMastDetails roleMastDetails=roleMasterRepository.findByRoleNameAndStatus(role, 1);
			if(roleMastDetails==null) {
				logger.error("Invalid role "+role);
				throw new UserManagementException("Invalid role "+role);
			}
			listOfRole.add(roleMastDetails);
		});
		
		apiMaster.setRolesDetails(listOfRole);
		apiMasterDetailsRepository.save(apiMaster);
		logger.trace("Exit from mapApiToRoles");
	}

	public List<ApiAndRolesResponseDto> mapRetrieveAndRolesMaping() {
		logger.trace("Entry into mapRetrieveAndRolesMaping");
		List<ApiAndRolesResponseDto> responseList = new ArrayList<ApiAndRolesResponseDto>();
		List<ApiMasterDetails> apiMasterDetailsList=globalProperties.findByMethodType("ALL");
		apiMasterDetailsList.forEach(apiMasterDetails -> {
			ApiAndRolesResponseDto dto = new ApiAndRolesResponseDto();
			dto.setUri(apiMasterDetails.getUri());
			dto.setMethodType(apiMasterDetails.getMethod());
			dto.setPermitAll(apiMasterDetails.getPermitAll());
			List<String> roles = new ArrayList<>();
			apiMasterDetails.getRolesDetails().forEach(role -> {
				roles.add(role.getRoleName());
			});
			dto.setRoles(roles);
			responseList.add(dto);
		});
		logger.trace("Exit from mapRetrieveAndRolesMaping");
		return responseList;
	}

	public void mapPermitToAll(PermitToAllDto permitToAllDto) {
		logger.trace("Entry into mapPermitToAll");
		ApiMasterDetails apiMasterDetails = apiMasterDetailsRepository.findByUriAndMethod(permitToAllDto.getUri(),
				permitToAllDto.getMethodType());

		if (apiMasterDetails != null) {
			if (permitToAllDto.isForceFully()) {
				if (apiMasterDetails.getRolesDetails() != null) {
					apiMasterDetails.getRolesDetails().removeAll(apiMasterDetails.getRolesDetails());
				}
				apiMasterDetailsRepository.delete(apiMasterDetails);
			} else {
				logger.error("Given uri is already mapped if you still want to premit to all please click on forcefully checkbox");
				throw new UserManagementException(
						"Given uri is already mapped if you still want to premit to all please click on forcefully checkbox");
			}
		}
		
		ApiMasterDetails apiMaster = new ApiMasterDetails();
		
		apiMaster.setUri(permitToAllDto.getUri());
		apiMaster.setStatus(1);
		apiMaster.setCreatedBy("Admin");
		apiMaster.setCreatedDate(new Date());
		apiMaster.setMethod(permitToAllDto.getMethodType());
		apiMaster.setUpdateBy("Admin");
		apiMaster.setUpdateDate(new Date());
		apiMaster.setPermitAll(true);
		apiMasterDetailsRepository.save(apiMaster);
		logger.trace("Exit from mapPermitToAll");
	}
	
	public void mapApplicationProperties(Map<String, String> map) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		GlobalProperties globalPropertiesInstance=mapper.convertValue(map, GlobalProperties.class);
		globalProperties.setLdapUrl(globalPropertiesInstance.getLdapUrl());
		globalProperties.setDnUsernameAttribute(globalPropertiesInstance.getDnUsernameAttribute());
		globalProperties.setDomainName(globalPropertiesInstance.getDomainName());
		globalProperties.setBaseDn(globalPropertiesInstance.getBaseDn());
		globalProperties.setSearchFilterObject(globalPropertiesInstance.getSearchFilterObject());
		globalProperties.setDnForSearchFilter(globalPropertiesInstance.getDnForSearchFilter());
		globalProperties.setUsernameAttribute(globalPropertiesInstance.getUsernameAttribute());
		globalProperties.setMobileAttribute(globalPropertiesInstance.getMobileAttribute());
		globalProperties.setFirstnameAttribute(globalPropertiesInstance.getFirstnameAttribute());
		globalProperties.setLastnameAttribute(globalPropertiesInstance.getLastnameAttribute());
		globalProperties.setEmailAttribute(globalPropertiesInstance.getEmailAttribute());
		globalProperties.setGenerateMobileNumber(globalPropertiesInstance.getGenerateMobileNumber());
		globalProperties.setCorsAllowedHosts(globalPropertiesInstance.getCorsAllowedHosts());
		globalProperties.setOtpSmsIntegrated(globalPropertiesInstance.getOtpSmsIntegrated());
		loadAllApiMappring(globalProperties);
	}
	
	private void loadAllApiMappring(GlobalProperties globalProperties){
		
		globalProperties.setListOfGetApis(new ArrayList<ApiMasterDetails>());
		globalProperties.setListOfPostApis(new ArrayList<ApiMasterDetails>());
		globalProperties.setListOfPatchApis(new ArrayList<ApiMasterDetails>());
		globalProperties.setListOfPutApis(new ArrayList<ApiMasterDetails>());
		globalProperties.setListOfDeleteApis(new ArrayList<ApiMasterDetails>());
		globalProperties.setListOfOptionApis(new ArrayList<ApiMasterDetails>());
		
		List<ApiMasterDetails> listOfApis=apiMasterDetailsRepository.findAll();
		
		if(listOfApis!=null) {
			listOfApis.forEach(api ->{
				switch (api.getMethod()) {
				case "GET":
					globalProperties.getListOfGetApis().add(api);
					break;
				case "POST":
					globalProperties.getListOfPostApis().add(api);
					break;
				case "PATCH":
					globalProperties.getListOfPatchApis().add(api);
					break;
				case "PUT":
					globalProperties.getListOfPutApis().add(api);
					break;
				case "DELETE":
					globalProperties.getListOfDeleteApis().add(api);	
					break;
				case "OPTION":
					globalProperties.getListOfGetApis().add(api);		
					break;
				default:
					break;
				}
			});
		}
	}

	public void mapAuthManagementProperties(Map<String, String> map) {	
		
		jwtConfig.setAllowedConcurrentUserLogin(getStringToInt(map.get("allowedConcurrentUserLogin")));
		jwtConfig.setAllowPreflightRequest(getStringToBoolean(map.get("allowPreflightRequest")));
		jwtConfig.setAllUrlPublicOfExternalSystem(getStringToBoolean(map.get("allUrlPublicOfExternalSystem")));
		jwtConfig.setApiRateLimitBy(map.get("apiRateLimitBy"));
		jwtConfig.setApiRateLimitCommaTime(map.get("apiRateLimitCommaTime"));
		jwtConfig.setBlockUserMessage(map.get("blockUserMessage"));
		jwtConfig.setCapitalCaseLetters(map.get("capitalCaseLetters"));
		jwtConfig.setCaptchaEncryptionKey(map.get("captchaEncryptionKey"));
		jwtConfig.setCaptchaExpireInSeconds(getStringToInt(map.get("captchaExpireInSeconds")));
		jwtConfig.setCapthaEnable(getStringToBoolean(map.get("capthaEnable")));
		jwtConfig.setContextRootPath(map.get("contextRootPath"));
		jwtConfig.setDisableUser(map.get("disableUser"));
		jwtConfig.setExternalSystemHandShakeToken(map.get("externalSystemHandShakeToken"));
		jwtConfig.setFirstUserIpAddressToAccess(map.get("firstUserIpAddressToAccess"));
		jwtConfig.setFirstUserRole(map.get("firstUserRole"));
		jwtConfig.setLastNumberOfPassCanNotUse(getStringToInt(map.get("lastNumberOfPassCanNotUse")));
		jwtConfig.setLogFilesLocation(map.get("logFilesLocation"));
		jwtConfig.setLowerCaseLetters(map.get("lowerCaseLetters"));
		jwtConfig.setNeedToUseCustomeExpiryTime(map.get("needToUseCustomeExpiryTime"));
		jwtConfig.setNotificationSmsCategory(map.get("notificationSmsCategory"));
		jwtConfig.setNotificationSmsDeliveryReportUrl(map.get("notificationSmsDeliveryReportUrl"));
		jwtConfig.setNotificationSmsIsVendorUrlGet(map.get("notificationSmsIsVendorUrlGet"));
		jwtConfig.setNotificationSmsIsVendorUrlPostBody(map.get("notificationSmsIsVendorUrlPostBody"));
		jwtConfig.setNotificationSmsPassword(map.get("notificationSmsPassword"));
		jwtConfig.setNotificationSmsSenderId(map.get("notificationSmsSenderId"));
		jwtConfig.setNotificationSmsTextTemplate(map.get("notificationSmsTextTemplate"));
		jwtConfig.setNotificationSmsTokenExpiryInMinutes(map.get("notificationSmsTokenExpiryInMinutes"));
		jwtConfig.setNotificationSmsUdh(map.get("notificationSmsUdh"));
		jwtConfig.setNotificationSmsUrlEncodeEnable(map.get("notificationSmsUrlEncodeEnable"));
		jwtConfig.setNotificationSmsUsername(map.get("notificationSmsUsername"));
		jwtConfig.setNotificationSmsVendorTokenUrl(map.get("notificationSmsVendorTokenUrl"));
		jwtConfig.setNotificationSmsVendorUrl(map.get("notificationSmsVendorUrl"));
		jwtConfig.setOneNumbers(map.get("oneNumbers"));
		jwtConfig.setOtpDefault(getStringToBoolean(map.get("otpDefault")));
		jwtConfig.setOtpEnable(getStringToBoolean(map.get("otpEnable")));
		jwtConfig.setOtpEncryptionKey(map.get("otpEncryptionKey"));
		jwtConfig.setOtpExpireInSeconds(getStringToInt(map.get("otpExpireInSeconds")));
		jwtConfig.setOtpSmsIntegrated(map.get("otpSmsIntegrated"));
		jwtConfig.setPassExpiredMessage(map.get("passExpiredMessage"));
		jwtConfig.setPasswordExpiredInDays(getStringToInt(map.get("passwordExpiredInDays")));
		jwtConfig.setPasswordLength(getStringToInt(map.get("passwordLength")));
		jwtConfig.setPasswordResetSessionExpireInSeconds(getStringToInt(map.get("passwordResetSessionExpireInSeconds")));
		jwtConfig.setPrivateKeyEncryption(map.get("privateKeyEncryption"));
		jwtConfig.setProxyIp(map.get("proxyIp"));
		jwtConfig.setProxyPort(getStringToInt(map.get("proxyPort")));
		jwtConfig.setPublicKeyEncryption(map.get("publicKeyEncryption"));
		jwtConfig.setRefreshTokenExpirationAfterSeconds(getStringToInt(map.get("refreshTokenExpirationAfterSeconds")));
		//jwtConfig.setSecretKey(map.get("secretKey"));
		jwtConfig.setSmsOtpOnDevEnv(map.get("smsOtpOnDevEnv"));
		jwtConfig.setSpecialCharacters(map.get("specialCharacters"));
		jwtConfig.setTimeSessionForUnSuccessAttemptInSecond(getStringToInt(map.get("timeSessionForUnSuccessAttemptInSecond")));
		jwtConfig.setTokenExpirationAfterSeconds(getStringToInt(map.get("tokenExpirationAfterSeconds")));
		jwtConfig.setTokenPrefix(map.get("tokenPrefix"));
		jwtConfig.setTokenRefresh(map.get("tokenRefresh"));
		jwtConfig.setUserBlockAfterNumberOfUnSuccesAttempt(getStringToLong(map.get("userBlockAfterNumberOfUnSuccesAttempt")));
		jwtConfig.setUserWillBlockForNextSecond(getStringToInt(map.get("userWillBlockForNextSecond")));
		
	}
	
	private Integer getStringToInt(String value){
		return Integer.parseInt(value);
	}
	
	private Boolean getStringToBoolean(String value){
		return Boolean.parseBoolean(value);
	}
	
	private Long getStringToLong(String value){
		return Long.parseLong(value);
	}

}
