package org.rebit.auth.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.model.StatusAndMessage;
import org.rebit.auth.model.TokenAndUri;
import org.rebit.auth.model.UserInfo;
import org.rebit.auth.service.AuthServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * @author Anuradha.Phalke
 * Purpose: Case Management System Utility class
 * Created Date: 26-03-2021
 * Modification History:     Last Change Date:       Revised By:     Change/Defect description:
 * CR Defect No
 */
@Component
public class ValidationUtil {
	
	static Logger logger=LoggerFactory.getLogger(ValidationUtil.class);
	
	@Autowired
	private EntityManager entityManager;

	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private AuthServices authServices;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	public static void validation(Errors errors) {
		logger.debug("entry -  validation");
		if(errors.hasErrors()) {
			logger.error("errors.hasErrors");
			List<ObjectError> errorsList=errors.getAllErrors();
			StringBuilder sb=new StringBuilder("");
			for (ObjectError objectError : errorsList) {
				sb.append(objectError.getDefaultMessage());
				sb.append(" ");
			}
			logger.error(sb.toString());
			throw new UserManagementException(sb.toString());
		}
		logger.debug("exit -  validation");
	}
	
	    public static HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Frame-Options", "sameorigin");
		headers.add("X-XSS-Protection", "0");
		headers.add("X-Content-Type-Options", "nosniff");
		headers.add("x-Download-options", "noopen");
		headers.add("X-Permitted-Cross-Domain-Policies", "none"); 
		headers.add("Referrer-Policy", "same-origin");
		headers.add("Content-Security-Policy", "script-src 'self'");
		headers.add("Feature-Policy", "microphone 'none'; "
			         + "geolocation 'none'; "
			         + "vibrate 'none'; "
			         + "xr-spatial-trackingExperimental 'none'; "
			         + "publickey-credentials-getExperimental 'none'; "
			         + "legacy-image-formatsExperimental 'none'; "
			         + "picture-in-picture 'none'");
		return headers;
	}
	    
	    public UserInfo getUserInfoFromToken(String authorization) {
	    	UserInfo userDetails = new UserInfo();
	    	if(!StringUtils.isEmpty(authorization)) {
	    	Claims claims = decodeUnsignedJWT(authorization);
            
            List<String> roles = new ArrayList<String>();
            String tokenUser = (String) claims.get("sub");
            if(tokenUser==null) {
                throw new AuthManagementException("User not found in token.");
            }
            List<LinkedHashMap<String, String>> userRoles = (List<LinkedHashMap<String, String>>) claims.get("authorities");
            if (userRoles!= null && userRoles.size()> 0) {
                userDetails.setUserId(tokenUser);
                for (LinkedHashMap<String, String> map : userRoles) {
                	roles.add(map.get("authority").replaceAll("ROLE_", ""));
				}
                LinkedHashMap<String, String> map=userRoles.get(0);
                userDetails.setRoles(roles);
            }else {
                throw new AuthManagementException("Roles not found in token.");
            }
            
	    }
            return userDetails;
        }  
	    
	    
	    public UserInfo getUserInfoFromTokenForLogin(String authorization) {
	    	UserInfo userDetails = new UserInfo();
	    	if(!StringUtils.isEmpty(authorization)) {
	    	Claims claims = decodeUnsignedJWT(authorization);
            
            List<String> roles = new ArrayList<String>();
            String tokenUser = (String) claims.get("sub");
            if(tokenUser==null) {
                throw new AuthManagementException("User not found in token.");
            }
            List<LinkedHashMap<String, String>> userRoles = (List<LinkedHashMap<String, String>>) claims.get("authorities");
            if (userRoles!= null && userRoles.size()> 0) {
                userDetails.setUserId(tokenUser);
                for (LinkedHashMap<String, String> map : userRoles) {
                	roles.add(map.get("authority").replaceAll("ROLE_", ""));
				}
                LinkedHashMap<String, String> map=userRoles.get(0);
                userDetails.setRoles(roles);
            }else {
                throw new AuthManagementException("Roles not found in token.");
            }
            
	    }
            return userDetails;
        } 
		
		private static Claims decodeUnsignedJWT(String authorization) {
			if (authorization == null || (!authorization.startsWith("Bearer ") && !authorization.startsWith("bearer "))) {
				throw new AuthManagementException("No JWT token found in request headers");
			}
			String authToken = authorization.substring(7);
			String[] splitToken = authToken.split("\\.");
			String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";
			Jwt<?, ?> jwt = Jwts.parser().parse(unsignedToken);
			Claims claims = (Claims) jwt.getBody();
			return claims;
		}
	  

		public StatusAndMessage verifyUserTokenAndRole(String token){
			TokenAndUri apiAndToken = new TokenAndUri();
	        apiAndToken.setApiUri(request.getRequestURI());
	        apiAndToken.setMethodType(request.getMethod());
	        apiAndToken.setToken(token);
	        apiAndToken.setHandShakeToken(jwtConfig.getExternalSystemHandShakeToken());
	        apiAndToken.setContextPath(jwtConfig.getContextRootPath());
	        apiAndToken.setIpAddress(request.getRemoteAddr());
			StatusAndMessage statusAndMessage=authServices.verifyUri(apiAndToken);
			return statusAndMessage;
		}
		
}