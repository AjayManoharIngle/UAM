package org.rebit.auth.controller;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.disha.model.RegistrationDto;
import org.rebit.auth.service.impl.DishaServiceImpl;
import org.rebit.auth.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 
 * @author kapil.Gautam
 * Purpose: Administrator controller class
 * Created Date: 15-06-2021
 */

@RestController
@RequestMapping("/registration-dashboard")
public class DishaController {
	
	final static Logger logger = LogManager.getLogger(DishaController.class);
	
	@Autowired
	private DishaServiceImpl dishaService;

	
	@PostMapping("/registration")
	public ResponseEntity<Object> registration(@RequestBody @Valid RegistrationDto registrationDto,Errors errors){
		logger.trace("Entry into login");
		ValidationUtil.validation(errors);
		dishaService.registration(registrationDto);
		logger.trace("Exit from login");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/update-registration")
	public ResponseEntity<Object> registrationUpdate(@RequestBody @Valid RegistrationDto registrationDto,Errors errors){
		logger.trace("Entry into login");
		ValidationUtil.validation(errors);
		dishaService.registration(registrationDto);
		logger.trace("Exit from login");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/retrieve-registration/{projectName}")
	public ResponseEntity<Object> registrationRetrieve(@PathVariable String projectName,@RequestHeader(required = false, value = "authorization") String authorization){
		logger.trace("Entry into login");
		RegistrationDto registrationDto = dishaService.registrationRetrieve(projectName,authorization);
		logger.trace("Exit from login");
		return new ResponseEntity<>(registrationDto,HttpStatus.OK);
	}
	

}
