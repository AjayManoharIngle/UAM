package org.rebit.auth.jwt;

import org.springframework.stereotype.Component;

import com.google.common.net.HttpHeaders;

@Component
//@PropertySource("classpath:auth_management_system.properties")
public class JwtConfig {
	
	////@Value("${application.jwt.secretKey}")
    //private String secretKey;
	////@Value("${application.jwt.tokenPrefix}")
    private String tokenPrefix;
	////@Value("${application.jwt.tokenRefesh}")
    private String tokenRefresh;
	////@Value("${application.jwt.token.expiration.after.seconds}")
    private Integer tokenExpirationAfterSeconds;
	////@Value("${application.jwt.refresh.token.expiration.after.seconds}")
    private Integer refreshTokenExpirationAfterSeconds;
	////@Value("${by.default.all.url.public.of.external.system}")
    private boolean allUrlPublicOfExternalSystem;
//	//@Value("${application.api.permitall}")
//    private String applicationApiPermitall;
	////@Value("${external.system.hand.shake.token}")
    private String externalSystemHandShakeToken;
	////@Value("${allow.preflight.request}")
    private boolean allowPreflightRequest;
	////@Value("${allowed.concurrent.user.login:0}")
    private int allowedConcurrentUserLogin;
	
	////@Value("${captcha.expire.in.seconds}")
	private int captchaExpireInSeconds;
	
	////@Value("${otp.expire.in.seconds}")
	private int otpExpireInSeconds;
	
	////@Value("${captcha.encryption.key}")
	private String captchaEncryptionKey;
	
	////@Value("${otp.encryption.key}")
	private String otpEncryptionKey;

	////@Value("${captcha.enable}")
    private boolean capthaEnable;
	
	////@Value("${otp.enable}")
    private boolean otpEnable;

	////@Value("${otp.default}")
    private boolean otpDefault;

	////@Value("${capital.case.letters}")
	private String capitalCaseLetters;
	
	////@Value("${lower.case.letters}")
	private String lowerCaseLetters;
	
	////@Value("${special.characters}")
	private String specialCharacters;
	
	////@Value("${one.numbers}")
	private String oneNumbers;
	
	////@Value("${password.length}")
	private int passwordLength;
	
//	//@Value("${otp.sms.integrated}")
//	private String otpSmsIntegrated;
	
	////@Value("${password.reset.session.expire.in.seconds}")
	private int passwordResetSessionExpireInSeconds;
	
	
	////@Value("${public.key.encryption}")
	private String publicKeyEncryption;
	
	////@Value("${private.key.encryption}")
	private String privateKeyEncryption;
	
	
	
	
	//@Value("${context.root.path:#{null}}")
	private String contextRootPath;
	
	//@Value("${block.user.message}")
	private String blockUserMessage;
	
	//@Value("${pass.expired.message}")
	private String passExpiredMessage;
	
	//@Value("${user.block.after.number.of.unsucces.attempt}")
	private Long userBlockAfterNumberOfUnSuccesAttempt;
	
	//@Value("${user.will.block.for.next.second}")
	private int userWillBlockForNextSecond;
	
	//@Value("${password.expired.in.days}")
	private int passwordExpiredInDays;
	
	//@Value("${time.session.for.unSuccess.attempt.in.second}")
	private int timeSessionForUnSuccessAttemptInSecond;
	
	//@Value("${last.number.of.pass.can.not.use}")
	private int lastNumberOfPassCanNotUse;
	
	//@Value("${user.disabled.account}")
	private String disableUser;
	
	//@Value("${first.user.role}")
	private String firstUserRole;
	
	//@Value("${first.user.ip.address.to.access}")
	private String firstUserIpAddressToAccess;
	
	//@Value("${api.rate.limit.by}")
	private String apiRateLimitBy;
	
	//@Value("${api.rate.limit.comma.time}")
	private String apiRateLimitCommaTime;
	
	//@Value("${log.files.location}")
	private String logFilesLocation;
	
	
//sms otp
	
