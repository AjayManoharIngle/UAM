package org.rebit.auth.service;

import org.rebit.auth.connector.model.ApprovalFlowDto;
import org.rebit.auth.connector.model.DownloadRequestDto;
import org.springframework.http.ResponseEntity;

public interface ApprovalFlowService {

	public void sendForApproval(ApprovalFlowDto approvalFlowDto);
	public ResponseEntity<Object> download(DownloadRequestDto downloadRequestDto, String authorization);
	
}
