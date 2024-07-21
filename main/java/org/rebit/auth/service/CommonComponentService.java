package org.rebit.auth.service;

import java.util.List;

import org.rebit.auth.entity.OfficeLocationDetails;
import org.rebit.auth.entity.RegionalOfficeDetails;
import org.rebit.auth.entity.RoleMastDetails;
import org.rebit.auth.entity.UserMaster;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Piyush.Pandey
 * Purpose: Common Component Service
 * Created Date: 10-08-2021
 * Modification History:     Last Change Date:       Revised By:     Change/Defect description:
 * CR Defect No
 */

public interface CommonComponentService {


	OfficeLocationDetails getOfficeLocationDetailsById(String id);

	String encryptId(long data);

	long decryptId(String data);

	List<Long> decryptId(List<String> list);

	void saveFileAsBase64(String filePath, String encodedString, String fileName, String folderName,
			String subFolderName, int documentSize);

	ResponseEntity<Object> download(String filePath, String identificationNumber, String fileNameWithExtension);

	UserMaster getEncryptedUserDetailsById(String id);

	RoleMastDetails getRoleMasterDetailsById(String roleId);

	String encrypt(String data);

	String decrypt(String data);

	RegionalOfficeDetails getRegionalOfficeDetailsById(String id);

	UserMaster getUserDetailsById(Long id);

	UserMaster getUserDetailsByUserName(String userName, boolean statusCheck);

	byte[] processMultipartFile(MultipartFile multipartFile, int documentSize, String fileType) throws Exception;

	ResponseEntity<Object> download(byte[] document, String documentName);
}