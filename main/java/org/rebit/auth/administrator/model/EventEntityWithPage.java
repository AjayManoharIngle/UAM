package org.rebit.auth.administrator.model;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author Kapil Gautam
 * 
 */

@Data
public class EventEntityWithPage {    
	
	private List<EventEntityLogDto> records;
	private Integer totalPage;
	
 
}