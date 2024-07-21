package org.rebit.auth.service.impl;

import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.disha.entity.FileUpload;
import org.rebit.auth.disha.entity.ProjectRegistration;
import org.rebit.auth.disha.entity.TeamDetails;
import org.rebit.auth.disha.model.RegistrationDto;
import org.rebit.auth.disha.model.SupportUser;
import org.rebit.auth.disha.repository.FileUploadRepository;
import org.rebit.auth.disha.repository.ProjectRegistrationRepository;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.service.DishaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DishaServiceImpl implements DishaService {
	
	final static Logger logger = LogManager.getLogger(DishaServiceImpl.class);
	
	@Autowired
	private ProjectRegistrationRepository projectRegistrationRepository;
	
	@Autowired
	private CommonServiceImpl commonService;
	
	@Autowired
	private FileUploadRepository fileUploadRepository;

	@Override
	public void registration(RegistrationDto registrationDto) {
		logger.trace("Entry into project registration");
		try {
			ProjectRegistration projectRegistration = new ProjectRegistration();
			projectRegistration.setProjectTitle(registrationDto.getProjectTitle());
			projectRegistration.setProejctDesc(registrationDto.getProejctDesc());
			projectRegistration.setProjectManagerFirstName(registrationDto.getProjectManagerFirstName());
			projectRegistration.setProjectManagerLastName(registrationDto.getProjectManagerLastName());
			projectRegistration.setProjectManagerContactNo(registrationDto.getProjectManagerContactNo());
			projectRegistration.setProjectManagerEmail(registrationDto.getProjectManagerEmail());
			projectRegistration.setProjectManagerRole("ADMIN");
			projectRegistration.setDepartment(registrationDto.getDepartment());
			projectRegistration.setDepartmentBOName(registrationDto.getDepartmentBOName());
			
			if(registrationDto.getTeamDetails()!=null) {
				ArrayList<TeamDetails> supportUsers = new ArrayList<>();
				for(SupportUser supportUser : registrationDto.getTeamDetails()) {
					TeamDetails teamDetails = new TeamDetails();
					teamDetails.setTeamMemberName(supportUser.getTeamMemberName());
					teamDetails.setTeamMemberRole(supportUser.getTeamMemberRole());
					teamDetails.setTeamMemberEmail(supportUser.getTeamMemberEmail());
					teamDetails.setTeamMemberContactNo(supportUser.getTeamMemberContactNo());
					supportUsers.add(teamDetails);
				}
				projectRegistration.setTeamDetails(supportUsers);
			}
			
			FileUpload getUploadProjectPrerequisite=uploadFile(registrationDto.getUploadProjectPrerequisite());
			projectRegistration.setUploadProjectPrerequisite(getUploadProjectPrerequisite);
			
			FileUpload getUploadProjectSRS=uploadFile(registrationDto.getUploadProjectSRS());
			projectRegistration.setUploadProjectPrerequisite(getUploadProjectSRS);

			FileUpload getUploadProjectSWO=uploadFile(registrationDto.getUploadProjectSWO());
			projectRegistration.setUploadProjectPrerequisite(getUploadProjectSWO);

			projectRegistrationRepository.save(projectRegistration);
		}catch(Exception e) {
			logger.error("error while registartion of project"+e);
			throw new UserManagementException("Error while registartion of project.");
		}
		logger.trace("Exit from registration");
	}
	
	@Override
	public FileUpload uploadFile(MultipartFile partFile) {
		logger.trace("Entry into upload file");
		FileUpload file = null;
		try {
			byte[] fileContent = null;
			if(partFile != null) {
				fileContent = commonService.processMultipartFile(partFile, partFile.getSize(), partFile.getContentType());
			}
			
			file = new FileUpload();
			file.setFileContent(fileContent);
			file.setFileName(partFile.getOriginalFilename());
			file.setFileSize(partFile.getSize());
			file.setFileType(partFile.getContentType());
			file.setFileUploadTime(new Date());
			
			fileUploadRepository.save(file);
		}
		catch(Exception e) {
			logger.error("error while uploading multipart file"+e);
			throw new UserManagementException("Error while uploading file.");
		}
		logger.trace("Exit from upload file");
		return file;
	}
	
	@Override
	public RegistrationDto registrationRetrieve(String projectName, String authorization) {
		
		return null;
	}
}
