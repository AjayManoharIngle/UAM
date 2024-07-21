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
@Table(name = "SMS_TOKEN_GENERATION_LOGS")
@Entity
public class SmsTokenGenerationLogs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "ATTEMPT_DATE")
	private Date attemptDate;
	
	@Column(name = "STATUS")
	private boolean status;

	public SmsTokenGenerationLogs() {
	}

	public SmsTokenGenerationLogs(Date attemptDate, boolean status) {
		this.attemptDate = attemptDate;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getAttemptDate() {
		return attemptDate;
	}

	public void setAttemptDate(Date attemptDate) {
		this.attemptDate = attemptDate;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
