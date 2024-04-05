package co.kr.compig.api.presentation.pass.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PassResponse {
	String sRequestNumber;
	String sResponseNumber;
	String sAuthType;
	String sCipherTime;
	String name;
	String birth;
	String gender;
	String nationalInfo;
	String dupInfo;
	String connInfo;
	String phone;
	String mobileCompany;
	String sErrorCode;
}
