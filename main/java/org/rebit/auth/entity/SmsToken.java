package org.rebit.auth.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author amit.hazare
 * 03-Jun-2021
 */
@Table(name = "SMS_TOKEN")
@Entity
public class SmsToken {

	@Id
	@Column(name = "VALUE")
	private String value;

	@Column(name = "EXPIRY_DATE")
	private Date expiryDate;

	public SmsToken() {
	}

	public SmsToken(String value, Date expiryDate) {
		this.value = value;
		this.expiryDate = expiryDate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
