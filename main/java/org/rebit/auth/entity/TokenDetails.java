package org.rebit.auth.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Table(name = "TBL_TOKEN")
@Entity
public class TokenDetails	 {
			
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "N_REFRESHTOKENID")
	private long id;

	@Column(name = "S_USERNAME")
	private String userName;

	@Column(name = "S_TOKEN")
	private String token;
	
	@Column(name = "S_TYPE")
	private String type;

	@Column(name = "DT_CREATEDDATE")
	private Date createdDate;

	@Column(name = "DT_EXPIRATIONDATE")
	private Date expirationDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
}