	//@Value("${notification.sms.username}")
	private String notificationSmsUsername;		
		
	//@Value("${notification.sms.password}")
	private String notificationSmsPassword;

	//@Value("${notification.sms.senderId}")
	private String notificationSmsSenderId;
	
	//@Value("${notification.sms.text.template}")
	private String notificationSmsTextTemplate;
		
	//@Value("${notification.sms.url.encode.enable}")
	private String notificationSmsUrlEncodeEnable;
	
	//@Value("${notification.sms.vendor.url}")
	private String notificationSmsVendorUrl;
	
	//@Value("${notification.sms.delivery.report.url}")
	private String notificationSmsDeliveryReportUrl;
	
	//@Value("${notification.sms.udh}")
	private String notificationSmsUdh;
			
	//@Value("${notification.sms.vendor.token.url}")
	private String notificationSmsVendorTokenUrl;

	
	//@Value("${notification.sms.is.vendor.url.post.body}")
	private String notificationSmsIsVendorUrlPostBody;
	
	//@Value("${notification.sms.category}")
	private String notificationSmsCategory;
	
	//@Value("${notification.sms.is.vendor.url.get}")
	private String notificationSmsIsVendorUrlGet;
	
	//@Value("${need.to.use.custome.expiry.time}")
	private String needToUseCustomeExpiryTime;
	
	//@Value("${notification.sms.token.expiry.in.minutes}")
	private String notificationSmsTokenExpiryInMinutes;
	
	//@Value("${proxy.ip}")
	private String proxyIp;
	
	//@Value("${proxy.port}")
	private int proxyPort;
	
	//@Value("${sms.otp.on.dev.env}")
	private String smsOtpOnDevEnv;
	
	//@Value("${otp.sms.integrated}")
	private String otpSmsIntegrated;
	
//sms otm

	
	public String getNotificationSmsUsername() {
		return notificationSmsUsername;
	}

	public String getOtpSmsIntegrated() {
		return otpSmsIntegrated;
	}

	public void setOtpSmsIntegrated(String otpSmsIntegrated) {
		this.otpSmsIntegrated = otpSmsIntegrated;
	}

	public String getSmsOtpOnDevEnv() {
		return smsOtpOnDevEnv;
	}

