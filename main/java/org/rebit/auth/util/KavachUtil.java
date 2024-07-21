package org.rebit.auth.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class KavachUtil {

	public static String convertStatusIntToString(int status) {
		String statusName = null;
		if(status==1) {
			statusName = "ACTIVE";
		}else if (status == 0) {
			statusName = "INACTIVE";
		}
		return statusName;
	}
	
	public static String convertStatusLongToString(Long status) {
		String statusName = null;
		if(status==1) {
			statusName = "ACTIVE";
		}else if (status == 0) {
			statusName = "INACTIVE";
		}
		return statusName;
	}
	
	public static int convertStatusStringToInt(String statusName) {
		int status = 0;
		if(statusName.equals("ACTIVE")) {
			status = 1;
		}else if (statusName.equals("INACTIVE")) {
			status = 0;
		}
		return status;
	}
	
	public static long convertStatusStringToLong(String statusName) {
		long status = 0;
		if(statusName.equals("ACTIVE")) {
			status = 1L;
		}else if (statusName.equals("INACTIVE")) {
			status = 0;
		}
		return status;
	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMYYYYhhmmss");
		return sdf.format(new Date());
	}
	
	public static boolean isLongPresentInArrayOfLong(Long roleName,Long[] compareWithRoles) {
		boolean isPresent = false;
		for (int i = 0; i < compareWithRoles.length; i++) {
			if(roleName.equals(compareWithRoles[i])) {
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}
	
	 public static Long generateIdentificationNumber()
		{
			Random rand = new SecureRandom();
			int rand_int1 = rand.nextInt(10000000);
			 return (System.currentTimeMillis() + rand_int1);
		} 
}
