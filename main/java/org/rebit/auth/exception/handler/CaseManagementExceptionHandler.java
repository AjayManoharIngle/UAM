package org.rebit.auth.exception.handler;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.exception.AuthenticationManagementException;
import org.rebit.auth.exception.ErrorMessage;
import org.rebit.auth.exception.RateLimitManagementException;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.model.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @author Anuradha.Phalke
 * Purpose: Case Management System Exception Handler class
 * Created Date: 26-03-2021
 * Modification History:     Last Change Date:       Revised By:     Change/Defect description:
 * CR Defect No
 */

@ControllerAdvice
public class CaseManagementExceptionHandler extends ResponseEntityExceptionHandler{
	
	final static Logger logger = LogManager.getLogger();
	
	@Autowired
	private RequestContext requestContext;

	@ExceptionHandler(value = {UserManagementException.class})
	public ResponseEntity<Object> UserManagementException(UserManagementException exception,WebRequest request){
		logger.debug("entry -  UserManagementException");
		String errorDiscription = exception.getLocalizedMessage();
		logger.error("UserManagementException error  "+errorDiscription);
		if(errorDiscription==null) {
			errorDiscription=exception.toString();
			logger.error("UserManagementException error  "+errorDiscription);
		}
		requestContext.setFailed(true);
		requestContext.setErrorMessage(errorDiscription);
		ErrorMessage error=new ErrorMessage(new Date(),errorDiscription);
		logger.error("UserManagementException error "+error);
		logger.debug("exit -  UserManagementException");

		return new ResponseEntity<Object>(error,new HttpHeaders(),HttpStatus.resolve(400));
	}

	@ExceptionHandler(value = {AuthManagementException.class})
	public ResponseEntity<Object> authManagementException(AuthManagementException exception,WebRequest request){
		logger.debug("entry -  AuthManagementException");
		String errorDiscription = exception.getLocalizedMessage();
		logger.error("AuthManagementException error  "+errorDiscription);
		if(errorDiscription==null) {
			errorDiscription=exception.toString();
			logger.error("AuthManagementException error  "+errorDiscription);
		}
		requestContext.setFailed(true);
		requestContext.setErrorMessage(errorDiscription);
		ErrorMessage error=new ErrorMessage(new Date(),errorDiscription);
		logger.error("AuthManagementException error "+error);
		logger.debug("exit -  AuthManagementException");

		return new ResponseEntity<Object>(error,new HttpHeaders(),HttpStatus.resolve(401));
	}
	
	@ExceptionHandler(value = {RateLimitManagementException.class})
	public ResponseEntity<Object> rateLimitManagementException(RateLimitManagementException exception,WebRequest request){
		logger.debug("entry -  RateLimitManagementException");
		String errorDiscription = exception.getLocalizedMessage();
		logger.error("RateLimitManagementException error  "+errorDiscription);
		if(errorDiscription==null) {
			errorDiscription=exception.toString();
			logger.error("RateLimitManagementException error  "+errorDiscription);
		}
		requestContext.setFailed(true);
		requestContext.setErrorMessage(errorDiscription);
		ErrorMessage error=new ErrorMessage(new Date(),errorDiscription);
		logger.error("RateLimitManagementException error "+error);
		logger.debug("exit -  RateLimitManagementException");

		return new ResponseEntity<Object>(error,new HttpHeaders(),HttpStatus.resolve(429));
	}
	
	
	@ExceptionHandler(value = {AuthenticationManagementException.class})
	public ResponseEntity<Object> authenticationManagementException(AuthenticationManagementException exception,WebRequest request){
		logger.debug("entry -  AuthenticationManagementException");
		String errorDiscription = exception.getLocalizedMessage();
		logger.error("AuthManagementException error  "+errorDiscription);
		if(errorDiscription==null) {
			errorDiscription=exception.toString();
			logger.error("AuthenticationManagementException error  "+errorDiscription);
		}
		requestContext.setFailed(true);
		requestContext.setErrorMessage(errorDiscription);
		ErrorMessage error=new ErrorMessage(new Date(),errorDiscription);
		logger.error("AuthenticationManagementException error "+error);
		logger.debug("exit -  AuthenticationManagementException");

		return new ResponseEntity<Object>(error,new HttpHeaders(),HttpStatus.resolve(403));
	}
	
	
	@ExceptionHandler(Exception.class)
	  public ResponseEntity<Object> unhandledException(Exception e) {
		logger.debug("entry - unhandledException");
		logger.error("Unhandled exception", e);
	    requestContext.setFailed(true);
		requestContext.setErrorMessage(e.getLocalizedMessage());
		ErrorMessage error = new ErrorMessage(new Date(), "Internal Server Error, Please contact the system administrator."); 
		logger.debug("Exit - unhandledException");
		return new ResponseEntity<Object>(error,new HttpHeaders(),HttpStatus.resolve(500));
	  }

}
