package org.rebit.auth.administrator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SMSDto {

	@JsonProperty("dlr-url")
	private String deliveryReportUrl;
	@JsonProperty("from")
	private String senderId;
	@JsonProperty("to")
	private String recipientNumber;
	@JsonProperty("text")
	private String text;
	@JsonProperty("udh")
	private String udh;
	

	public SMSDto() {
	}
	
	public SMSDto(String deliveryReportUrl, String senderId, String recipientNumber, String text, String udh) {
		this.deliveryReportUrl = deliveryReportUrl;
		this.senderId = senderId;
		this.recipientNumber = recipientNumber;
		this.text = text;
		this.udh = udh;
	}
	
	public String getDeliveryReportUrl() {
		return deliveryReportUrl;
	}
	public void setDeliveryReportUrl(String deliveryReportUrl) {
		this.deliveryReportUrl = deliveryReportUrl;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getRecipientNumber() {
		return recipientNumber;
	}
	public void setRecipientNumber(String recipientNumber) {
		this.recipientNumber = recipientNumber;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUdh() {
		return udh;
	}
	public void setUdh(String udh) {
		this.udh = udh;
	}
	
}
