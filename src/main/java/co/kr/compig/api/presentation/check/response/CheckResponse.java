package co.kr.compig.api.presentation.check.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckResponse {
	String jumin1;
	String jumin2;
	String name;
	String iRtn;
	String Rtn;
	String sRtnMsg;
}
