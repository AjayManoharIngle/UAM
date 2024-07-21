package org.rebit.auth.repository;

import java.util.List;

import org.rebit.auth.entity.RoleMastDetails;
import org.rebit.auth.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserMasterRepository extends JpaRepository<UserMaster, Long>{

	public UserMaster findByUserNameAndStatus(String userName,Long status);
	public UserMaster findByUserEmailIdAndStatus(String emailId,Long status);
	public UserMaster findByUserName(String userName);
	public UserMaster findByUserNameIgnoreCaseNotAndUserEmailIdIgnoreCase(String userName,String emailId);
	public List<UserMaster> findByUserEmailIdIgnoreCaseOrUserNameIgnoreCaseOrUserMobileNo(String emailId,String userName,String userMobileNo);
	public List<UserMaster> findByUserNameIgnoreCaseNotAndUserEmailIdIgnoreCaseOrUserMobileNo(String userName,String emailId,String userMobileNo);
	public List<UserMaster> findByRolesDetailsInAndStatus(List<RoleMastDetails> rolesDetails,Long status);
	public List<UserMaster> findByIsLDAPUser(Long isLDAPUser);
	
	@Query(nativeQuery = true,value="select u.* \r\n"
			+ "from tbl_user_m u \r\n"
			+ "inner join tbl_user_role_mapping ur on u.n_userid=ur.n_userid\r\n"
			+ "inner join tbl_role_m r on ur.n_roleid=r.n_roleid\r\n"
			+ "and s_rolename =:userRole\r\n"
			+ "and u.n_status=1")
	public List<UserMaster> findUsersByRoleName(String userRole);
	
	@Query(nativeQuery = true,value="select u.* \r\n"
			+ "from tbl_user_m u \r\n"
			+ "left join tbl_user_role_mapping ur on u.n_userid=ur.n_userid\r\n"
			+ "left join tbl_role_m r on ur.n_roleid=r.n_roleid\r\n"
			+ "where ((s_rolename not in(:userRoles)) or (s_rolename is null))\r\n"
			+ "and u.n_status=1")
	public List<UserMaster> findUsersByRoleNameNotIn(List<String> userRoles);
	
	public List<UserMaster> findByStatus(Long status);

}

