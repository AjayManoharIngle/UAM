package org.rebit.auth.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.model.ApiAndRolesDto;
import org.rebit.auth.model.ApiAndRolesResponseDto;
import org.rebit.auth.model.LogOutDto;
import org.rebit.auth.model.PermitToAllDto;
import org.rebit.auth.model.RefreshTokenDto;
import org.rebit.auth.model.StatusAndMessage;
import org.rebit.auth.model.SynchDto;
import org.rebit.auth.model.TokenAndUri;
import org.rebit.auth.model.TokenResponse;
import org.rebit.auth.model.UsernameAndPasswordDto;
import org.rebit.auth.service.AuthServices;
import org.rebit.auth.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 
 * @author Kapil Gautam
 * 
 */

@RestController
@RequestMapping("/")
public class AuthController {
	
	final static  Logger logger = LogManager.getLogger();
	
	@Autowired
	private AuthServices authServices;
	
	@Autowired
	private ValidationUtil validationUtil;

	
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody @Valid UsernameAndPasswordDto usernameAndPasswordDto,Errors errors){
		logger.trace("Entry into login");
		ValidationUtil.validation(errors);
		TokenResponse tokenResponse=authServices.login(usernameAndPasswordDto);
		logger.trace("Exit from login");
		return new ResponseEntity<>(tokenResponse,HttpStatus.OK);
	}
	
	@PostMapping("/sign-out")
	public ResponseEntity<Object> logout(@RequestBody @Valid LogOutDto logOutDto,Errors errors,@RequestHeader("authorization") String authorization){
		logger.trace("Entry into logout");
		ValidationUtil.validation(errors);
		authServices.logOutUser(logOutDto,authorization);
		logger.trace("Exit from logout");
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshTokenDto RefreshTokenDto,Errors errors){
		logger.trace("Entry into refresh");
		ValidationUtil.validation(errors);
		TokenResponse tokenResponse=authServices.jwtTokenByRefreshToken(RefreshTokenDto.getToken());
		logger.trace("Exit from refresh");
		return new ResponseEntity<>(tokenResponse,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/verifaction")
	public ResponseEntity<StatusAndMessage> url(@RequestBody @Valid TokenAndUri tokenAndUri,Errors errors){
		logger.trace("Entry into url");
		ValidationUtil.validation(errors);
		StatusAndMessage statusAndMessage=authServices.verifyUri(tokenAndUri);
		logger.trace("Exit from url");
		return new ResponseEntity<>(statusAndMessage,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/api-role-mapping")
	public ResponseEntity<Object> apiRoleMapping(@RequestBody @Valid ApiAndRolesDto apiAndRolesDto,Errors errors){
		logger.trace("Entry into apiRoleMapping");
		ValidationUtil.validation(errors);
		authServices.apiAndRolesMaping(apiAndRolesDto);
		logger.trace("Exit from apiRoleMapping");
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/retrieve-api-role-mapping")
	public ResponseEntity<Object> getAllApiAndRole(@RequestHeader("authorization") String authorization){
		logger.trace("Entry into getAllApiAndRole");
		List<ApiAndRolesResponseDto> response=authServices.retrieveAndRolesMaping();
		logger.trace("Exit from getAllApiAndRole");
		return new ResponseEntity<>(response,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/api-permit-to-all")
	public ResponseEntity<Object> apiPermitToAll(@RequestBody @Valid PermitToAllDto permitToAllDto,Errors errors){
		logger.trace("Entry into apiPermitToAll");
		ValidationUtil.validation(errors);
		authServices.apiPermitAll(permitToAllDto);
		logger.trace("Exit from apiPermitToAll");
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/captcha")
	public ResponseEntity<Object> captcha() {
		logger.trace("Entry into captcha");
		try {
			return authServices.captcha();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new UserManagementException("Some error are there please check logs");
		}
	}
	
	@GetMapping(value = "/key")
	public ResponseEntity<Object> getPublicKey() {
		logger.debug("inside -  key");
		return new ResponseEntity<Object>(authServices.getPublicKey(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/synch")
	public ResponseEntity<Object> synch(@RequestBody @Valid SynchDto synchDto,Errors errors) {
		logger.debug("inside -  synch");
		ValidationUtil.validation(errors);
		authServices.getSynch(synchDto);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@GetMapping("/refresh-application-properties")
	public ResponseEntity<Object> refreshApplicationProperties(){
		logger.debug("refreshApplicationProperties - entry");
		authServices.refreshApplicationProperties();
		logger.debug("refreshApplicationProperties - exit");
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);		
	}
}
