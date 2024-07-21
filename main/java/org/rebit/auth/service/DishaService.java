package org.rebit.auth.service;

import org.rebit.auth.disha.entity.FileUpload;
import org.rebit.auth.disha.model.RegistrationDto;
import org.springframework.web.multipart.MultipartFile;

public interface DishaService {

	public void registration(RegistrationDto registrationDto);

	public RegistrationDto registrationRetrieve(String projectName, String authorization);
	
	public FileUpload uploadFile(MultipartFile partFile);
}
