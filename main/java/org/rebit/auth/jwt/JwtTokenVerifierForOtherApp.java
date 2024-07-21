package org.rebit.auth.jwt;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.util.LoggerUtil;
import org.rebit.auth.util.ReBITUserManagementConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenVerifierForOtherApp {

	final static Logger logger = LogManager.getLogger();
	
	@Autowired
	private SecretKey secretKey;
	
	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	public Set<String> validate(String token,String ipAddress) {
		logger.info("Entry into validate");
		String username = null;
		try {
			if (!StringUtils.isEmpty(token)) {
				String orgToken = token.replace(jwtConfig.getTokenPrefix(), "");
				Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(orgToken);

				Claims body = claimsJws.getBody();
				username = body.getSubject();

				List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

				Set<String> authority = authorities.stream().map(m -> new String(m.get("authority")))
						.collect(Collectors.toSet());
				logger.info("Exit from validate");
				loggerUtil.printSuccessLogs(logger, ReBITUserManagementConstant.VALID_JWT_TOKEN, username, ipAddress, token, "jwt token successfully validated");
				return authority;
			} else {
				logger.info("Token not present in request");
				loggerUtil.printSuccessLogs(logger, ReBITUserManagementConstant.JWT_TOKEN_NOT_PRESENT_IN_REQUEST, "", ipAddress, token, "Token not present in request");
				return null;
			}
		} catch (JwtException e) {
			loggerUtil.printFailedLogs(logger, ReBITUserManagementConstant.INVALID_JWT_TOKEN, "", ipAddress, token, String.format("Token %s cannot be trusted", token));
			logger.error(String.format("Token %s cannot be trusted", token), e);
			throw new UserManagementException(String.format("Token %s cannot be trusted", token));
		}

	}
}
