package org.rebit.auth.config;

import java.util.Date;

import org.rebit.auth.exception.RateLimitManagementException;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class ApiLimitWithTime{
	private Integer apiLimit;
	private Integer currentCount;
	private Date withInTime;
	
	
	
	public Integer getApiLimit() {
		return apiLimit;
	}
	public void setApiLimit(Integer apiLimit) {
		this.apiLimit = apiLimit;
	}
	public Integer getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(Integer currentCount) {
		this.currentCount = currentCount;
	}
	public Date getWithInTime() {
		return withInTime;
	}
	public void setWithInTime(Date withInTime) {
		this.withInTime = withInTime;
	}
	public ApiLimitWithTime(Integer apiLimit, Integer currentCount, Date withInTime) {
		if(apiLimit<currentCount) {
			throw new RateLimitManagementException("User Reachech api limit threshhold");
		}
		this.apiLimit = apiLimit;
		this.currentCount = currentCount;
		this.withInTime = withInTime;
	}
	
} 