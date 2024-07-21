package org.rebit.auth.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rebit.auth.administrator.model.AddOfficeLocationDetailsDto;
import org.rebit.auth.administrator.model.AddRegionalLocationDetailsDto;
import org.rebit.auth.administrator.model.AddRoleDetailsDto;
import org.rebit.auth.administrator.model.GetOfficeLocationDetailsDto;
import org.rebit.auth.administrator.model.GetRoleDetailsDto;
import org.rebit.auth.administrator.model.OfficeLocationParamDto;
import org.rebit.auth.administrator.model.RegionalLocationDetailsDto;
import org.rebit.auth.administrator.model.RegionalOfficeParamDto;
import org.rebit.auth.administrator.model.RolesParamDto;
import org.rebit.auth.administrator.model.UpdateStatusDto;
import org.rebit.auth.administrator.model.UserDetailsDto;
import org.rebit.auth.connector.model.EmployeeSearchDto;
import org.rebit.auth.connector.model.UpdateUserProfileInfo;
import org.rebit.auth.entity.OfficeLocationDetails;
import org.rebit.auth.entity.RegionalOfficeDetails;
import org.rebit.auth.entity.RoleMastDetails;
import org.rebit.auth.entity.UserMaster;
import org.rebit.auth.exception.AuthManagementException;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.model.UserInfo;
import org.rebit.auth.repository.OfficeLocationDetailsRepository;
import org.rebit.auth.repository.RegionalOfficeDetailsRepository;
import org.rebit.auth.repository.RoleDetailsRepository;
import org.rebit.auth.repository.UserMasterRepository;
import org.rebit.auth.util.AuthKavachUtil;
import org.rebit.auth.util.KavachUtil;
import org.rebit.auth.util.RSAUtil;
import org.rebit.auth.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.validation.Valid;

@Component
public class AdministratorMapper {
	
	
	private static final Integer STATUS_INT_ACTIVE = 1;
	private static final String STATUS_STRING_INACTIVE = "INACTIVE";
	
	@Autowired
	private RSAUtil rsaUtil;
	
	@Autowired
	private RoleDetailsRepository roleDetailsRepository;
	
	@Autowired
	private OfficeLocationDetailsRepository officeLocationDetailsRepository;
	
	@Autowired
	private RegionalOfficeDetailsRepository regionalOfficeDetailsRepository;
	
	@Autowired
	private UserMasterRepository userMasterRepository;
	
	@Autowired
	private AuthKavachUtil authKavachUtil;

	
	@Autowired
	private ValidationUtil validationUtil;
	
	public RolesParamDto mapParamToSearchRole() {
		Set<String> roleName = new HashSet<String>();
		Set<String> status = new HashSet<String>();
		List<RoleMastDetails> roleDetailsList=roleDetailsRepository.findAll();
		roleDetailsList.forEach(roleDetails ->{
			roleName.add(roleDetails.getRoleName());
			status.add(KavachUtil.convertStatusIntToString(roleDetails.getStatus()));
		});
		RolesParamDto rolesParamDto = new RolesParamDto();
		rolesParamDto.setRoleName(roleName);
		rolesParamDto.setStatus(status);
		return rolesParamDto;
	}
	
	public List<GetRoleDetailsDto> mapRoleSearchDetails(UserInfo userInfo) {
		List<String> roles=new ArrayList<String>();
		if(userInfo!=null) {
			roles=userInfo.getRoles();
		}
		List<RoleMastDetails> roleList =roleDetailsRepository.findAll();
		List<GetRoleDetailsDto> searchRoleDtoList = new ArrayList<GetRoleDetailsDto>();
		roleList.forEach(roleDetails -> {
			GetRoleDetailsDto dto = new GetRoleDetailsDto();
			dto.setRoleId(roleDetails.getRoleId());
			dto.setRoleName(roleDetails.getRoleName());
			dto.setStatus(KavachUtil.convertStatusIntToString(roleDetails.getStatus()));
			searchRoleDtoList.add(dto);
		});
		return searchRoleDtoList;
	}
	
	public List<GetRoleDetailsDto> mapRoleSearchDetails() {
		List<RoleMastDetails> roleList=roleDetailsRepository.findAll();
		List<GetRoleDetailsDto> searchRoleDtoList = new ArrayList<GetRoleDetailsDto>();
		if(roleList!=null) {
		roleList.forEach(roleDetails -> {
			GetRoleDetailsDto dto = new GetRoleDetailsDto();
			dto.setRoleId(roleDetails.getRoleId());
			dto.setRoleName(roleDetails.getRoleName());
			dto.setStatus(KavachUtil.convertStatusIntToString(roleDetails.getStatus()));
			searchRoleDtoList.add(dto);
		});
		}
		return searchRoleDtoList;
	}
	
