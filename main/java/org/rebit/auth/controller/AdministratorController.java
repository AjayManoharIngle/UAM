package org.rebit.auth.controller;



import java.util.List;

import org.rebit.auth.administrator.model.AddOfficeLocationDetailsDto;
import org.rebit.auth.administrator.model.AddRegionalLocationDetailsDto;
import org.rebit.auth.administrator.model.AddRoleDetailsDto;
import org.rebit.auth.administrator.model.EventEntityWithPage;
import org.rebit.auth.administrator.model.GetOfficeLocationDetailsDto;
import org.rebit.auth.administrator.model.GetRoleDetailsDto;
import org.rebit.auth.administrator.model.GlobalPropertiesDto;
import org.rebit.auth.administrator.model.LogFilesNameDto;
import org.rebit.auth.administrator.model.OfficeLocationParamDto;
import org.rebit.auth.administrator.model.RegionalLocationDetailsDto;
import org.rebit.auth.administrator.model.RegionalOfficeParamDto;
import org.rebit.auth.administrator.model.RolesParamDto;
import org.rebit.auth.administrator.model.RunTimeConfigrationChangeDto;
import org.rebit.auth.administrator.model.UpdateStatusDto;
import org.rebit.auth.administrator.model.UserDetailsDto;
import org.rebit.auth.connector.model.DisableUser;
import org.rebit.auth.connector.model.EmployeeSearchDto;
import org.rebit.auth.connector.model.OtpVerificationRequestDto;
import org.rebit.auth.connector.model.ResetPasswordDto;
import org.rebit.auth.connector.model.UpdateUserProfileInfo;
import org.rebit.auth.connector.model.VerifyUserInfoDto;
import org.rebit.auth.model.CheckLdapDto;
import org.rebit.auth.service.AdministratorService;
import org.rebit.auth.service.RunTimeConfigrationChangeService;
import org.rebit.auth.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 
 * @author kapil.Gautam
 * Purpose: Administrator controller class
 * Created Date: 15-06-2021
 */

@RestController
@RequestMapping("/uam-connector")
public class AdministratorController {
	
