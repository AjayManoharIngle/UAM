package org.rebit.auth.administrator.model;

import java.util.Date;

import lombok.Data;

/**
 * 
 * @author Kapil Gautam
 * 
 */

@Data
public class EventEntityLogDto {    
  
	private String level;
	private Date eventDate;
	private String message;
	private String logger;
	private String type;
	private String userId;
	private String ipAddress;
	private String request;
	private String applicationName;
	
 
}