	public void updateRoles(String roleName, UserInfo userInfo, @Valid UpdateStatusDto updateRoleDetailsDto) {
		RoleMastDetails roleDetails=roleDetailsRepository.findByRoleName(roleName);
		if(roleDetails!=null) {
			roleDetails.setStatus(KavachUtil.convertStatusStringToInt(updateRoleDetailsDto.getStatus()));
			roleDetails.setUpdateBy(userInfo.getUserId());
			roleDetails.setUpdatedOn(new Date());
			roleDetailsRepository.save(roleDetails);
		}else {
			throw new UserManagementException("Invalid RoleName, No Record found with "+roleName);
		}
		
	}
	
	public void addRoles(AddRoleDetailsDto addRoleDetailsDto, UserInfo userInfo) {
		RoleMastDetails roleDetails=roleDetailsRepository.findByRoleName(addRoleDetailsDto.getRoleName());
		if(roleDetails!=null) {
			throw new UserManagementException("RoleName "+addRoleDetailsDto.getRoleName()+" already present in system");
		}else {
			RoleMastDetails addRoles = new RoleMastDetails();
			addRoles.setRoleName(addRoleDetailsDto.getRoleName());
			addRoles.setStatus(KavachUtil.convertStatusStringToInt(addRoleDetailsDto.getStatus()));
			addRoles.setCreatedBy(userInfo.getUserId());
			addRoles.setCreatedOn(new Date());
			roleDetailsRepository.save(addRoles);
		}
		
	}
	
	
	public OfficeLocationParamDto mapParamToSearchOfficeLocation() {
		// TODO Auto-generated method stub
		Set<String> offLongName = new HashSet<String>();
		Set<String> offShortName = new HashSet<String>();
		Set<String> status = new HashSet<String>();
		List<OfficeLocationDetails> officeLocationDetailsList=officeLocationDetailsRepository.findAll();
		officeLocationDetailsList.forEach(offDetails ->{
			offLongName.add(offDetails.getLocationLongName());
			offShortName.add(offDetails.getLocationShortName());
			status.add(KavachUtil.convertStatusIntToString(offDetails.getStatus()));
		});
		OfficeLocationParamDto officeLocationParamDto = new OfficeLocationParamDto();
		officeLocationParamDto.setOfficeLocationName(offLongName);
		officeLocationParamDto.setOfficeLocationShortName(offShortName);
		officeLocationParamDto.setStatus(status);
		return officeLocationParamDto;
	}
	
	public List<GetOfficeLocationDetailsDto> mapOfficeLocationSearchDetails(UserInfo userInfo) {
		
		List<OfficeLocationDetails> officeLocationList = officeLocationDetailsRepository.findAll();

		List<GetOfficeLocationDetailsDto> searchOffDtoList = new ArrayList<GetOfficeLocationDetailsDto>();
		officeLocationList.forEach(refDetails -> {
			GetOfficeLocationDetailsDto dto = new GetOfficeLocationDetailsDto();
			dto.setOfficeLocationId(refDetails.getOfficeLocationId());
			dto.setOfficeLocationName(refDetails.getLocationLongName());
			dto.setOfficeLocationShortName(refDetails.getLocationShortName());
			dto.setStatus(KavachUtil.convertStatusIntToString(refDetails.getStatus()));
			searchOffDtoList.add(dto);
		});
		return searchOffDtoList;
	}
	
	public void updateOfficeLoc(UserInfo userInfo, String officeLocationName,AddOfficeLocationDetailsDto updateOfficeLocationDetailsDto) {
		OfficeLocationDetails officeLocationDetails=officeLocationDetailsRepository.findByLocationLongName(officeLocationName);
		if(officeLocationDetails!=null) {
			officeLocationDetails.setUpdatedAt(new Date());
			officeLocationDetails.setUpdaterUserId(userInfo.getUserId());
			officeLocationDetails.setStatus(KavachUtil.convertStatusStringToInt(updateOfficeLocationDetailsDto.getStatus()));
			officeLocationDetailsRepository.save(officeLocationDetails);
		}else {
			throw new UserManagementException("Invalid OfficeLocationName, No Record found with "+officeLocationName);
		}
	}
	
