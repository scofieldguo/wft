package com.bw30.open.cardpool.service.cmcc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ResBean {
	@XStreamAlias("MessageHeader")
	private MessageHeader messageHeader;
	
	@XStreamAlias("ResultCode")
	private Integer resultCode;
	
	@XStreamAlias("ResultDesc")
	private String resultDesc;
	
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public Integer getResultCode() {
		return resultCode;
	}

	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	
	public static class MessageHeader{
		@XStreamAlias("Version")
		private String version;
		
		@XStreamAlias("Language")
		private String language;
		
		@XStreamAlias("ClientType")
		private String clientType;
		
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		public String getClientType() {
			return clientType;
		}
		public void setClientType(String clientType) {
			this.clientType = clientType;
		}
		
	}
	
	
}
