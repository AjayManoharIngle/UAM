package org.rebit.auth.exception;

/**
 * 
 * @author Anuradha.Phalke
 * Purpose: Case Management System exception class
 * Created Date: 26-03-2021
 * Modification History:     Last Change Date:       Revised By:     Change/Defect description:
 * CR Defect No
 */

public class UserManagementException extends RuntimeException{

private static final long serialVersionUID = 1L;


public UserManagementException(String errorMsg) {
	super(errorMsg);
}


}
