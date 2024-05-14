package co.kr.compig.api.presentation.push.request;

import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.PhoneTypeCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUpdate {
	private PhoneTypeCode phoneType;
	private String pushKey;
	private String modelName;
	private String osVersion;
	private IsYn isAgreeReceive;
}