	public void setSmsOtpOnDevEnv(String smsOtpOnDevEnv) {
		this.smsOtpOnDevEnv = smsOtpOnDevEnv;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getNotificationSmsVendorTokenUrl() {
		return notificationSmsVendorTokenUrl;
	}

	public void setNotificationSmsVendorTokenUrl(String notificationSmsVendorTokenUrl) {
		this.notificationSmsVendorTokenUrl = notificationSmsVendorTokenUrl;
	}

	public String getNotificationSmsIsVendorUrlPostBody() {
		return notificationSmsIsVendorUrlPostBody;
	}

	public void setNotificationSmsIsVendorUrlPostBody(String notificationSmsIsVendorUrlPostBody) {
		this.notificationSmsIsVendorUrlPostBody = notificationSmsIsVendorUrlPostBody;
	}

	public String getNotificationSmsCategory() {
		return notificationSmsCategory;
	}

	public void setNotificationSmsCategory(String notificationSmsCategory) {
		this.notificationSmsCategory = notificationSmsCategory;
	}

	public String getNotificationSmsIsVendorUrlGet() {
		return notificationSmsIsVendorUrlGet;
	}

	public void setNotificationSmsIsVendorUrlGet(String notificationSmsIsVendorUrlGet) {
		this.notificationSmsIsVendorUrlGet = notificationSmsIsVendorUrlGet;
	}

	public String getNeedToUseCustomeExpiryTime() {
		return needToUseCustomeExpiryTime;
	}

	public void setNeedToUseCustomeExpiryTime(String needToUseCustomeExpiryTime) {
		this.needToUseCustomeExpiryTime = needToUseCustomeExpiryTime;
	}

	public String getNotificationSmsTokenExpiryInMinutes() {
		return notificationSmsTokenExpiryInMinutes;
	}

	public void setNotificationSmsTokenExpiryInMinutes(String notificationSmsTokenExpiryInMinutes) {
		this.notificationSmsTokenExpiryInMinutes = notificationSmsTokenExpiryInMinutes;
	}

	public void setNotificationSmsUsername(String notificationSmsUsername) {
		this.notificationSmsUsername = notificationSmsUsername;
	}

	public String getNotificationSmsPassword() {
		return notificationSmsPassword;
	}

	public void setNotificationSmsPassword(String notificationSmsPassword) {
		this.notificationSmsPassword = notificationSmsPassword;
	}

	public String getNotificationSmsSenderId() {
		return notificationSmsSenderId;
	}

	public void setNotificationSmsSenderId(String notificationSmsSenderId) {
		this.notificationSmsSenderId = notificationSmsSenderId;
	}

	public String getNotificationSmsTextTemplate() {
		return notificationSmsTextTemplate;
	}

	public void setNotificationSmsTextTemplate(String notificationSmsTextTemplate) {
		this.notificationSmsTextTemplate = notificationSmsTextTemplate;
	}

	public String getNotificationSmsUrlEncodeEnable() {
		return notificationSmsUrlEncodeEnable;
	}

	public void setNotificationSmsUrlEncodeEnable(String notificationSmsUrlEncodeEnable) {
		this.notificationSmsUrlEncodeEnable = notificationSmsUrlEncodeEnable;
	}

	public String getNotificationSmsVendorUrl() {
		return notificationSmsVendorUrl;
	}

	public void setNotificationSmsVendorUrl(String notificationSmsVendorUrl) {
		this.notificationSmsVendorUrl = notificationSmsVendorUrl;
	}

	public String getNotificationSmsDeliveryReportUrl() {
		return notificationSmsDeliveryReportUrl;
	}

	public void setNotificationSmsDeliveryReportUrl(String notificationSmsDeliveryReportUrl) {
		this.notificationSmsDeliveryReportUrl = notificationSmsDeliveryReportUrl;
	}

	public String getNotificationSmsUdh() {
		return notificationSmsUdh;
	}

	public void setNotificationSmsUdh(String notificationSmsUdh) {
		this.notificationSmsUdh = notificationSmsUdh;
	}

	public String getLogFilesLocation() {
		return logFilesLocation;
	}

	public void setLogFilesLocation(String logFilesLocation) {
		this.logFilesLocation = logFilesLocation;
	}

	public String getApiRateLimitBy() {
		return apiRateLimitBy;
	}

	public String getApiRateLimitCommaTime() {
		return apiRateLimitCommaTime;
	}


	public String getFirstUserRole() {
		return firstUserRole;
	}


	public String getFirstUserIpAddressToAccess() {
		return firstUserIpAddressToAccess;
	}


	public String getDisableUser() {
		return disableUser;
	}

	
	public int getLastNumberOfPassCanNotUse() {
		return lastNumberOfPassCanNotUse;
	}

//	public String getDisableUser() {
//		return disableUser;
//	}

	public int getTimeSessionForUnSuccessAttemptInSecond() {
		return timeSessionForUnSuccessAttemptInSecond;
	}

	public String getPassExpiredMessage() {
		return passExpiredMessage;
	}

	public String getBlockUserMessage() {
		return blockUserMessage;
	}

	public int getPasswordExpiredInDays() {
		return passwordExpiredInDays;
	}

	public int getUserWillBlockForNextSecond() {
		return userWillBlockForNextSecond;
	}

	public Long getUserBlockAfterNumberOfUnSuccesAttempt() {
		return userBlockAfterNumberOfUnSuccesAttempt;
	}

//	public String getUserUserNameAttribute() {
//		return userUserNameAttribute;
//	}
//
//	public String getDnUser() {
//		return dnUser;
//	}
//
//	public String getAdminUserNameAttribute() {
//		return adminUserNameAttribute;
//	}
//
//	public String getDnAdmin() {
//		return dnAdmin;
//	}

//	public String getLdapUrl() {
//		return ldapUrl;
//	}

//	public String getAttributesToGetFromLdap() {
//		return attributesToGetFromLdap;
//	}
//
//	public String getSearchFilterObject() {
//		return searchFilterObject;
//	}
//
//	public String getDnForSearchFilter() {
//		return dnForSearchFilter;
//	}

	public String getPublicKeyEncryption() {
		return publicKeyEncryption;
	}

	public String getPrivateKeyEncryption() {
		return privateKeyEncryption;
	}

	public String getCapitalCaseLetters() {
		return capitalCaseLetters;
	}

	public String getLowerCaseLetters() {
		return lowerCaseLetters;
	}

	public String getSpecialCharacters() {
		return specialCharacters;
	}

	public String getOneNumbers() {
		return oneNumbers;
	}

	public int getPasswordLength() {
		return passwordLength;
	}

	public boolean isCapthaEnable() {
		return capthaEnable;
	}

	public int getCaptchaExpireInSeconds() {
		return captchaExpireInSeconds;
	}

	public String getCaptchaEncryptionKey() {
		return captchaEncryptionKey;
	}

	public int getAllowedConcurrentUserLogin() {
		return allowedConcurrentUserLogin;
	}

	public boolean isAllowPreflightRequest() {
		return allowPreflightRequest;
	}

	public String getExternalSystemHandShakeToken() {
		return externalSystemHandShakeToken;
	}

	public boolean isAllUrlPublicOfExternalSystem() {
		return allUrlPublicOfExternalSystem;
	}
	
	public Integer getRefreshTokenExpirationAfterSeconds() {
		return refreshTokenExpirationAfterSeconds;
	}

//    public String getApplicationApiPermitall() {
//		return applicationApiPermitall;
//	}

//	public String getSecretKey() {
//        return secretKey;
//    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public Integer getTokenExpirationAfterSeconds() {
		return tokenExpirationAfterSeconds;
	}

