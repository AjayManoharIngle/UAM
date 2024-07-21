package org.rebit.auth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Kapil Gautam
 */

public interface CommonService {

	public byte[] processMultipartFile(MultipartFile multipartFile, long documentSize, String fileType);

	public ResponseEntity<Object> download(byte[] document, String documentName);

}