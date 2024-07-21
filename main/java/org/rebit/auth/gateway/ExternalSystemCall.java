package org.rebit.auth.gateway;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.administrator.model.GenerateTokenResponse;
import org.rebit.auth.administrator.model.SMSDto;
import org.rebit.auth.administrator.model.SmsDao;
import org.rebit.auth.config.GlobalProperties;
import org.rebit.auth.exception.UserManagementException;
import org.rebit.auth.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kapil.Gautam
 * @DateAndTime NA
 * @ReasonForChange NA
 */

@Component
@Scope("prototype")
public class ExternalSystemCall {
	final static Logger logger = LogManager.getLogger();
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private GlobalProperties globalProperties;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	/**
	* @author Kapil.Gautam
	* Generate token used for sending SMS
	* @param smsVendorTokenUrl SMS Vendor url for token operation
	* @param username for Basic Authorization
	* @param password for Basic Authorization
	* @return GenerateTokenResponse with status true response if token was generated successfully else status false response.
	*/
	public GenerateTokenResponse generateSmsToken(String smsVendorTokenUrl, String username, String password) {
		RestTemplate restTemplateWithProxy= null;
		if(jwtConfig.getSmsOtpOnDevEnv().equalsIgnoreCase("true")) {
			restTemplateWithProxy=restTemplate;
		}else {
			restTemplateWithProxy=getRestTemplateWithProxy();
		}
		logger.debug("entry generateSmsToken");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //headers.add("Access-Control-Expose-Headers", "Authorization");
		headers.setBasicAuth(username, password);
		HttpEntity<Object> request = new HttpEntity<>(headers);
		GenerateTokenResponse response = new GenerateTokenResponse();
		try {
			String url = smsVendorTokenUrl +"?action=generate";
			logger.debug("url :"+url);
			response = restTemplateWithProxy.postForObject(url, request, GenerateTokenResponse.class);
			response.setStatus(true);
			logger.debug("exit generateSmsToken");
			return response;
        } catch(HttpStatusCodeException e){
			logger.error(e.getMessage());
			logger.error(e.getResponseBodyAsString());
			logger.debug("exit generateSmsToken");
			response.setStatus(false);
            return response;
        } catch(Exception e){
			logger.error(e.getMessage());
			logger.debug("exit generateSmsToken");
			response.setStatus(false);
            return response;
        }
	}
	
	public ResponseEntity<Object> sendSms(SmsDao smsDao, boolean isGet, boolean isPostBody) {
		
		RestTemplate restTemplateWithProxy= null;
		if(jwtConfig.getSmsOtpOnDevEnv().equalsIgnoreCase("true")) {
			restTemplateWithProxy=restTemplate;
		}else {
			restTemplateWithProxy=getRestTemplateWithProxy();
		}
		
		logger.debug("entry sendSms");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		if(smsDao.getToken() != null) {
	        //headers.add("Access-Control-Expose-Headers", "Authorization");
			//headers.add("Authorization", "Bearer "+smsDao.getToken());
			headers.setBearerAuth(smsDao.getToken());
		} else {
			logger.debug("Token not set");
		}
		String response;
		try {
			String url = getSmsUrl(smsDao);
			logger.debug("sms url :"+url);
			if(!isGet) {
				if(isPostBody) {
					SMSDto smsDto = new SMSDto(smsDao.getDeliveryReportUrl(), smsDao.getSenderId(), smsDao.getRecipientNumber(), smsDao.getText(), smsDao.getUdh());
					HttpEntity<Object> request = new HttpEntity<>(smsDto, headers);
					response = restTemplateWithProxy.postForObject(smsDao.getSmsVendorUrl(), request, String.class);
				}else {
					HttpEntity<Object> request = new HttpEntity<>(headers);
					response = restTemplateWithProxy.postForObject(url, request, String.class);
				}
				
			}else {
				//response = outRest.getForObject(url, String.class);
				HttpEntity<Object> entity = new HttpEntity<>(headers);
				response = restTemplateWithProxy.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			}
			logger.debug("exit sendSms");
			return new ResponseEntity<Object>(response,HttpStatus.OK);
        } catch(HttpStatusCodeException e){
			logger.error(e.getMessage());
			logger.debug("exit sendSms");
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        } catch(Exception e){
			logger.error(e.getMessage());
			logger.debug("exit sendSms");
            throw new UserManagementException("sendSms Exception");
        }
	}
	
	private String getSmsUrl(SmsDao smsDao) {

		logger.debug("entry getSmsUrl");
		String deliveryReportUrl = smsDao.getDeliveryReportUrl();
		String deliveryReportMask = smsDao.getDeliveryReportMask();
		String udh = smsDao.getUdh();
		String category = smsDao.getCategory();
		String url = smsDao.getSmsVendorUrl() +"?to=" + smsDao.getRecipientNumber() + "&from=" + smsDao.getSenderId() + "&text=" + smsDao.getText();
		
		if(deliveryReportUrl != null && !deliveryReportUrl.trim().isEmpty()) {
			url = url + "&dlr-url="	+ deliveryReportUrl;
		} 
		if(deliveryReportMask != null && !deliveryReportMask.trim().isEmpty()) {
			url = url + "&dlr-mask=" + deliveryReportMask;
		}
		if(udh != null && !udh.trim().isEmpty()) {
			url = url + "&udh=" + udh;
		}
		if(category != null && !category.trim().isEmpty()) {
			url = url + "&category=" + category;
		}
		logger.debug("exit getSmsUrl");
		return url;
	}
	
	public RestTemplate getRestTemplateWithProxy() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
	    Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(jwtConfig.getProxyIp(), jwtConfig.getProxyPort()));
	    requestFactory.setProxy(proxy);
	    return new RestTemplate(requestFactory);
	}
}
