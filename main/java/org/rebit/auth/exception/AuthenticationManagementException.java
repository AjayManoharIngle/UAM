package org.rebit.auth.exception;

/**
 * 
 * @author Kapil.gautam
 * Purpose: Case Management System exception class
 * Created Date: 19-08-2022
 * Modification History:     Last Change Date:       Revised By:     Change/Defect description:
 * CR Defect No
 */

public class AuthenticationManagementException extends RuntimeException{

private static final long serialVersionUID = 1L;


public AuthenticationManagementException(String errorMsg) {
	super(errorMsg);
}


}
