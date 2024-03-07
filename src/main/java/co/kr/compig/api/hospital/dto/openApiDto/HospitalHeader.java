package co.kr.compig.api.hospital.dto.openApiDto;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "header")
public class HospitalHeader {
    private String resultCode;
    private String resultMsg;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
