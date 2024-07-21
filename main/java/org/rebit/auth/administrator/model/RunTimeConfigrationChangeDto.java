package org.rebit.auth.administrator.model;


import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RunTimeConfigrationChangeDto {
	
	@Min(value = 60)
	private Integer tokenExpirationAfterSeconds;
	@Min(value = 120)
	private Integer refreshTokenExpirationAfterSeconds;
	private boolean allUrlPublicOfExternalSystem;
	@Ascii
	private String externalSystemHandShakeToken;
	private boolean allowPreflightRequest;
	private int allowedConcurrentUserLogin;
	private int captchaExpireInSeconds;
	private int otpExpireInSeconds;
	private boolean capthaEnable;
    private boolean otpEnable;
    private boolean otpDefault;
    @Ascii
	private String capitalCaseLetters;
    @Ascii
	private String lowerCaseLetters;
    @Ascii
	private String specialCharacters;
	private Integer oneNumbers;
	private int passwordLength;
	private int passwordResetSessionExpireInSeconds;
	//private String publicKeyEncryption;
	//private String privateKeyEncryption;
	@Ascii
	private String blockUserMessage;
	@Ascii
	private String passExpiredMessage;
	private Long userBlockAfterNumberOfUnSuccesAttempt;
	private int userWillBlockForNextSecond;
	private int passwordExpiredInDays;
	private int timeSessionForUnSuccessAttemptInSecond;
	private int lastNumberOfPassCanNotUse;
	@Ascii
	private String disableUser;
	@Ascii
	private String firstUserRole;
	@Ascii
	private String firstUserIpAddressToAccess;
	@Ascii
	private String apiRateLimitBy;
	private Integer apiRateLimit;
	private Integer apiRateInSecondTime;
	
	//sms otp config
	
	private String notificationSmsUsername;		
	private String notificationSmsPassword;
	private String notificationSmsSenderId;
	private String notificationSmsTextTemplate;
	private String notificationSmsUrlEncodeEnable;
	private String notificationSmsVendorUrl;
	private String notificationSmsDeliveryReportUrl;
	private String notificationSmsUdh;
	private String notificationSmsVendorTokenUrl;
	private String notificationSmsIsVendorUrlPostBody;
	private String notificationSmsCategory;
	private String notificationSmsIsVendorUrlGet;
	private String needToUseCustomeExpiryTime;
	private String notificationSmsTokenExpiryInMinutes;
	private String proxyIp;
	private int proxyPort;
	private String smsOtpOnDevEnv;
	private String otpSmsIntegrated;
	
	//sms otp config
	
	
	
}
