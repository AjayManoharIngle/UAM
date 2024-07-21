package org.rebit.auth.service.impl;
import org.apache.tika.Tika;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.service.CommonService;
import org.rebit.auth.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Kapil Gautam
 */

@Service
public class CommonServiceImpl implements CommonService {

	static Logger logger=LoggerFactory.getLogger(CommonServiceImpl.class);
	
	@Override
	public ResponseEntity<Object> download(byte[] document,String documentName) {
		logger.debug("start -  download");
		try {
			HttpHeaders headers = ValidationUtil.getHttpHeaders();
	        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	        headers.add("Pragma", "no-cache");
	        headers.add("Expires", "0");
	        headers.add("Content-disposition", "attachment; filename="+documentName);
	        long contentLength=document.length;
	        ByteArrayResource resource = new ByteArrayResource(document);
	    	logger.debug("exit -  download");
				return ResponseEntity.ok()
				            .headers(headers)
				            .contentLength(contentLength)
				            .contentType(MediaType.APPLICATION_OCTET_STREAM)
				            .body(resource);			
		}catch(Exception e) {
			logger.error("error while downloading file",e);
			throw new UserManagementException("Error while downloading file");
		}
	}
	
	@Override
	public byte[] processMultipartFile(MultipartFile multipartFile,long l,String fileType) {
		logger.debug("entry -  processMultipartFile");
		try {
			byte[] bytesArray = multipartFile.getBytes();
			long fileSize = bytesArray.length;
			String fileName=multipartFile.getOriginalFilename();
			if (fileSize > l * 1000L * 1024L || fileSize < 1000L) {
				throw new UserManagementException(fileName + " File Size :" + fileSize
						+ " Bytes. Maximum File Limit allowd is " + l + " MB and Minimum File Limit is 1KB");
			}
			Tika tika = new Tika();
			String detectedType = tika.detect(bytesArray);
			if (!detectedType.equalsIgnoreCase(fileType)) {
				throw new UserManagementException("Invalid File Type " + detectedType + ". Please upload "+fileType+" files only");
			}
			logger.debug("exit -  processMultipartFile");
			return bytesArray;			
		}catch(Exception e) {
			logger.error("error while processing multipart file"+e);
			throw new UserManagementException("Error while upload file.");
		}
	}


}