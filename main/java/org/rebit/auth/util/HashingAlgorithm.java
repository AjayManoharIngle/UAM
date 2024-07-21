package org.rebit.auth.util;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.exception.UserManagementException;
  
public class HashingAlgorithm {
	
	final static Logger logger = LogManager.getLogger();
	
    public static String encryptThisString(String input)
    {
    	logger.trace("Entry into encryptThisString");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        	logger.trace("Exit from encryptThisString");
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
        	logger.error("Error while encrypting",e);
            throw new UserManagementException("Error while encrypting");
        }
    }
}