	@Autowired
	private AdministratorService administratorService;

	
	@Autowired
	private RunTimeConfigrationChangeService runTimeConfigrationChangeService;
	
	
	@GetMapping("/param-to-retrieve-roles")
	public ResponseEntity<Object> paramToSearchRoles(@RequestHeader(required = false, value = "authorization") String authorization){
		RolesParamDto rolesParamDto=administratorService.getParamToSearchRoles();
		return new ResponseEntity<>(rolesParamDto,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/retrieve-roles")
	public ResponseEntity<Object> searchRoles(@RequestHeader(required = false, value = "authorization") String authorization){
		List<GetRoleDetailsDto> searchRoleDtoList=administratorService.getRoles(authorization);
		return new ResponseEntity<>(searchRoleDtoList,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/retrieve-all-roles")
	public ResponseEntity<Object> searchAllRoles(){
		List<GetRoleDetailsDto> searchRoleDtoList=administratorService.getAllRoles();
		return new ResponseEntity<>(searchRoleDtoList,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PatchMapping("/update-roles/{roleName}")
	public ResponseEntity<Object> updateRoles(@PathVariable String roleName,@RequestBody @Valid UpdateStatusDto updateRoleDetailsDto,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.updateRoles(roleName,updateRoleDetailsDto,authorization);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/add-roles")
	public ResponseEntity<Object> addRoles(@RequestBody @Valid AddRoleDetailsDto addRoleDetailsDto,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.addRoles(addRoleDetailsDto,authorization);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	
	@GetMapping("/param-to-retrieve-office-locations")
	public ResponseEntity<Object> paramToSearchOfficeLocation(@RequestHeader(required = false, value = "authorization") String authorization){
		OfficeLocationParamDto officeLocationParamDto=administratorService.getParamToSearchOfficeLocation();
		return new ResponseEntity<>(officeLocationParamDto,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/retrieve-office-locations")
	public ResponseEntity<Object> searchOfficeLocation(@RequestHeader(required = false, value = "authorization") String authorization){
		List<GetOfficeLocationDetailsDto> searchOfficeLocationDtoList=administratorService.getOfficeLocation(authorization);
		return new ResponseEntity<>(searchOfficeLocationDtoList,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PatchMapping("/update-office-locations")
	public ResponseEntity<Object> updateOfficeLocation(@RequestParam String officeLocationName,@RequestBody @Valid AddOfficeLocationDetailsDto updateOfficeLocationDetailsDto,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.updateOfficeLocation(officeLocationName,updateOfficeLocationDetailsDto,authorization);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/add-office-locations")
	public ResponseEntity<Object> addOfficeLocation(@RequestBody @Valid AddOfficeLocationDetailsDto addOfficeLocationDetailsDto,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.addOfficeLocation(addOfficeLocationDetailsDto,authorization);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/param-to-regional-office")
	public ResponseEntity<Object> paramToSearchRegional(@RequestHeader(required = false, value = "authorization") String authorization){
		RegionalOfficeParamDto regionalOfficeParamDto=administratorService.getParamToSearchRegional();
		return new ResponseEntity<>(regionalOfficeParamDto,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/retrieve-regional-office")
	public ResponseEntity<Object> searchRegional(@RequestHeader(required = false, value = "authorization") String authorization){
		List<RegionalLocationDetailsDto> searchRegionalLocationDtoList=administratorService.getRegionalLocation();
		return new ResponseEntity<>(searchRegionalLocationDtoList,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PatchMapping("/update-regional-office")
	public ResponseEntity<Object> updateRegional(@RequestParam String officeLocationName,@RequestBody @Valid AddRegionalLocationDetailsDto updateOfficeLocationDetailsDto,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.updateRegionalOffice(officeLocationName,updateOfficeLocationDetailsDto,authorization);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping("/add-regional-office")
	public ResponseEntity<Object> addRegional(@RequestBody @Valid AddRegionalLocationDetailsDto addOfficeLocationDetailsDto,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.addRegionalOffice(addOfficeLocationDetailsDto,authorization);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	//@PostMapping("/add-user")
	public ResponseEntity<Object> addUser(@RequestBody @Valid UserDetailsDto userDetailsDto,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.createUser(userDetailsDto,authorization,true);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}

	@PostMapping("/user-registration")
	public ResponseEntity<Object> addUserNoRole(@RequestBody @Valid UserDetailsDto userDetailsDto,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.createUser(userDetailsDto,authorization,false);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PutMapping("/disable-user/{userName}")
	public ResponseEntity<Object> disableUser(@PathVariable String userName, @RequestBody DisableUser disableUser,@RequestHeader(required = false, value = "authorization") String authorization){
		administratorService.disableUser(userName,disableUser,authorization);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/unblock-user/{userName}")
	public ResponseEntity<Object> unblockUser(@PathVariable String userName){
		administratorService.unBlockUser(userName);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	
	@GetMapping("/user-profile/{userName}")
	public ResponseEntity<Object> getUserProfileDetails(@PathVariable String userName,@RequestHeader(required = false, value = "authorization") String authorization){
		return new ResponseEntity<Object>(administratorService.getUserProfileInfo(userName,authorization),ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/user-profiles")
	public ResponseEntity<Object> getUserProfiles(@RequestHeader(required = false, value = "authorization") String authorization){
		return new ResponseEntity<Object>(administratorService.getUserProfiles(authorization),ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PatchMapping("/user-profile/{userName}")
	public ResponseEntity<Object> updateUserProfileDetails(@PathVariable String userName, @RequestBody @Valid UpdateUserProfileInfo user,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.updateUserProfileInfo(userName, user,authorization);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PutMapping("/reset-password")
	public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto,Errors errors,@RequestHeader(required = false, value = "authorization") String authorization){
		ValidationUtil.validation(errors);
		administratorService.resetPassword(resetPasswordDto,authorization);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}

	@GetMapping("/retrieve-users")
	public ResponseEntity<Object> searchUser(@RequestHeader(value = "authorization") String authorization){
		List<EmployeeSearchDto> listOfUsers=administratorService.searchUser(authorization);
		return new ResponseEntity<>(listOfUsers,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping(value = "/users/reset-password/verify-info")
	public ResponseEntity<Object> verifyUserInfo(@RequestBody @Valid VerifyUserInfoDto verifyUserInfoDto,Errors errors){
		ValidationUtil.validation(errors);
		return administratorService.verifyUserInfo(verifyUserInfoDto);
	}
	
	@PostMapping(value = "/users/reset-password/verify-otp")
	public ResponseEntity<Object> verifyOtp(@RequestBody @Valid OtpVerificationRequestDto otpVerificationRequestDto,Errors errors) {
		ValidationUtil.validation(errors);
		return administratorService.verifyOtp(otpVerificationRequestDto);
	}
	
	@PatchMapping(value = "/users/reset-password")
	public ResponseEntity<Object> resetPassword(@RequestBody @Valid OtpVerificationRequestDto otpVerificationRequestDto,Errors errors) {
		ValidationUtil.validation(errors);
		return administratorService.resetPassword(otpVerificationRequestDto);
	}
	
	@GetMapping("/retrieve-config")
	public ResponseEntity<Object> getConfig(){
		RunTimeConfigrationChangeDto runTimeConfigrationChangeDto=runTimeConfigrationChangeService.getConfig();
		return new ResponseEntity<>(runTimeConfigrationChangeDto,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping(value = "/update-config")
	public ResponseEntity<Object> updateConfig(@RequestBody @Valid RunTimeConfigrationChangeDto runTimeConfigrationChangeDto,Errors errors){
		ValidationUtil.validation(errors);
		runTimeConfigrationChangeService.changeConfig(runTimeConfigrationChangeDto);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/retrieve-ldap-config")
	public ResponseEntity<Object> getLdapConfig(){
		GlobalPropertiesDto globalProperties=runTimeConfigrationChangeService.getGlobalConfig();
		return new ResponseEntity<>(globalProperties,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping(value = "/update-ldap-config")
	public ResponseEntity<Object> updateLdapConfig(@RequestBody @Valid GlobalPropertiesDto globalProperties,Errors errors){
		ValidationUtil.validation(errors);
		runTimeConfigrationChangeService.changeGLobalConfig(globalProperties);
		return new ResponseEntity<>(ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping(value = "/check-ldap-config")
	public ResponseEntity<Object> checkLdap(@RequestBody @Valid CheckLdapDto checkLdapDto,Errors errors){
		ValidationUtil.validation(errors);
		boolean check = runTimeConfigrationChangeService.checkLdap(checkLdapDto);
		return new ResponseEntity<>(check,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/retrieve-log-files-name")
	public ResponseEntity<Object> getLogFilesName(){
		LogFilesNameDto logFilesNameDto=runTimeConfigrationChangeService.getLogFileNames();
		return new ResponseEntity<>(logFilesNameDto,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/downloadFile/{fileName}")
	public ResponseEntity<Object> downloadFile(@PathVariable String fileName) {
		return runTimeConfigrationChangeService.download(fileName);
	}
	
	@GetMapping("/retrieve-event-logs/{pageNumber}")
	public ResponseEntity<Object> retrieveEventLogs(@PathVariable int pageNumber) {
		EventEntityWithPage eventEntityWithPage=  runTimeConfigrationChangeService.getEventLogs(pageNumber);
		return new ResponseEntity<>(eventEntityWithPage,ValidationUtil.getHttpHeaders(),HttpStatus.OK);
	}

}
