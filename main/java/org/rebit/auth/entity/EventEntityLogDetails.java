package org.rebit.auth.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 
 * @author Kapil Gautam
 * 
 */

@Data
@Entity
@Table(name = "tbl_Event_Entity")
public class EventEntityLogDetails {    
  
	@Id
	@Column(name = "N_EVENT_ID")
	private long logId;
	@Column(name = "S_LEVEL")
	private String level;
	@Column(name = "DT_EVENTDATE")
	private Date eventDate;
	@Column(name = "S_MESSAGE")
	private String message;
	@Column(name = "S_LOGGER")
	private String logger;
	@Column(name = "S_TYPE")
	private String type;
	@Column(name = "S_USER_ID")
	private String userId;
	@Column(name = "S_IP_ADDRESS")
	private String ipAddress;
	@Column(name = "S_JSON_REQUEST")
	private String request;
	@Column(name = "S_APPLICATION_NAME")
	private String applicationName;
	
 
}