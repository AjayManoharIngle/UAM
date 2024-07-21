package org.rebit.auth.service.impl;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.administrator.model.EventEntityWithPage;
import org.rebit.auth.administrator.model.GenerateAndEnableTokenResponse;
import org.rebit.auth.administrator.model.GenerateTokenResponse;
import org.rebit.auth.administrator.model.GlobalPropertiesDto;
import org.rebit.auth.administrator.model.LogFilesNameDto;
import org.rebit.auth.administrator.model.RunTimeConfigrationChangeDto;
import org.rebit.auth.administrator.model.SmsDao;
import org.rebit.auth.config.LdapUserAuthProvider;
import org.rebit.auth.entity.ApplicationProperties;
import org.rebit.auth.entity.EventEntityLogDetails;
import org.rebit.auth.entity.SmsGenerationLogs;
import org.rebit.auth.entity.SmsToken;
import org.rebit.auth.entity.SmsTokenGenerationLogs;
import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.gateway.ExternalSystemCall;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.mapper.RunTimeConfigrationChangeMapper;
import org.rebit.auth.model.CheckLdapDto;
import org.rebit.auth.repository.ApplicationPropertiesRepository;
import org.rebit.auth.repository.EventEntityLogDetailsRepository;
import org.rebit.auth.repository.SmsGenerationLogsRepository;
import org.rebit.auth.repository.SmsTokenGenerationLogsRepository;
import org.rebit.auth.repository.SmsTokenRepository;
import org.rebit.auth.service.RunTimeConfigrationChangeService;
import org.rebit.auth.util.RSAUtil;
import org.rebit.auth.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RunTimeConfigrationChangeServiceImpl implements RunTimeConfigrationChangeService {
	
	final static Logger logger = LogManager.getLogger();
	
	@Autowired
	private RunTimeConfigrationChangeMapper runTimeConfigrationChangeMapper;
	
	@Autowired
	private ApplicationPropertiesRepository applicationPropertiesRepository;
	
	@Autowired
	private EventEntityLogDetailsRepository eventEntityLogDetailsRepository;
	
	@Autowired
	private SmsTokenRepository smsTokenRepository;
	
	@Autowired
	private SmsTokenGenerationLogsRepository smsTokenGenerationLogsRepository;
	
	@Autowired
	private SmsGenerationLogsRepository smsGenerationLogsRepository;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	@Autowired
	private ExternalSystemCall externalApiCalls;
	
	@Autowired
	private RSAUtil rsaUtil;
	
	@Autowired
	private LdapUserAuthProvider ldapUserAuthProvider;
	
	@Override
	public void changeConfig(RunTimeConfigrationChangeDto runTimeConfigrationChangeDto) {
		runTimeConfigrationChangeMapper.updateConfig(runTimeConfigrationChangeDto);
	}

	@Override
	public RunTimeConfigrationChangeDto getConfig() {
		return runTimeConfigrationChangeMapper.getConfig();
	}

	@Override
	public GlobalPropertiesDto getGlobalConfig() {
		return runTimeConfigrationChangeMapper.getGlobalProperties();
	}

	@Override
	public void changeGLobalConfig(GlobalPropertiesDto globalProperties) {
		
		if("false".equals(globalProperties.getGenerateMobileNumber())){
			if(StringUtils.isEmpty(globalProperties.getMobileAttribute())){
				throw new UserManagementException("if getGenerateMobileNumber is false then getMobileAttribute can not be null or empty.");
			}
		}
		
		if(StringUtils.isEmpty(globalProperties.getDnUsernameAttribute()) && StringUtils.isEmpty(globalProperties.getDomainName())){
			throw new UserManagementException("getDnUsernameAttribute and getDomainName both can not be null or empty at same time.");
		}
		
		List<ApplicationProperties> propertyList = runTimeConfigrationChangeMapper.updateGlobalProperties(globalProperties);
		applicationPropertiesRepository.saveAll(propertyList);
	}

	@Override
	public LogFilesNameDto getLogFileNames() {
		return runTimeConfigrationChangeMapper.getfileNames();
	}
	
	@Override
	public EventEntityWithPage getEventLogs(int pageNo) {
		Pageable pageWithElements = PageRequest.of(pageNo, 10);
		Page<EventEntityLogDetails> pageEventEntityLogDetails= eventEntityLogDetailsRepository.findAll(pageWithElements);
		return runTimeConfigrationChangeMapper.eventEntityLogDetailsToEventEntityLogDto(pageEventEntityLogDetails.getContent(),pageEventEntityLogDetails.getTotalPages());
	}
	
	@Override
	public ResponseEntity<Object> download(String fileName) {
		try {
			return runTimeConfigrationChangeMapper.download(fileName);
		} catch (FileNotFoundException e) {
			throw new UserManagementException("Error While download file");
		}
	}
	
	
	public boolean sendSmsOtpUsingToken(long mobileNumber, String otpValue, String otpExpiryTime) {
		logger.debug("entry sendSmsOtpUsingToken");
			SmsDao smsDao = new SmsDao();
			smsDao.setUsername(jwtConfig.getNotificationSmsUsername());
			smsDao.setPassword(jwtConfig.getNotificationSmsPassword());
			smsDao.setSenderId(jwtConfig.getNotificationSmsSenderId());
			String textTemplate = jwtConfig.getNotificationSmsTextTemplate();
			String textToSend = "Dear Customer, Your Demo Account has been created-Unimobile";
			if (textTemplate != null && textTemplate.contains("SMS_OTP_VALUE")) {
				if (textTemplate.contains("SMS_OTP_EXPIRY")) {
					textToSend = textTemplate.replaceAll("SMS_OTP_VALUE", otpValue).replaceAll("SMS_OTP_EXPIRY",
							otpExpiryTime);
				} else {
					textToSend = textTemplate.replaceAll("SMS_OTP_VALUE", otpValue);
				}
			}

			String token = null;
			List<SmsToken> smsTokens = smsTokenRepository.findAll();
			if (smsTokens != null && smsTokens.size() > 0) {
				logger.debug("Found old token");
				SmsToken smsToken = smsTokens.get(0);
				if (smsToken == null || smsToken.getValue() == null || smsToken.getExpiryDate() == null
						|| smsToken.getExpiryDate().before(new Date())) {
					logger.debug("Found null or old invalid token :" + token);
					GenerateAndEnableTokenResponse generateAndEnableTokenResponse = liveTokenGenerate();
					if (generateAndEnableTokenResponse.isStatus()) {
						token = generateAndEnableTokenResponse.getNewToken();
					} else {
						logger.debug("exit sendSmsOtpUsingToken");
						throw new UserManagementException("Error while Sending SMS.");
					}
				} else {
					token = smsToken.getValue();
					logger.debug("Found old Valid token :" + token);
				}
			} else {
				logger.debug("Old token not found");
				GenerateAndEnableTokenResponse generateAndEnableTokenResponse = liveTokenGenerate();
				if (generateAndEnableTokenResponse.isStatus()) {
					token = generateAndEnableTokenResponse.getNewToken();
				} else {
					logger.debug("exit sendSmsOtpUsingToken");
					throw new UserManagementException("Error while Sending SMS.");
				}
			}
			smsDao.setToken(token);

			try {
				if (jwtConfig.getNotificationSmsUrlEncodeEnable() != null && jwtConfig.getNotificationSmsUrlEncodeEnable().equalsIgnoreCase("true")) {
					smsDao.setText(URLEncoder.encode(textToSend, StandardCharsets.UTF_8.toString()));
				} else {
					smsDao.setText(textToSend);
				}
				if (jwtConfig.getNotificationSmsIsVendorUrlPostBody() != null && jwtConfig.getNotificationSmsIsVendorUrlPostBody().equalsIgnoreCase("true")) {
					smsDao.setText(textToSend);
				}
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage());
				logger.debug("exit sendSmsOtpUsingToken");
				throw new UserManagementException("Invalid OPT Text");
			}
			smsDao.setRecipientNumber(Long.toString(mobileNumber));
			smsDao.setSmsVendorUrl(jwtConfig.getNotificationSmsVendorUrl());
			smsDao.setUdh(jwtConfig.getNotificationSmsUdh());
			smsDao.setCategory(jwtConfig.getNotificationSmsCategory());
			ResponseEntity<Object> response = null;
			
//			String smsVendorUrlPostBody = properties.getSmsVendorUrlPostBody();
				if (jwtConfig.getNotificationSmsIsVendorUrlPostBody() != null && jwtConfig.getNotificationSmsIsVendorUrlPostBody().equalsIgnoreCase("true")) {
					logger.debug("Calling sendSms with POST & body");
					response = externalApiCalls.sendSms(smsDao, false, true);
				} else if (jwtConfig.getNotificationSmsIsVendorUrlGet() != null && jwtConfig.getNotificationSmsIsVendorUrlGet().equalsIgnoreCase("false")) {
					logger.debug("Calling sendSms with POST & query parameters");
					response = externalApiCalls.sendSms(smsDao, false, false);
				} else {
					logger.debug("Calling sendSms with GET & query parameters");
					response = externalApiCalls.sendSms(smsDao, true, false);
				}			
			

			if (response != null && response.getStatusCode().is2xxSuccessful()) {
				logger.debug("SMS successfully sent");
				response.status(response.getStatusCode()).headers(ValidationUtil.getHttpHeaders());
				smsGenerationLogsRepository.save(new SmsGenerationLogs(smsDao.getRecipientNumber(), textToSend, new Date(), true));
			} else {
				logger.debug("SMS Failure");
				response.status(response.getStatusCode()).headers(ValidationUtil.getHttpHeaders());
				smsGenerationLogsRepository.save(new SmsGenerationLogs(smsDao.getRecipientNumber(), textToSend, new Date(), false));
			}
			logger.debug("exit sendSmsOtpUsingToken");
			return true;
	}
	
	
	private GenerateAndEnableTokenResponse liveTokenGenerate() {
		logger.debug("entry liveTokenGenerate");

		GenerateAndEnableTokenResponse generateAndEnableTokenResponse = generateAndEnableToken();
		if (generateAndEnableTokenResponse.isStatus()) {
			smsTokenGenerationLogsRepository.save(new SmsTokenGenerationLogs(new Date(), true));
			logger.debug("exit liveTokenGenerate");
			return generateAndEnableTokenResponse;
		} else {
			smsTokenGenerationLogsRepository.save(new SmsTokenGenerationLogs(new Date(), false));
			logger.debug("exit liveTokenGenerate");
			return generateAndEnableTokenResponse;
		}
	}
	
	private GenerateAndEnableTokenResponse generateAndEnableToken() {

		logger.debug("entry generateAndEnableToken");

		GenerateAndEnableTokenResponse response = new GenerateAndEnableTokenResponse();
		GenerateTokenResponse generateTokenResponse = externalApiCalls.generateSmsToken(
				jwtConfig.getNotificationSmsVendorTokenUrl(), jwtConfig.getNotificationSmsUsername(), jwtConfig.getNotificationSmsPassword());

		if (generateTokenResponse != null) {
			if (!generateTokenResponse.isStatus()) {
				response.setStatus(false);
				logger.debug("exit generateAndEnableToken");
				return response;
			} else {
				String newToken = generateTokenResponse.getToken();
				if (newToken != null) {
					boolean enableToken = true;//externalApiCalls.smsTokenAction(globalProperties.getNotificationSmsVendorTokenUrl(),globalProperties.getNotificationSmsUsername(), globalProperties.getNotificationSmsPassword(), newToken, "enable");
					if (enableToken) {
						response.setStatus(true);
						response.setNewToken(newToken);
						List<SmsToken> smsTokens = smsTokenRepository.findAll();
						if (smsTokens != null && smsTokens.size() > 0) {
							response.setOldToken(smsTokens.get(0).getValue());
							smsTokenRepository.deleteAll();
						}
						Date tokenExpiry = null;
						
						
						try {
							tokenExpiry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(generateTokenResponse.getExpiryDate());
						} catch (ParseException e) {
							logger.error("Error while parsing token expiryDate",e);
						}
						
						if (tokenExpiry == null || "true".equalsIgnoreCase(jwtConfig.getNeedToUseCustomeExpiryTime())) {
							Calendar now = Calendar.getInstance();
							now.add(Calendar.MINUTE,Integer.parseInt(jwtConfig.getNotificationSmsTokenExpiryInMinutes()));
							
							if(tokenExpiry!=null && "true".equalsIgnoreCase(jwtConfig.getNeedToUseCustomeExpiryTime())) {
								if(now.getTime().after(tokenExpiry)) {
									throw new UserManagementException("you cannot set custome tokenExpiry time greater then actual tokenExpiry time");
								}
							}
							tokenExpiry = now.getTime();
						}
						SmsToken smsToken = new SmsToken(newToken, tokenExpiry);
						smsTokenRepository.save(smsToken);
						logger.debug("exit generateAndEnableToken");
						return response;
					} else {
						response.setStatus(false);
						logger.debug("exit generateAndEnableToken");
						return response;
					}
				} else {
					response.setStatus(false);
					logger.debug("exit generateAndEnableToken");
					return response;
				}
			}
		} else {
			response.setStatus(false);
			logger.debug("exit generateAndEnableToken");
			return response;
		}

	}

	@Override
	public boolean checkLdap(CheckLdapDto checkLdapDto) {
		try {
			checkLdapDto.setPassword(rsaUtil.decrypt(checkLdapDto.getPassword()));
			checkLdapDto.setUserName(rsaUtil.decrypt(checkLdapDto.getUserName()));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			throw new AuthManagementException("Error while decrypting userName");
		}
		
		return ldapUserAuthProvider.authenticatUser(checkLdapDto.getUserName(), checkLdapDto.getPassword());
	}
	
}
