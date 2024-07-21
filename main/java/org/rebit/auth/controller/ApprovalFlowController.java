package org.rebit.auth.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.connector.model.DownloadRequestDto;
import org.rebit.auth.service.ApprovalFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 
 * @author kapil gautam
 * 
 */

@RestController
@RequestMapping("/approval-flow")
public class ApprovalFlowController {
	
	final static Logger logger = LogManager.getLogger(ApprovalFlowController.class);
	
	@Autowired
	private ApprovalFlowService approvalFlowService;
	
	@PostMapping("/user-certification/requests/download")
	public ResponseEntity<Object> download(@RequestBody @Valid DownloadRequestDto downloadRequestDto,@RequestHeader("authorization") String authorization) {
			return approvalFlowService.download(downloadRequestDto,authorization);
	}

}
