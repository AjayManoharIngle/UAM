package org.rebit.auth.service;

import java.util.List;

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
import org.rebit.auth.connector.model.DisableUser;
import org.rebit.auth.connector.model.EmployeeSearchDto;
import org.rebit.auth.connector.model.OtpVerificationRequestDto;
import org.rebit.auth.connector.model.ResetPasswordDto;
import org.rebit.auth.connector.model.UpdateUserProfileInfo;
import org.rebit.auth.connector.model.UserProfileInfo;
import org.rebit.auth.connector.model.VerifyUserInfoDto;
import org.springframework.http.ResponseEntity;

public interface AdministratorService {
	public RolesParamDto getParamToSearchRoles();
	public List<GetRoleDetailsDto> getRoles(String authorization);
	public void updateRoles(String roleName,UpdateStatusDto updateRoleDetailsDto, String authorization);
	public void addRoles(AddRoleDetailsDto addRoleDetailsDto, String authorization);
	
	public OfficeLocationParamDto getParamToSearchOfficeLocation();
	public List<GetOfficeLocationDetailsDto> getOfficeLocation(String authorization);
	public void updateOfficeLocation(String officeLocationName,AddOfficeLocationDetailsDto updateOfficeLocationDetailsDto, String authorization);
	public void addOfficeLocation(AddOfficeLocationDetailsDto addOfficeLocationDetailsDto, String authorization);
	
	public RegionalOfficeParamDto getParamToSearchRegional();
	public List<RegionalLocationDetailsDto> getRegionalLocation();
	public void updateRegionalOffice(String officeLocationName,AddRegionalLocationDetailsDto updateOfficeLocationDetailsDto, String authorization);
	public void addRegionalOffice(AddRegionalLocationDetailsDto addOfficeLocationDetailsDto,String authorization);
	
	
	public void createUser(UserDetailsDto userDetailsDto, String authorization,boolean withRole);
	void disableUser(String userName, DisableUser disableUser,String authorization);
	public UserProfileInfo getUserProfileInfo(String userName,String authorization);
	void updateUserProfileInfo(String userName, UpdateUserProfileInfo user,String authorization);
	public void resetPassword(ResetPasswordDto resetPasswordDto,String authorization);
	
	public List<EmployeeSearchDto> searchUser(String authorization);
		public ResponseEntity<Object> verifyUserInfo(VerifyUserInfoDto verifyUserInfoDto);
	public ResponseEntity<Object> verifyOtp(OtpVerificationRequestDto otpVerificationRequestDto);
	public ResponseEntity<Object> resetPassword(OtpVerificationRequestDto otpVerificationRequestDto);
	public void unBlockUser(String userName);
	public List<GetRoleDetailsDto> getAllRoles();
	public List<UserProfileInfo> getUserProfiles(String authorization);

}