	public void addOfficeLoc(AddOfficeLocationDetailsDto addOfficeLocationDetailsDto, UserInfo userInfo) {
		OfficeLocationDetails officeLocationDetails=officeLocationDetailsRepository.findByLocationLongName(addOfficeLocationDetailsDto.getOfficeLocationName());
		if(officeLocationDetails!=null) {
			throw new UserManagementException("OfficeLocationName "+addOfficeLocationDetailsDto.getOfficeLocationShortName()+" already present in system");
		}else {
			OfficeLocationDetails addOfficeLocation = new OfficeLocationDetails();
			addOfficeLocation.setCreatedAt(new Date());
			addOfficeLocation.setCreatorUserId(userInfo.getUserId());
			addOfficeLocation.setLocationLongName(addOfficeLocationDetailsDto.getOfficeLocationName());
			addOfficeLocation.setLocationShortName(addOfficeLocationDetailsDto.getOfficeLocationShortName());
			addOfficeLocation.setStatus(KavachUtil.convertStatusStringToInt(addOfficeLocationDetailsDto.getStatus()));
			officeLocationDetailsRepository.save(addOfficeLocation);
		}
	}
	
	
	public RegionalOfficeParamDto mapParamToSearchRegional() {
		Set<String> code = new HashSet<String>();
		Set<String> name = new HashSet<String>();
		//Set<String> status = new HashSet<String>();
		List<RegionalOfficeDetails> officeLocationDetailsList=regionalOfficeDetailsRepository.findAll();
		officeLocationDetailsList.forEach(offDetails ->{
			code.add(offDetails.getRoCode());
			name.add(offDetails.getRoName());
		});
		RegionalOfficeParamDto officeLocationParamDto = new RegionalOfficeParamDto();
		officeLocationParamDto.setCode(code);
		officeLocationParamDto.setName(name);
		//officeLocationParamDto.setStatus(status);
		return officeLocationParamDto;
	}
	
	public List<RegionalLocationDetailsDto> mapRegionalLocationSearchDetails() {
		List<RegionalOfficeDetails> officeLocationList=regionalOfficeDetailsRepository.findAll();
		List<RegionalLocationDetailsDto> searchOffDtoList = new ArrayList<RegionalLocationDetailsDto>();
		officeLocationList.forEach(refDetails -> {
			RegionalLocationDetailsDto dto = new RegionalLocationDetailsDto();
			dto.setCode(refDetails.getRoCode());
			dto.setName(refDetails.getRoName());
			searchOffDtoList.add(dto);				
		});
		return searchOffDtoList;
	}
	
	public void updateRegionalOffice(UserInfo userInfo, String regionalOfficeName,AddRegionalLocationDetailsDto updateOfficeLocationDetailsDto) {
		RegionalOfficeDetails regionalOfficeDetails=regionalOfficeDetailsRepository.findByRoName(regionalOfficeName);
		if(regionalOfficeDetails!=null) {
			regionalOfficeDetailsRepository.save(regionalOfficeDetails);
		}else {
			throw new UserManagementException("Invalid regionalOfficeName, No Record found with "+regionalOfficeName);
		}
	}

	public void addRegionalOffice(AddRegionalLocationDetailsDto addRegionalLocationDetailsDto, UserInfo userInfo) {
		RegionalOfficeDetails regionalOfficeDetails=regionalOfficeDetailsRepository.findByRoName(addRegionalLocationDetailsDto.getName());
		if(regionalOfficeDetails!=null) {
			throw new UserManagementException("RegionalLocationName "+addRegionalLocationDetailsDto.getName()+" already present in system");
		}else {
			RegionalOfficeDetails addRegionalLocation = new RegionalOfficeDetails();
			addRegionalLocation.setCreatedOn(new Date());
			addRegionalLocation.setCreatedBy(userInfo.getUserId());
			addRegionalLocation.setRoCode(addRegionalLocationDetailsDto.getCode());
			addRegionalLocation.setRoName(addRegionalLocationDetailsDto.getName());
			addRegionalLocation.setUpdatedBy(userInfo.getUserId());
			addRegionalLocation.setUpdatedOn(new Date());
			regionalOfficeDetailsRepository.save(addRegionalLocation);
		}
		
	}

