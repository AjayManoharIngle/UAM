package org.rebit.auth.administrator.model;
public class SmsDao {
	private String username;
	private String password;
	private String deliveryReportUrl;
	private String deliveryReportMask;
	private String senderId;
	private String recipientNumber;
	private String text;
	private String smsVendorUrl;
	private String udh;
	private String token;
	private String category;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDeliveryReportUrl() {
		return deliveryReportUrl;
	}
	public void setDeliveryReportUrl(String deliveryReportUrl) {
		this.deliveryReportUrl = deliveryReportUrl;
	}
	public String getRecipientNumber() {
		return recipientNumber;
	}
	public void setRecipientNumber(String recipientNumber) {
		this.recipientNumber = recipientNumber;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSmsVendorUrl() {
		return smsVendorUrl;
	}
	public void setSmsVendorUrl(String smsVendorUrl) {
		this.smsVendorUrl = smsVendorUrl;
	}
	public String getUdh() {
		return udh;
	}
	public void setUdh(String udh) {
		this.udh = udh;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDeliveryReportMask() {
		return deliveryReportMask;
	}
	public void setDeliveryReportMask(String deliveryReportMask) {
		this.deliveryReportMask = deliveryReportMask;
	}	
}
