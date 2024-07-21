package org.rebit.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


@Table(name = "KAVACH_OTP_LOGS")
@Entity
public class OTPLogsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="OtlSysId")
	@SequenceGenerator(sequenceName = "OTL_SYS_ID",allocationSize = 1, name = "OtlSysId")
	@Column(name = "OTL_SYS_ID")
	private long id;

	@Column(name = "OTL_TOKEN")
	private String token;

	@Column(name = "OTL_OTP")
	private String otpValue;

	@Column(name = "OTL_MOBILE_NUMBER")
	private long mobileNumber;
	
	@Column(name = "OTL_KEY")
	private String key;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOtpValue() {
		return otpValue;
	}

	public void setOtpValue(String otpValue) {
		this.otpValue = otpValue;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
