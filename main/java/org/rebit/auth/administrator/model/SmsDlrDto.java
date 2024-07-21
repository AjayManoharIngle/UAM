package org.rebit.auth.administrator.model;
public class SmsDlrDto {
	
	private String dlrUrl;
	private String dlrMask;
	private boolean dlrEnabled;
	public String getDlrUrl() {
		return dlrUrl;
	}
	public void setDlrUrl(String dlrUrl) {
		this.dlrUrl = dlrUrl;
	}
	public String getDlrMask() {
		return dlrMask;
	}
	public void setDlrMask(String dlrMask) {
		this.dlrMask = dlrMask;
	}
	public boolean isDlrEnabled() {
		return dlrEnabled;
	}
	public void setDlrEnabled(boolean dlrEnabled) {
		this.dlrEnabled = dlrEnabled;
	}
	public SmsDlrDto(String dlrUrl, String dlrMask, boolean dlrEnabled) {
		super();
		this.dlrUrl = dlrUrl;
		this.dlrMask = dlrMask;
		this.dlrEnabled = dlrEnabled;
	}
}
