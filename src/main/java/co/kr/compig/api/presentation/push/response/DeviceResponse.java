package co.kr.compig.api.presentation.push.response;

import java.time.LocalDate;

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
public class DeviceResponse {

	private Long deviceId;
	private String userId;
	private PhoneTypeCode phoneType;
	private String deviceUuid;
	private String pushKey;
	private String modelName;
	private String osVersion;
	private LocalDate agreeReceiveDate;
	private IsYn isAgreeReceive;

}
