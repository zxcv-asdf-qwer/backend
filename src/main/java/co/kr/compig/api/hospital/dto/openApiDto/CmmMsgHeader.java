package co.kr.compig.api.hospital.dto.openApiDto;

import jakarta.xml.bind.annotation.XmlElement;

public class CmmMsgHeader {
  private String errMsg;
  private String returnAuthMsg;
  private int returnReasonCode;

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

  public int getReturnReasonCode() {
    return returnReasonCode;
  }

  @XmlElement(name = "returnReasonCode")
  public void setReturnReasonCode(int returnReasonCode) {
    this.returnReasonCode = returnReasonCode;
  }
}
