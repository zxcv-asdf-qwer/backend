package co.kr.compig.api.presentation.push.request;

import static co.kr.compig.global.utils.SecurityUtil.*;

import java.time.LocalDate;

import org.apache.commons.lang3.ObjectUtils;

import co.kr.compig.api.domain.push.Device;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.PhoneTypeCode;
import co.kr.compig.global.code.UseYn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCreate {
	@NotNull
	private PhoneTypeCode phoneType;
	@NotBlank
	private String deviceUuid;
	private String pushKey;
	private String modelName;
	private String osVersion;
	@Builder.Default
	private UseYn useYn = UseYn.Y; // 사용유무
	private IsYn isAgreeReceive;

	public Device converterEntity() {
		return Device.builder()
			.phoneType(phoneType)
			.deviceUuid(deviceUuid)
			.pushKey(pushKey)
			.modelName(modelName)
			.osVersion(osVersion)
			.isAgreeReceive(isAgreeReceive)
			.agreeReceiveDate(ObjectUtils.isNotEmpty(isAgreeReceive) ? LocalDate.now() : null)
			.member(getCurrentMember())
			.build();
	}
}
