package org.rebit.auth.connector.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ApprovalFlowDto {
	
	private String flowType;
	private Long flowTypeId;
	private Long flowTypeSubId;
	private String documentName;
	private MultipartFile document;
	private String realm;
	private String createdBy;
	private String creatorRole;
}
