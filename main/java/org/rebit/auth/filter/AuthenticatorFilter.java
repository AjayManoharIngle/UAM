package org.rebit.auth.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rebit.auth.config.ApiRateLimitingConfig;
import org.rebit.auth.config.GlobalProperties;
import org.rebit.auth.exception.RateLimitManagementException;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.model.StatusAndMessage;
import org.rebit.auth.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class AuthenticatorFilter  implements Filter {

	static Logger logger=LoggerFactory.getLogger(AuthenticatorFilter.class);
	
	private String apiLimitBy;
	
	@Autowired
	private ValidationUtil validationUtil;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	@Autowired
	private GlobalProperties globalProperties;
	
	@Autowired
	private ApiRateLimitingConfig apiRateLimitingConfig;
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("doFilter - entry ");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res=(HttpServletResponse) response;
		
		boolean isNotPresent;
		
		try {
			isNotPresent = defaultPublicUrl(request, response, chain, req, res);
			if (isNotPresent) {
				if(!"OPTIONS".equalsIgnoreCase(req.getMethod())) {
					StatusAndMessage statusAndMessage=validationUtil.verifyUserTokenAndRole(req.getHeader("Authorization"));
						if(statusAndMessage.getHttpStatus().equals(HttpStatus.FORBIDDEN)) {
							res.sendError(403, statusAndMessage.getMessage());
						}else {
							chain.doFilter(request, response);
							logger.debug("doFilter - exit");
					}	
				}else {
					chain.doFilter(request, response);
					logger.debug("doFilter - exit");
				 }
				}
		} catch (RateLimitManagementException e) {
			res.sendError(429, e.getMessage());
		}
	}


	private boolean defaultPublicUrl(ServletRequest request, ServletResponse response, FilterChain chain,
			HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		
		AntPathMatcher matcher = rateLimitCheck(request, req, res);
		
		boolean isNotPresent = true;
		
		List<String> publicURLs = new ArrayList<>();
		publicURLs.add("/verifaction");
		publicURLs.add("/refresh");
		publicURLs.add("/login");
		publicURLs.add("/captcha");
		publicURLs.add("/key");
		
		
		for (Iterator iterator = publicURLs.iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			
			if(!StringUtils.isEmpty(jwtConfig.getContextRootPath())) {
				if(matcher.match(jwtConfig.getContextRootPath()+url, req.getRequestURI())) {
					chain.doFilter(request, response);
					isNotPresent = false;
					break;
				}
			}else {
				if(matcher.match(url, req.getRequestURI())) {
					chain.doFilter(request, response);
					isNotPresent = false;
					break;
				}
			}
		}
		return isNotPresent;
	}

	private AntPathMatcher rateLimitCheck(ServletRequest request, HttpServletRequest req,HttpServletResponse res) throws IOException{
		AntPathMatcher matcher = new AntPathMatcher();
		String token;
		String ipAddress = request.getRemoteAddr();
		if(matcher.match(jwtConfig.getContextRootPath()+"/verifaction", req.getRequestURI())) {
			token=req.getHeader("CustomAuthorization");
			ipAddress = req.getHeader("CustomIpAddress");
		}else {
			token=req.getHeader("Authorization");
		}
		apiRateLimitingConfig.rateLimitCheck(token, ipAddress);
		return matcher;
		
	}

}

