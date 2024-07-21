package org.rebit.auth.service;

import org.rebit.auth.administrator.model.EventEntityWithPage;
import org.rebit.auth.administrator.model.GlobalPropertiesDto;
import org.rebit.auth.administrator.model.LogFilesNameDto;
import org.rebit.auth.administrator.model.RunTimeConfigrationChangeDto;
import org.rebit.auth.model.CheckLdapDto;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

public interface RunTimeConfigrationChangeService {
	
	public void changeConfig(RunTimeConfigrationChangeDto runTimeConfigrationChangeDto);
	
	public RunTimeConfigrationChangeDto getConfig();

	public GlobalPropertiesDto getGlobalConfig();

	public void changeGLobalConfig(@Valid GlobalPropertiesDto globalProperties);

	public LogFilesNameDto getLogFileNames();
	
	public ResponseEntity<Object> download(String fileName);
	
	public EventEntityWithPage getEventLogs(int pageNo);
	
	public boolean sendSmsOtpUsingToken(long mobileNumber, String otpValue, String otpExpiryTime);

	public boolean checkLdap(CheckLdapDto checkLdapDto);
}
