package org.rebit.auth.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.entity.PassHistoryDetails;
import org.rebit.auth.entity.UserMaster;
import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.repository.PassHistoryDetailsRepository;
import org.rebit.auth.repository.RegionalOfficeDetailsRepository;
import org.rebit.auth.service.ApplicationUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class AuthKavachUtil {
	
	final static Logger logger = LogManager.getLogger();
	
	@Autowired
	private ApplicationUserServices applicationUserServices;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	@Autowired
	private RegionalOfficeDetailsRepository regionalOfficeDetailsRepository;
	
	
	@Autowired
	private PassHistoryDetailsRepository passHistoryDetailsRepository;
	
	public boolean compareDateWithPresentDate(Date date){
	logger.trace("Entry into compareDateWithPresentDate");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	Date dt = new Date();
	
	Date currentDate;
	Date refreshTokenDate;
	try {
		currentDate = sdf.parse(sdf.format(dt));
		refreshTokenDate = sdf.parse(sdf.format(date));
	} catch (ParseException e) {
		logger.error("Error while data parsing",e);
		throw new AuthManagementException("Error while data parsing");
	}
	
	if (currentDate.compareTo(refreshTokenDate) > 0) {
		return false;
	}
	logger.trace("Exit from compareDateWithPresentDate");
	return true;
}

public String generateJwtToken(String userName,Collection<? extends GrantedAuthority> authorities,int tokenExpirationAfterSeconds,SecretKey secretKey,Date refreshTokenExpiration){
	logger.trace("Entry into generateJwtToken");
	int milliseconds = tokenExpirationAfterSeconds * 1000;
	int refreshCallInMiliSeconds=(tokenExpirationAfterSeconds-60)*1000;
	Date timeObject = new Date(new Date().getTime() + milliseconds);
	List<String> roles = new ArrayList<String>();
	
	
	UserMaster userDetails=applicationUserServices.getUserDetails(userName);
	
	String officeLocationId = "";
	if(userDetails.getOfficeLocationDetails()!=null) {
		officeLocationId = String.valueOf(userDetails.getOfficeLocationDetails().getOfficeLocationId());
	}
	
	List<String> regionalOffice = new ArrayList<String>();
	
	authorities.forEach(i -> {
		roles.add(i.getAuthority().substring(5));
	});
	
	if(regionalOffice.size()<=0) {
		if(userDetails.getRegionalOfficeDetails()!=null) {
			regionalOffice.add(userDetails.getRegionalOfficeDetails().getRoName());
		}
	}
	
	Map<String, Object> claims=new HashMap<String, Object>();
	claims.put("Roles", roles);
	claims.put("OfficeLocationId",officeLocationId);
	claims.put("sub",userName);
	claims.put("preferred_username",userName);
	claims.put("groups",regionalOffice);
	claims.put("authorities", authorities);
	claims.put("iat", new Date());
	claims.put("exp", timeObject);
	claims.put("refresh_token_iat",new Date());
	claims.put("refresh_token_exp", refreshTokenExpiration);
	claims.put("refresh_token_cim", jwtConfig.getRefreshTokenExpirationAfterSeconds()-60);
	claims.put("jwt_token_cim", refreshCallInMiliSeconds);
	logger.trace("Exit from generateJwtToken");
	return Jwts.builder().
			signWith(secretKey).
			setClaims(claims).
			compact();
}

public String passwordPolicyCheck(String password,UserMaster userDetails){
	
	if(password.length()<jwtConfig.getPasswordLength()) {
		throw new UserManagementException("Password should be minimum "+jwtConfig.getPasswordLength()+" characters");
	}
	
	password.charAt(0);
	
	String capitalCaseLetters = jwtConfig.getCapitalCaseLetters();
	boolean isCapitalCaseLettersPresent =false;
    String lowerCaseLetters = jwtConfig.getLowerCaseLetters();
    boolean isLowerCaseLettersPresent =false;
    String specialCharacters = jwtConfig.getSpecialCharacters();
    boolean isSpecialCharactersPresent =false;
    String numbers = jwtConfig.getOneNumbers();
    boolean isNumbersPresent =false;
    
    char firstOne=password.charAt(0);
   
    if(!(capitalCaseLetters.indexOf(firstOne)!=-1 || lowerCaseLetters.indexOf(firstOne)!=-1)) {
    	throw new UserManagementException("Password should start with letter only");
    }
    
    for (int i = 0; i < password.length(); i++) {
		char c=password.charAt(i);
		
		if(capitalCaseLetters.indexOf(c)!=-1) {
			isCapitalCaseLettersPresent=true;
		}
		if(lowerCaseLetters.indexOf(c)!=-1) {
			isLowerCaseLettersPresent=true;
		}
		if(specialCharacters.indexOf(c)!=-1) {
			isSpecialCharactersPresent=true;
		}
		if(numbers.indexOf(c)!=-1) {
			isNumbersPresent=true;
		}
	}
    if(!(isCapitalCaseLettersPresent && isLowerCaseLettersPresent && isSpecialCharactersPresent && isNumbersPresent)) {
    	throw new UserManagementException("Require that at least one digit\r\n" + 
    			"Require that at least one lowercase letter\r\n" + 
    			"Require that at least one uppercase letter\r\n" + 
    			"Require that at least one of the special character "+specialCharacters);
    }
		String encPass = HashingAlgorithm.encryptThisString(password);
		List<PassHistoryDetails> passHistory = userDetails.getPassHistoryDetails();
		if (passHistory != null && !passHistory.isEmpty()) {
			List<PassHistoryDetails> passHistoryToDelete = new ArrayList<PassHistoryDetails>();
			for (int i = 0; i < passHistory.size(); i++) {
				PassHistoryDetails passHistoryDetail = passHistory.get(i);
				if (passHistoryDetail != null && jwtConfig.getLastNumberOfPassCanNotUse() > i) {
					if (encPass.equals(passHistoryDetail.getPassCode())) {
						throw new UserManagementException(
								"You can not use last " + jwtConfig.getLastNumberOfPassCanNotUse() + " Password");
					}
				} else {
					passHistoryToDelete.add(passHistoryDetail);
				}
			}
			passHistoryDetailsRepository.deleteAll(passHistoryToDelete);
		}
		
    return encPass;
}

public String generatePassword(int length) {
    String capitalCaseLetters = jwtConfig.getCapitalCaseLetters();
    String lowerCaseLetters = jwtConfig.getLowerCaseLetters();
    String specialCharacters = jwtConfig.getSpecialCharacters();
    String numbers = jwtConfig.getOneNumbers();
    String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
    Random random = new Random();
    char[] password = new char[length];

    password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
    password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
    password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
    password[3] = numbers.charAt(random.nextInt(numbers.length()));

    for(int i = 4; i< length ; i++) {
       password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
    }
    return new String(password);
 }


	public void passHistoryCreator(UserMaster userDetails) {
		PassHistoryDetails passHistory = new PassHistoryDetails();
		passHistory.setCreatedAt(userDetails.getCreatedAt());
		passHistory.setCreatorUserId(userDetails.getCreatorUserId());
		passHistory.setPassCode(userDetails.getPassCode());
		passHistory.setUserMaster(userDetails);
		passHistoryDetailsRepository.save(passHistory);
	}
}