	public void createUserMapping(UserDetailsDto userDetailsDto, UserInfo userInfo,boolean withRole) {
		
		List<UserMaster> userMasterByEmailId = userMasterRepository.findByUserEmailIdIgnoreCaseOrUserNameIgnoreCaseOrUserMobileNo(userDetailsDto.getUserEmailId(),userDetailsDto.getUserName(),userDetailsDto.getUserMobileNo());
		
		for (UserMaster userMaster : userMasterByEmailId) {
			
			if(userMaster.getUserName().equalsIgnoreCase(userDetailsDto.getUserName())){
				throw new UserManagementException("user already present with same userName, Please user different userName");
			}
			
			if(userMaster.getUserEmailId().equalsIgnoreCase(userDetailsDto.getUserEmailId())) {
				throw new UserManagementException("user already present with same emailId, Please user different emailId");
			}
			
			if(userMaster.getUserMobileNo().equals(userDetailsDto.getUserMobileNo())) {
				throw new UserManagementException("user already present with same mobileNumber, Please user different mobileNumber");
			}
	
		}
		
		UserMaster userDetails = new UserMaster();
		userDetails.setCreatedAt(new Date());
		userDetails.setCreatorUserId(userInfo.getUserId());
		userDetails.setFirstName(userDetailsDto.getFirstName());
		userDetails.setLastName(userDetailsDto.getLastName());
		
		if(!StringUtils.isEmpty(userDetailsDto.getOfficeLocationDetails())) {
			OfficeLocationDetails officeDetail=officeLocationDetailsRepository.findByLocationLongNameAndStatus(userDetailsDto.getOfficeLocationDetails(), STATUS_INT_ACTIVE);
			
			if(officeDetail==null) {
				throw new UserManagementException("Invalid OfficeLocation");
			}
			userDetails.setOfficeLocationDetails(officeDetail);
		}
			try {
				userDetailsDto.setPassCode(rsaUtil.decrypt(userDetailsDto.getPassCode()));
			} catch (Exception e) {
				throw new AuthManagementException("Error while decrypting passcode");
			}
		
		if(0L == userDetailsDto.getIsLdapUser()) {
		userDetails.setPassCode(authKavachUtil.passwordPolicyCheck(userDetailsDto.getPassCode(),userDetails));
		}
		
		if(!StringUtils.isEmpty(userDetailsDto.getRegionalOfficeDetails())) {
			RegionalOfficeDetails regionalOfficeDetails = regionalOfficeDetailsRepository.findByRoName(userDetailsDto.getRegionalOfficeDetails());
			
			if(regionalOfficeDetails==null) {
				throw new UserManagementException("Invalid RegionalOffice");
			}
			
			userDetails.setRegionalOfficeDetails(regionalOfficeDetails);
		}
		
		if (withRole) {
			List<RoleMastDetails> rolesDetails = new ArrayList<RoleMastDetails>();
			userDetailsDto.getRoles().forEach(role -> {
				RoleMastDetails roleDetails = roleDetailsRepository.findByRoleName(role);
				if (roleDetails == null) {
					throw new UserManagementException("Invalid role " + role);
				}
				rolesDetails.add(roleDetails);
			});
			userDetails.setRolesDetails(rolesDetails);
			
		}
		
		userDetails.setIsLDAPUser(userDetailsDto.getIsLdapUser());
		userDetails.setStatus(KavachUtil.convertStatusStringToLong(userDetailsDto.getStatus()));
		userDetails.setUserEmailId(userDetailsDto.getUserEmailId());
		userDetails.setUserMobileNo(userDetailsDto.getUserMobileNo());
		userDetails.setUserName(userDetailsDto.getUserName());
		userMasterRepository.save(userDetails);
		authKavachUtil.passHistoryCreator(userDetails);
	}
	

	public void resetPassword(String userName,String newPassword) {
		UserMaster userDetails=userMasterRepository.findByUserName(userName);
		if(userDetails==null) {
			throw new UserManagementException("User Not found with given UserName "+userName);
		}
		userDetails.setPassCode(authKavachUtil.passwordPolicyCheck(newPassword,userDetails));
		userDetails.setUpdaterUserId(userName);
		userDetails.setUpdatedAt(new Date());
		userMasterRepository.save(userDetails);
		authKavachUtil.passHistoryCreator(userDetails);
	}
	