	public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

	public String getTokenRefresh() {
		return tokenRefresh;
	}

	public String getOtpEncryptionKey() {
		return otpEncryptionKey;
	}

	public int getOtpExpireInSeconds() {
		return otpExpireInSeconds;
	}

	public boolean isOtpEnable() {
		return otpEnable;
	}

//	public String getOtpSmsIntegrated() {
//		return otpSmsIntegrated;
//	}

	public int getPasswordResetSessionExpireInSeconds() {
		return passwordResetSessionExpireInSeconds;
	}

	public String getContextRootPath() {
		return contextRootPath;
	}

	public boolean isOtpDefault() {
		return otpDefault;
	}

//	public void setSecretKey(String secretKey) {
//		this.secretKey = secretKey;
//	}

	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}

	public void setTokenRefresh(String tokenRefresh) {
		this.tokenRefresh = tokenRefresh;
	}

	public void setTokenExpirationAfterSeconds(Integer tokenExpirationAfterSeconds) {
		this.tokenExpirationAfterSeconds = tokenExpirationAfterSeconds;
	}

	public void setRefreshTokenExpirationAfterSeconds(Integer refreshTokenExpirationAfterSeconds) {
		this.refreshTokenExpirationAfterSeconds = refreshTokenExpirationAfterSeconds;
	}

	public void setAllUrlPublicOfExternalSystem(boolean allUrlPublicOfExternalSystem) {
		this.allUrlPublicOfExternalSystem = allUrlPublicOfExternalSystem;
	}

