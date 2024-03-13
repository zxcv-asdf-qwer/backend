package co.kr.compig.api.presentation.hospital.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "OpenAPI_ServiceResponse")
public class HospitalExceptionDto {
	private CmmMsgHeader cmmMsgHeader;

	public CmmMsgHeader getCmmMsgHeader() {
		return cmmMsgHeader;
	}

	@XmlElement(name = "cmmMsgHeader")
	public void setCmmMsgHeader(CmmMsgHeader cmmMsgHeader) {
		this.cmmMsgHeader = cmmMsgHeader;
	}
}