	public List<EmployeeSearchDto> userDetailsToEmployeeDetailsDto(String authorization) {
		List<EmployeeSearchDto> employeeSearchDtoList = null;
		List<UserMaster> userDetails=userMasterRepository.findAll();

		if(!CollectionUtils.isEmpty(userDetails)) {
			employeeSearchDtoList = new ArrayList<EmployeeSearchDto>();
		for (Iterator iterator = userDetails.iterator(); iterator.hasNext();) {
			UserMaster userMaster = (UserMaster) iterator.next();
			if(userMaster.getIsLDAPUser().equals(1L)) {
				EmployeeSearchDto dto = new EmployeeSearchDto();
				dto.setId(userMaster.getUserName());
				dto.setName(userMaster.getFirstName()+" "+userMaster.getLastName());
				dto.setEmail(userMaster.getUserEmailId());
				dto.setMobile(userMaster.getUserMobileNo());	
				OfficeLocationDetails officeLocationDetails=userMaster.getOfficeLocationDetails();
				if(officeLocationDetails!=null) {
				dto.setOfficeLocation(officeLocationDetails.getLocationLongName());
				RegionalOfficeDetails regionalOfficeDetails=userMaster.getRegionalOfficeDetails();
				if(regionalOfficeDetails!=null) {
					dto.setRegionalOffice(regionalOfficeDetails.getRoName());					
				}
				}
				List<RoleMastDetails> roles=userMaster.getRolesDetails();
				if(roles!=null && roles.size()>0) {
					RoleMastDetails roleDetails=roles.get(0);
					dto.setRole(roleDetails.getRoleName());
					dto.setStatus(KavachUtil.convertStatusLongToString(userMaster.getStatus()));
				}else {
					dto.setStatus(STATUS_STRING_INACTIVE);					
				}
				employeeSearchDtoList.add(dto);		
			}
		}
		}
		return employeeSearchDtoList;
	}

	public void updateUserProfileInfo(String userName, UpdateUserProfileInfo userProfile, UserInfo userInfo) {
		
		UserMaster userDetails=userMasterRepository.findByUserName(userName);
		if(userDetails==null) {
			throw new UserManagementException("User Not found with given UserName "+userName);
		}
		
		List<UserMaster> userMasterByEmailId = userMasterRepository.findByUserEmailIdIgnoreCaseOrUserNameIgnoreCaseOrUserMobileNo(userProfile.getUserEmailId(),userName,userProfile.getUserMobileNo());
		
		for (UserMaster userMaster : userMasterByEmailId) {
			
			if(!userName.equalsIgnoreCase(userMaster.getUserName()) && userMaster.getUserEmailId().equalsIgnoreCase(userProfile.getUserEmailId())) {
				throw new UserManagementException("user already present with same emailId, Please user different emailId");
			}
			
			if(!userName.equalsIgnoreCase(userMaster.getUserName()) && userMaster.getUserMobileNo().equals(userProfile.getUserMobileNo())) {
				throw new UserManagementException("user already present with same mobileNumber, Please user different mobileNumber");
			}
	
		}
		
		if(!StringUtils.isEmpty(userProfile.getOfficeLocationDetails())) {
			OfficeLocationDetails officeDetail=officeLocationDetailsRepository.findByLocationLongNameAndStatus(userProfile.getOfficeLocationDetails(), STATUS_INT_ACTIVE);
			
			if(officeDetail==null) {
				throw new UserManagementException("Invalid OfficeLocation");
			}
			userDetails.setOfficeLocationDetails(officeDetail);
		}
	
		
		if(!StringUtils.isEmpty(userProfile.getRegionalOfficeDetails())) {
			RegionalOfficeDetails regionalOfficeDetails = regionalOfficeDetailsRepository.findByRoName(userProfile.getRegionalOfficeDetails());
			
			if(regionalOfficeDetails==null) {
				throw new UserManagementException("Invalid RegionalOffice");
			}
			
			userDetails.setRegionalOfficeDetails(regionalOfficeDetails);
		}
		
		userDetails.setFirstName(userProfile.getFirstName());
		userDetails.setLastName(userProfile.getLastName());
		userDetails.setUserEmailId(userProfile.getUserEmailId());
		userDetails.setUserMobileNo(userProfile.getUserMobileNo());
		
		if ( 0L == userProfile.getIsLDAPUser() && !StringUtils.isEmpty(userProfile.getPassCode())) {
			try {
				userProfile.setPassCode(rsaUtil.decrypt(userProfile.getPassCode()));
			} catch (Exception e) {
				throw new AuthManagementException("Error while decrypting passcode");
			}
			userDetails.setPassCode(authKavachUtil.passwordPolicyCheck(userProfile.getPassCode(), userDetails));
		}
		
		userDetails.setIsLDAPUser(userProfile.getIsLDAPUser());
		userDetails.setUpdaterUserId(userInfo.getUserId());
		userDetails.setUpdatedAt(new Date());
		userMasterRepository.save(userDetails);

		
	}

}
