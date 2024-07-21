package org.rebit.auth.service.impl;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.config.MasterProperties;
import org.rebit.auth.connector.model.ApprovalFlowDto;
import org.rebit.auth.connector.model.DownloadRequestDto;
import org.rebit.auth.entity.ApprovalDetails;
import org.rebit.auth.repository.ApprovalFlowDetailsRepository;
import org.rebit.auth.service.ApprovalFlowService;
import org.rebit.auth.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ApprovalFlowServiceImpl implements ApprovalFlowService {
	
	final static Logger logger = LogManager.getLogger(ApprovalFlowService.class);
	
	@Autowired
	private ApprovalFlowDetailsRepository approvalFlowDetailsRepository;
	
	@Autowired
	private CommonService commonService;
	
	private MasterProperties masterProperties;

	@Override
	public void sendForApproval(ApprovalFlowDto approvalFlowDto) {
		
		ApprovalDetails approvalDetails = new ApprovalDetails();
		approvalDetails.setAction(getAction(approvalFlowDto));
		approvalDetails.setChecker(getCheckerUser(approvalFlowDto));
		approvalDetails.setCreatedAt(new Date());
		approvalDetails.setCreatorUserId(approvalFlowDto.getCreatedBy());
		approvalDetails.setFlowType(getFlowType(approvalFlowDto));
		approvalDetails.setFlowTypeId(approvalFlowDto.getFlowTypeId());
		approvalDetails.setFlowTypeSubId(approvalFlowDto.getFlowTypeSubId());
		approvalDetails.setMaker(getMaker(approvalFlowDto));
		approvalDetails.setRealm(approvalFlowDto.getRealm());
		if(approvalFlowDto.getDocumentName()!=null && approvalFlowDto.getDocumentName()!="" && approvalFlowDto.getDocument()!=null) {
			approvalDetails.setDocumentName(approvalFlowDto.getDocumentName());
			approvalDetails.setDocument(getDocument(approvalFlowDto.getDocument()));
		}
		
	}

	private byte[] getDocument(MultipartFile document) {
		return commonService.processMultipartFile(document, masterProperties.getDocumentSizeInMb(), masterProperties.getDocumentTypePdf());
	}

	private String getMaker(ApprovalFlowDto approvalFlowDto) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getFlowType(ApprovalFlowDto approvalFlowDto) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getCheckerUser(ApprovalFlowDto approvalFlowDto) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getAction(ApprovalFlowDto approvalFlowDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Object> download(DownloadRequestDto downloadRequestDto,String authorization) {
		ApprovalDetails approvalDetails=approvalFlowDetailsRepository.getById(downloadRequestDto.getId());
		return commonService.download(approvalDetails.getDocument(), approvalDetails.getDocumentName());
	}
	
}
