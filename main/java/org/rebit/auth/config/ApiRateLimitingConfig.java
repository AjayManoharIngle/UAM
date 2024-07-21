package org.rebit.auth.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.rebit.auth.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.ApplicationScope;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

@Service
@ApplicationScope
@Lazy(false)
public class ApiRateLimitingConfig {
	
	private String apiLimitBy;
	private Integer apiLimit;
	private Integer limitTimeInSectond;
	
	@Autowired
	private JwtConfig jwtConfig;

	private void init(){

		if(apiLimit==null && limitTimeInSectond==null) {
			String[] apiLimitAndTime = jwtConfig.getApiRateLimitCommaTime().split(",");
			this.apiLimit=Integer.valueOf(apiLimitAndTime[0]);
			this.limitTimeInSectond=Integer.valueOf(apiLimitAndTime[1]);
			}
			if(StringUtils.isEmpty(apiLimitBy)) {
			this.apiLimitBy = jwtConfig.getApiRateLimitBy();
			}
		}
	
	Map<String, ApiLimitWithTime> map = new ConcurrentHashMap<>();
	
	public void checkApiLimitByUser(String authorization,String ipAddress) {
		
		String username = getUserName(authorization);
		
		if(StringUtils.isEmpty(username)) {
			username = ipAddress;
		}
		if(map.get(username)!=null) {
			ApiLimitWithTime apiLimitWithTime = map.get(username);
			if(apiLimitWithTime.getWithInTime().after(new Date()) || apiLimitWithTime.getWithInTime().equals(new Date())) {
				map.put(username, new ApiLimitWithTime(apiLimit,apiLimitWithTime.getCurrentCount()+1,apiLimitWithTime.getWithInTime()));
			}else {
				map.put(username, new ApiLimitWithTime(apiLimit,1,tokenExpireDate(limitTimeInSectond)));
			}
		}else {
			map.put(username, new ApiLimitWithTime(apiLimit,1,tokenExpireDate(limitTimeInSectond)));
		}
	}

	
	public void checkApiLimitByIPAddress(String ipAddress) {
		String username = ipAddress;
		
		if(map.get(username)!=null) {
			ApiLimitWithTime apiLimitWithTime = map.get(username);
			if(apiLimitWithTime.getWithInTime().before(new Date()) || apiLimitWithTime.getWithInTime().equals(new Date())) {
				map.put(username, new ApiLimitWithTime(apiLimit,apiLimitWithTime.getCurrentCount()+1,apiLimitWithTime.getWithInTime()));
			}else {
				map.put(username, new ApiLimitWithTime(apiLimit,1,tokenExpireDate(limitTimeInSectond)));
			}
		}else {
			map.put(username, new ApiLimitWithTime(apiLimit,1,tokenExpireDate(limitTimeInSectond)));
		}
	}
	
	public void checkApiLimitByTotalCount() {
		String username = "TotalCount";
		
		if(map.get(username)!=null) {
			ApiLimitWithTime apiLimitWithTime = map.get(username);
			if(apiLimitWithTime.getWithInTime().before(new Date()) || apiLimitWithTime.getWithInTime().equals(new Date())) {
				map.put(username, new ApiLimitWithTime(apiLimit,apiLimitWithTime.getCurrentCount()+1,apiLimitWithTime.getWithInTime()));
			}else {
				map.put(username, new ApiLimitWithTime(apiLimit,1,tokenExpireDate(limitTimeInSectond)));
			}
		}else {
			map.put(username, new ApiLimitWithTime(apiLimit,1,tokenExpireDate(limitTimeInSectond)));
		}
	}

	private String getUserName(String authorization) {
		if (authorization == null || (!authorization.startsWith("Bearer ") && !authorization.startsWith("bearer "))) {
			return null;
		}
		String authToken = authorization.substring(7);
		String[] splitToken = authToken.split("\\.");
		String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";
		Jwt<?, ?> jwt = Jwts.parser().parse(unsignedToken);
		Claims claims = (Claims) jwt.getBody();
		List<String> tokenRoles = (List<String>) claims.get("Roles");
		String userName = (String) claims.get("preferred_username");
		return userName;
	}
	
	public  Date tokenExpireDate(int second) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date currentDate = new Date();
        System.out.println(dateFormat.format(currentDate));
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.SECOND, second);
        Date currentDatePlusOne = c.getTime();
        return currentDatePlusOne;
    }
	
	public void rateLimitCheck(String token,String ipAddress){
		init();
		switch (apiLimitBy) {
		case "userName":
			checkApiLimitByUser(token,ipAddress);
			break;
		case "ipAddress":
			checkApiLimitByIPAddress(ipAddress);
			break;
		case "totalCount":
			checkApiLimitByTotalCount();
			break;
		}
	}
	
	@Scheduled(fixedDelay = 100)
	public void nightJobToClearRateLimitCache(){
		map.clear();
	}

}
