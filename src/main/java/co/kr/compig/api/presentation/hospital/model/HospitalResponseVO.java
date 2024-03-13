package co.kr.compig.api.presentation.hospital.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class HospitalResponseVO {
	private HospitalHeader header;
	private HospitalsDto body;

	public HospitalHeader getHeader() {
		return header;
	}

	public void setHeader(HospitalHeader header) {
		this.header = header;
	}

	public HospitalsDto getBody() {
		return body;
	}

	public void setBody(HospitalsDto body) {
		this.body = body;
	}
}
