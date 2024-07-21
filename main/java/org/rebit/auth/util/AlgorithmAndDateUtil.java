package org.rebit.auth.util;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorithmAndDateUtil {

	static Logger logger=LoggerFactory.getLogger(AlgorithmAndDateUtil.class);
  

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data,String keyValue) throws Exception {
    	logger.debug("entry -  encrypt");
    	String encryptValue = AesGcmNoPadding.encrypt(data, keyValue);
        logger.debug("exit -  encrypt");
        return encryptValue;
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public static String decrypt(String encryptedData,String keyValue) throws Exception {
    	logger.debug("entry -  decrypt");
    	String decryptValue = AesGcmNoPadding.decrypt(encryptedData, keyValue);
        logger.debug("exit -  decrypt");
        return decryptValue;
    }

    
    public static String tokenExpireDate(int second) {
    	logger.debug("entry -  tokenExpireDate");
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date currentDate = new Date();
        System.out.println(dateFormat.format(currentDate));
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.SECOND, second);
        Date currentDatePlusOne = c.getTime();
        logger.debug("exit -  tokenExpireDate");
        return dateFormat.format(currentDatePlusOne);
		/*
		 * String sub=dt.substring(0,19); System.out.println(sub);
		 */

    }

}