package org.rebit.auth.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.administrator.model.AddOfficeLocationDetailsDto;
import org.rebit.auth.administrator.model.AddRegionalLocationDetailsDto;
import org.rebit.auth.administrator.model.AddRoleDetailsDto;
import org.rebit.auth.administrator.model.GetOfficeLocationDetailsDto;
import org.rebit.auth.administrator.model.GetRoleDetailsDto;
import org.rebit.auth.administrator.model.OfficeLocationParamDto;
import org.rebit.auth.administrator.model.RegionalLocationDetailsDto;
import org.rebit.auth.administrator.model.RegionalOfficeParamDto;
import org.rebit.auth.administrator.model.RolesParamDto;
import org.rebit.auth.administrator.model.UpdateStatusDto;
import org.rebit.auth.administrator.model.UserDetailsDto;
import org.rebit.auth.connector.model.DisableUser;
import org.rebit.auth.connector.model.EmployeeSearchDto;
import org.rebit.auth.connector.model.OtpResponseDto;
import org.rebit.auth.connector.model.OtpVerificationRequestDto;
import org.rebit.auth.connector.model.ResetPasswordDto;
import org.rebit.auth.connector.model.UpdateUserProfileInfo;
import org.rebit.auth.connector.model.UserProfileInfo;
import org.rebit.auth.connector.model.VerifyUserInfoDto;
import org.rebit.auth.entity.OTPLogsEntity;
import org.rebit.auth.entity.RoleMastDetails;
import org.rebit.auth.entity.UserMaster;
import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.mapper.AdministratorMapper;
import org.rebit.auth.model.UserInfo;
import org.rebit.auth.repository.OTPServiceRepository;
import org.rebit.auth.repository.UserMasterRepository;
import org.rebit.auth.service.AdministratorService;
import org.rebit.auth.service.RunTimeConfigrationChangeService;
import org.rebit.auth.util.AlgorithmAndDateUtil;
import org.rebit.auth.util.AuthKavachUtil;
import org.rebit.auth.util.HashingAlgorithm;
import org.rebit.auth.util.RSAUtil;
import org.rebit.auth.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class AdministratorServiceImpl implements AdministratorService {
	
	private static final Long STATUS_LONG_ACTIVE = 1L;
	private static final Long STATUS_LONG_INACTIVE = 0L;
	
	final static Logger logger = LogManager.getLogger();

	@Autowired
	private AdministratorMapper administratorMapper;

	
	@Autowired
	private UserMasterRepository userMasterRepository;
	
	@Autowired
	private AuthKavachUtil authKavachUtil;
	
	@Autowired
	private ValidationUtil ValidationUtil;
	
	@Autowired
	private RSAUtil rsaUtil;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	@Autowired
	private OTPServiceRepository otpServiceRepository;

	
	@Autowired
	private RunTimeConfigrationChangeService runTimeConfigrationChangeService;

	
	@Override
	public RolesParamDto getParamToSearchRoles() {
		RolesParamDto rolesParamDto= administratorMapper.mapParamToSearchRole();
		return rolesParamDto;
	}
	
	@Override
	public List<GetRoleDetailsDto> getRoles(String authorization) {
		UserInfo userInfo=new UserInfo();
		if(authorization!=null) {
			userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		}
		List<GetRoleDetailsDto> list=administratorMapper.mapRoleSearchDetails(userInfo);
		return list;
	}
	
	@Override
	public void updateRoles(String roleName,UpdateStatusDto updateRoleDetailsDto, String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		administratorMapper.updateRoles(roleName,userInfo,updateRoleDetailsDto);
	}
	
	@Override
	public void addRoles(AddRoleDetailsDto addRoleDetailsDto, String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		administratorMapper.addRoles(addRoleDetailsDto,userInfo);
	}
	
	@Override
	public OfficeLocationParamDto getParamToSearchOfficeLocation() {
		OfficeLocationParamDto officeLocationParamDto= administratorMapper.mapParamToSearchOfficeLocation();
		return officeLocationParamDto;
	}
	
	@Override
	public List<GetOfficeLocationDetailsDto> getOfficeLocation(String authorization) {
		UserInfo userInfo=new UserInfo();
		if(authorization!=null) {
			userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		}
		List<GetOfficeLocationDetailsDto> list=administratorMapper.mapOfficeLocationSearchDetails(userInfo);
		return list;
	}
	
	@Override
	public void updateOfficeLocation(String officeLocationName,AddOfficeLocationDetailsDto updateOfficeLocationDetailsDto, String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		administratorMapper.updateOfficeLoc(userInfo,officeLocationName,updateOfficeLocationDetailsDto);
	}
	
	@Override
	public void addOfficeLocation(AddOfficeLocationDetailsDto addOfficeLocationDetailsDto,
			String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		administratorMapper.addOfficeLoc(addOfficeLocationDetailsDto,userInfo);
	}
	
	@Override
	public RegionalOfficeParamDto getParamToSearchRegional() {
		RegionalOfficeParamDto regionalOfficeParamDto= administratorMapper.mapParamToSearchRegional();
		return regionalOfficeParamDto;
	}
	
	@Override
	public List<RegionalLocationDetailsDto> getRegionalLocation() {
		List<RegionalLocationDetailsDto> list=administratorMapper.mapRegionalLocationSearchDetails();
		return list;
	}
	
	@Override
	public void updateRegionalOffice(String officeLocationName,AddRegionalLocationDetailsDto updateOfficeLocationDetailsDto, String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		administratorMapper.updateRegionalOffice(userInfo,officeLocationName,updateOfficeLocationDetailsDto);
	}

	@Override
	public void addRegionalOffice(AddRegionalLocationDetailsDto addRegionalLocationDetailsDto, String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		administratorMapper.addRegionalOffice(addRegionalLocationDetailsDto,userInfo); 
		
	}

	@Override
	public void createUser(UserDetailsDto userDetailsDto, String authorization,boolean withRole) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		administratorMapper.createUserMapping(userDetailsDto,userInfo, withRole);
	}

	
	@Override
	public void disableUser(String userName, DisableUser disableUser,String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		UserMaster userDetails=userMasterRepository.findByUserName(userName);
		if(userDetails!=null) {
			userDetails.setUpdaterUserId(userInfo.getUserId());
			userDetails.setUpdatedAt(new Date());
			if(!disableUser.isEnabled()) {
			userDetails.setStatus(STATUS_LONG_INACTIVE);
			}else {
			userDetails.setStatus(STATUS_LONG_ACTIVE);
			}
			userMasterRepository.save(userDetails);
		}
	}
	
	
	@Override
	public UserProfileInfo getUserProfileInfo(String userName,String authorization) {
//		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		UserMaster userDetails=userMasterRepository.findByUserName(userName);
		if(userDetails==null) {
			throw new UserManagementException("User Not found with given UserName "+userName);
		}
		UserProfileInfo dto = new UserProfileInfo();
		dto.setBlockedTill(userDetails.getBlockedTill());
		dto.setCreatedAt(userDetails.getCreatedAt());
		dto.setCreatorUserId(userDetails.getCreatorUserId());
		dto.setFirstName(userDetails.getFirstName());
		dto.setIsLDAPUser(userDetails.getIsLDAPUser());
		dto.setLastFailedAttempt(userDetails.getLastFailedAttempt());
		dto.setLastLoginDate(userDetails.getLastLoginDate());
		dto.setLastName(userDetails.getLastName());
		dto.setOfficeLocationDetails(null != userDetails.getOfficeLocationDetails() && null != userDetails.getOfficeLocationDetails().getLocationLongName() ? userDetails.getOfficeLocationDetails().getLocationLongName() : null);
		dto.setPassExpired(userDetails.getPassExpired());
		dto.setRegionalOfficeDetails(null != userDetails.getRegionalOfficeDetails() && null != userDetails.getRegionalOfficeDetails().getRoName() ? userDetails.getRegionalOfficeDetails().getRoName() : null);
		if(userDetails.getRolesDetails()!=null) {
		dto.setRolesDetails(userDetails.getRolesDetails().stream().map(RoleMastDetails::getRoleName).collect(Collectors.toList()));
		}
		dto.setStatus(userDetails.getStatus());
		dto.setUnSuccessAttempt(userDetails.getUnSuccessAttempt());
		dto.setUpdatedAt(userDetails.getUpdatedAt());
		dto.setUpdaterUserId(userDetails.getUpdaterUserId());
		dto.setUserEmailId(userDetails.getUserEmailId());
		dto.setUserMobileNo(userDetails.getUserMobileNo());
		dto.setUserName(userDetails.getUserName());
		return dto;
	}
	
	@Override
	public void updateUserProfileInfo(String userName, UpdateUserProfileInfo userProfile,String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		administratorMapper.updateUserProfileInfo(userName,userProfile,userInfo) ;
		}
	
	@Override
	public void resetPassword(ResetPasswordDto resetPasswordDto,String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		try {
			resetPasswordDto.setCurrentPassword(rsaUtil.decrypt(resetPasswordDto.getCurrentPassword()));
			resetPasswordDto.setNewPassword(rsaUtil.decrypt(resetPasswordDto.getNewPassword()));
			resetPasswordDto.setUserName(rsaUtil.decrypt(resetPasswordDto.getUserName()));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			throw new AuthManagementException("Error while decrypting userName");
		}
		
		UserMaster userDetails=userMasterRepository.findByUserName(resetPasswordDto.getUserName());
		if(userDetails==null) {
			throw new UserManagementException("User Not found with given UserName "+resetPasswordDto.getUserName());
		}
		if(userDetails.getUserName().equalsIgnoreCase(resetPasswordDto.getUserName())) {
			String currentPass=HashingAlgorithm.encryptThisString(resetPasswordDto.getCurrentPassword());
			if(userDetails.getPassCode().equalsIgnoreCase(currentPass)) {
				String newPass=authKavachUtil.passwordPolicyCheck(resetPasswordDto.getNewPassword(),userDetails);
				userDetails.setPassCode(newPass);
				userDetails.setUpdatedAt(new Date());
				userDetails.setUpdaterUserId(userInfo.getUserId());
				userMasterRepository.save(userDetails);
				authKavachUtil.passHistoryCreator(userDetails);
			}else {
				throw new UserManagementException("Invalid current password for user "+resetPasswordDto.getUserName());
			}
		}else {
			throw new UserManagementException("Your are not authorized the password for user "+resetPasswordDto.getUserName());
		}
	}
	
	
	@Override
	public List<EmployeeSearchDto> searchUser(String authorization) {
		List<EmployeeSearchDto> users=administratorMapper.userDetailsToEmployeeDetailsDto(authorization);
		return users;
	}
	
	
	@Override
	public ResponseEntity<Object> verifyUserInfo(VerifyUserInfoDto verifyUserInfoDto) {
		validateCaptchaToken(verifyUserInfoDto.getToken(), verifyUserInfoDto.getCaptchaValue());
		UserMaster userDetails=userMasterRepository.findByUserEmailIdAndStatus(verifyUserInfoDto.getEmail(),STATUS_LONG_ACTIVE);
		if(userDetails!=null){
			try {
				return processOtp(Long.valueOf(userDetails.getUserMobileNo()), userDetails.getUserEmailId());
			} catch (Exception e) {
				logger.debug("exit - verifyUserInfo - error while generating otp");
				logger.error(e.getMessage(), e);
				throw new UserManagementException("exit - verifyUserInfo - error while generating otp");
			}
		}else {
			throw new UserManagementException("Invalid Email Address");
		}
	}
	

	private void validateCaptchaToken(String token, String captchaValue) {
		try {
			token = AlgorithmAndDateUtil.decrypt(token,jwtConfig.getCaptchaEncryptionKey());
		} catch (Exception e) {
			throw new UserManagementException("Error while decrypt captcha token");
		}
		if (!(token.substring(19).equalsIgnoreCase(captchaValue))) {
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
			throw new UserManagementException(e.getMessage());
		}
		if (currentDate.compareTo(captchaDate) > 0) {
			throw new UserManagementException("Captcha Expired");
		}
	}

	@Transactional
	public ResponseEntity<Object> processOtp(long mobileNumber, String key) {
//		String smsOtpIntegrated = jwtConfig.getOtpSmsIntegrated();
		boolean smsIntegrated = false;
		if (jwtConfig.getOtpSmsIntegrated() != null && jwtConfig.getOtpSmsIntegrated().equalsIgnoreCase("true")) {
			smsIntegrated = true;
		}
		String otpValue = null;
		if(jwtConfig.isOtpDefault()) {
			otpValue="111111";
		}else {
			otpValue=getRandomNumberString();	
		}		
		String token = null;
		int otpExpiryTime = jwtConfig.getOtpExpireInSeconds();
		int sessionExpireTime = jwtConfig.getPasswordResetSessionExpireInSeconds();
		try {
			token = AlgorithmAndDateUtil.encrypt(AlgorithmAndDateUtil.tokenExpireDate(otpExpiryTime) + otpValue,
					jwtConfig.getOtpEncryptionKey());
			key = AlgorithmAndDateUtil.encrypt(AlgorithmAndDateUtil.tokenExpireDate(sessionExpireTime) + key,
					jwtConfig.getOtpEncryptionKey());
			if (smsIntegrated) {
				boolean response = runTimeConfigrationChangeService.sendSmsOtpUsingToken(mobileNumber, otpValue,otpExpiryTime+"");
				if (response) {
					OtpResponseDto otpResponse = new OtpResponseDto();
					otpResponse.setSmsToken(token);
					otpResponse.setExpiredTimeInSecond(otpExpiryTime);
					otpResponse.setKey(key);
					OTPLogsEntity otpEntity = new OTPLogsEntity();
					otpEntity.setMobileNumber(mobileNumber);
					otpEntity.setOtpValue(otpValue);
					otpEntity.setToken(token);
					otpEntity.setKey(key);
					otpServiceRepository.save(otpEntity);
					return new ResponseEntity<Object>(otpResponse, HttpStatus.OK);
				} else {
					throw new UserManagementException("error while generating otp");
				}
			} else {
				OtpResponseDto otpResponse = new OtpResponseDto();
				otpResponse.setSmsToken(token);
				otpResponse.setExpiredTimeInSecond(otpExpiryTime);
				otpResponse.setKey(key);
				OTPLogsEntity otpEntity = new OTPLogsEntity();
				otpEntity.setMobileNumber(mobileNumber);
				otpEntity.setOtpValue(otpValue);
				otpEntity.setToken(token);
				otpEntity.setKey(key);
				otpServiceRepository.save(otpEntity);
				return new ResponseEntity<Object>(otpResponse, HttpStatus.OK);
			}
		} catch (Exception e) {
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
	public ResponseEntity<Object> verifyOtp(OtpVerificationRequestDto otpVerificationRequestDto) {
		logger.debug("entry - verifyOtp");
		try {
			otpVerificationRequestDto.setSmsToken(AlgorithmAndDateUtil.decrypt(otpVerificationRequestDto.getSmsToken(),
					jwtConfig.getOtpEncryptionKey()));
		} catch (Exception e) {
			logger.debug("exit - verifyOtp - error occured while decrypting smsToken");
			logger.error(e.getMessage(), e);
			throw new UserManagementException("Error while verifying OTP");
		}
		if (!(otpVerificationRequestDto.getSmsToken().substring(19)
				.equalsIgnoreCase(otpVerificationRequestDto.getValue()))) {
			logger.debug("exit - verifyOtp - otp mismatch");
			throw new UserManagementException("Invalid OTP");
		}
		boolean tokenExpired;
		try {
			tokenExpired = isTokenExpired(otpVerificationRequestDto.getSmsToken());
		} catch (Exception e) {
			logger.debug("exit - verifyOtp - error occured while verifying user token");
			logger.error(e.getMessage(), e);
			throw new UserManagementException("Error while verifying OTP");
		}
//		if (tokenExpired) {
//			logger.debug("exit - verifyOtp - otp expired");
//			throw new ApplicationManagmentException("OTP expired");
//		}
		OtpVerificationRequestDto response = new OtpVerificationRequestDto();
		try {
			response.setSmsToken(AlgorithmAndDateUtil.encrypt(otpVerificationRequestDto.getValue(),
					jwtConfig.getOtpEncryptionKey()));
			response.setKey(otpVerificationRequestDto.getKey());
		} catch (Exception e) {
			logger.debug("exit - verifyOtp - error occured while encrypting original OTP");
			logger.error(e.getMessage(), e);
			throw new UserManagementException("Error while verifying OTP");
		}
		logger.debug("exit - verifyOtp - success");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<Object> resetPassword(OtpVerificationRequestDto otpVerificationRequestDto) {
		logger.debug("entry - resetPassword");
		try {
			otpVerificationRequestDto.setSmsToken(AlgorithmAndDateUtil.decrypt(otpVerificationRequestDto.getSmsToken(),
					jwtConfig.getOtpEncryptionKey()));
		} catch (Exception e) {
			logger.debug("exit - resetPassword - error occured while decrypting smsToken");
			logger.error(e.getMessage(), e);
			throw new UserManagementException("Error occured during password reset");
		}
		OTPLogsEntity otpEntity = otpServiceRepository.findByOtpValueAndKey(otpVerificationRequestDto.getSmsToken(),
				otpVerificationRequestDto.getKey());
		if (otpEntity == null) {
			logger.debug("exit - resetPassword - smsToken & passwordToken not found in db");
			throw new UserManagementException("Invalid Data");
		}
		try {
			otpVerificationRequestDto.setKey(AlgorithmAndDateUtil.decrypt(otpVerificationRequestDto.getKey(),
					jwtConfig.getOtpEncryptionKey()));
			logger.debug("entry - resetPassword - user token decrypted");
			otpVerificationRequestDto.setValue(rsaUtil.decrypt(otpVerificationRequestDto.getValue()));
			logger.debug("entry - resetPassword - password token decrypted");
		} catch (Exception e) {
			logger.debug("exit - resetPassword - error occured while decrypting user and password token");
			logger.error(e.getMessage(), e);
			throw new UserManagementException("Error occured during password reset");
		}
		boolean tokenExpired;
		try {
			tokenExpired = isTokenExpired(otpVerificationRequestDto.getKey());
		} catch (Exception e) {
			logger.debug("exit - resetPassword - error occured while validating user token");
			logger.error(e.getMessage(), e);
			throw new UserManagementException("Error occured during password reset");
		}
		
		if (tokenExpired) {
			logger.debug("exit - resetPassword - user token expired");
			throw new UserManagementException("Timeout occured");
		}	  
		  	String userName = otpVerificationRequestDto.getKey().substring(19);
			logger.debug("exit - resetPassword - success");
			administratorMapper.resetPassword(userName, otpVerificationRequestDto.getValue());
			return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	private boolean isTokenExpired(String token) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date dt = new Date();
		Date currentDate;
		Date tokenDate;
		try {
			currentDate = sdf.parse(sdf.format(dt));
			tokenDate = sdf.parse(token.substring(0, 19));
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		if (currentDate.compareTo(tokenDate) > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void unBlockUser(String userName) {
		logger.debug("entry - unBlockUser");
		UserMaster userDetails=userMasterRepository.findByUserName(userName);
		if(userDetails==null) {
			throw new UserManagementException("User Not found with given UserName "+userName);
		}
		userDetails.setBlockedTill(new Date());
		userMasterRepository.save(userDetails);
		logger.debug("exit - unBlockUser");
		
	}

	@Override
	public List<GetRoleDetailsDto> getAllRoles() {
		return administratorMapper.mapRoleSearchDetails();
	}

	@Override
	public List<UserProfileInfo> getUserProfiles(String authorization) {
		UserInfo userInfo=ValidationUtil.getUserInfoFromToken(authorization);
		List<UserMaster> userList=userMasterRepository.findAll();
		
		List<UserProfileInfo> userProfileInfos = new ArrayList<>();
		
		if(userList!=null) {
			userList.forEach(userDetails ->{
				UserProfileInfo dto = new UserProfileInfo();
				dto.setBlockedTill(userDetails.getBlockedTill());
				dto.setCreatedAt(userDetails.getCreatedAt());
				dto.setCreatorUserId(userDetails.getCreatorUserId());
				dto.setFirstName(userDetails.getFirstName());
				dto.setIsLDAPUser(userDetails.getIsLDAPUser());
				dto.setLastFailedAttempt(userDetails.getLastFailedAttempt());
				dto.setLastLoginDate(userDetails.getLastLoginDate());
				dto.setLastName(userDetails.getLastName());
				dto.setOfficeLocationDetails(null != userDetails && userDetails.getOfficeLocationDetails() != null && userDetails.getOfficeLocationDetails().getLocationLongName() != null ? userDetails.getOfficeLocationDetails().getLocationLongName() : null);
				dto.setPassExpired(userDetails.getPassExpired());
				dto.setRegionalOfficeDetails( null != userDetails  && userDetails.getRegionalOfficeDetails() != null ? userDetails.getRegionalOfficeDetails().getRoName() : null);
				if(userDetails.getRolesDetails()!=null) {
				dto.setRolesDetails(userDetails.getRolesDetails().stream().map(RoleMastDetails::getRoleName).collect(Collectors.toList()));
				}
				dto.setStatus(userDetails.getStatus());
				dto.setUnSuccessAttempt(userDetails.getUnSuccessAttempt());
				dto.setUpdatedAt(userDetails.getUpdatedAt());
				dto.setUpdaterUserId(userDetails.getUpdaterUserId());
				dto.setUserEmailId(userDetails.getUserEmailId());
				dto.setUserMobileNo(userDetails.getUserMobileNo());
				dto.setUserName(userDetails.getUserName());
				userProfileInfos.add(dto);
			});
		}
		
		
		return userProfileInfos;
	}

}