//	public void setApplicationApiPermitall(String applicationApiPermitall) {
//		this.applicationApiPermitall = applicationApiPermitall;
//	}

	public void setExternalSystemHandShakeToken(String externalSystemHandShakeToken) {
		this.externalSystemHandShakeToken = externalSystemHandShakeToken;
	}

	public void setAllowPreflightRequest(boolean allowPreflightRequest) {
		this.allowPreflightRequest = allowPreflightRequest;
	}

	public void setAllowedConcurrentUserLogin(int allowedConcurrentUserLogin) {
		this.allowedConcurrentUserLogin = allowedConcurrentUserLogin;
	}

	public void setCaptchaExpireInSeconds(int captchaExpireInSeconds) {
		this.captchaExpireInSeconds = captchaExpireInSeconds;
	}

	public void setOtpExpireInSeconds(int otpExpireInSeconds) {
		this.otpExpireInSeconds = otpExpireInSeconds;
	}

	public void setCaptchaEncryptionKey(String captchaEncryptionKey) {
		this.captchaEncryptionKey = captchaEncryptionKey;
	}

	public void setOtpEncryptionKey(String otpEncryptionKey) {
		this.otpEncryptionKey = otpEncryptionKey;
	}

	public void setCapthaEnable(boolean capthaEnable) {
		this.capthaEnable = capthaEnable;
	}

	public void setOtpEnable(boolean otpEnable) {
		this.otpEnable = otpEnable;
	}

	public void setOtpDefault(boolean otpDefault) {
		this.otpDefault = otpDefault;
	}

	public void setCapitalCaseLetters(String capitalCaseLetters) {
		this.capitalCaseLetters = capitalCaseLetters;
	}

	public void setLowerCaseLetters(String lowerCaseLetters) {
		this.lowerCaseLetters = lowerCaseLetters;
	}

	public void setSpecialCharacters(String specialCharacters) {
		this.specialCharacters = specialCharacters;
	}

	public void setOneNumbers(String oneNumbers) {
		this.oneNumbers = oneNumbers;
	}

	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}

	public void setPasswordResetSessionExpireInSeconds(int passwordResetSessionExpireInSeconds) {
		this.passwordResetSessionExpireInSeconds = passwordResetSessionExpireInSeconds;
	}

	public void setPublicKeyEncryption(String publicKeyEncryption) {
		this.publicKeyEncryption = publicKeyEncryption;
	}

	public void setPrivateKeyEncryption(String privateKeyEncryption) {
		this.privateKeyEncryption = privateKeyEncryption;
	}

	public void setContextRootPath(String contextRootPath) {
		this.contextRootPath = contextRootPath;
	}

	public void setBlockUserMessage(String blockUserMessage) {
		this.blockUserMessage = blockUserMessage;
	}

	public void setPassExpiredMessage(String passExpiredMessage) {
		this.passExpiredMessage = passExpiredMessage;
	}

	public void setUserBlockAfterNumberOfUnSuccesAttempt(Long userBlockAfterNumberOfUnSuccesAttempt) {
		this.userBlockAfterNumberOfUnSuccesAttempt = userBlockAfterNumberOfUnSuccesAttempt;
	}

	public void setUserWillBlockForNextSecond(int userWillBlockForNextSecond) {
		this.userWillBlockForNextSecond = userWillBlockForNextSecond;
	}

	public void setPasswordExpiredInDays(int passwordExpiredInDays) {
		this.passwordExpiredInDays = passwordExpiredInDays;
	}

	public void setTimeSessionForUnSuccessAttemptInSecond(int timeSessionForUnSuccessAttemptInSecond) {
		this.timeSessionForUnSuccessAttemptInSecond = timeSessionForUnSuccessAttemptInSecond;
	}

	public void setLastNumberOfPassCanNotUse(int lastNumberOfPassCanNotUse) {
		this.lastNumberOfPassCanNotUse = lastNumberOfPassCanNotUse;
	}

	public void setDisableUser(String disableUser) {
		this.disableUser = disableUser;
	}

	public void setFirstUserRole(String firstUserRole) {
		this.firstUserRole = firstUserRole;
	}

	public void setFirstUserIpAddressToAccess(String firstUserIpAddressToAccess) {
		this.firstUserIpAddressToAccess = firstUserIpAddressToAccess;
	}

	public void setApiRateLimitBy(String apiRateLimitBy) {
		this.apiRateLimitBy = apiRateLimitBy;
	}

	public void setApiRateLimitCommaTime(String apiRateLimitCommaTime) {
		this.apiRateLimitCommaTime = apiRateLimitCommaTime;
	}
	
}
