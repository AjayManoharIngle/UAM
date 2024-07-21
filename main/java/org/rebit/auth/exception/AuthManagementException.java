package org.rebit.auth.exception;

/**
 * 
 * @author Anuradha.Phalke
 * Purpose: Case Management System exception class
 * Created Date: 26-03-2021
 * Modification History:     Last Change Date:       Revised By:     Change/Defect description:
 * CR Defect No
 */

public class AuthManagementException extends RuntimeException{

private static final long serialVersionUID = 1L;


public AuthManagementException(String errorMsg) {
	super(errorMsg);
}


}