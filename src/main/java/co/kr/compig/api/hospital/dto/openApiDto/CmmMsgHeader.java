package co.kr.compig.api.hospital.dto.openApiDto;

import jakarta.xml.bind.annotation.XmlElement;

public class CmmMsgHeader {
	private String errMsg;
	private String returnAuthMsg;
	private String returnReasonCode;

	public String getErrMsg() {
		return errMsg;
	}

	@XmlElement(name = "errMsg")
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getReturnAuthMsg() {
		return returnAuthMsg;
	}

	@XmlElement(name = "returnAuthMsg")
	public void setReturnAuthMsg(String returnAuthMsg) {
		this.returnAuthMsg = returnAuthMsg;
	}

	public String getReturnReasonCode() {
		return returnReasonCode;
	}

	@XmlElement(name = "returnReasonCode")
	public void setReturnReasonCode(String returnReasonCode) {
		this.returnReasonCode = returnReasonCode;
	}
}
