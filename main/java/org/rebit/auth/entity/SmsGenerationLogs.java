package org.rebit.auth.entity;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author amit.hazare
 * 03-Jun-2021
 */
@Table(name = "SMS_GENERATION_LOGS")
@Entity
public class SmsGenerationLogs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "MOBILE_NO")
	private String mobileNo;
	
	@Column(name = "TEXT")
	private String text;

	@Column(name = "SENT_ON")
	private Date sentOn;
	
	@Column(name = "STATUS")
	private boolean status;

	public SmsGenerationLogs() {
	}

	public SmsGenerationLogs(String mobileNo, String text, Date sentOn, boolean status) {
		this.mobileNo = mobileNo;
		this.text = text;
		this.sentOn = sentOn;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getSentOn() {
		return sentOn;
	}

	public void setSentOn(Date sentOn) {
		this.sentOn = sentOn;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}	
	
}
