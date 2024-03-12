package co.kr.compig.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsTemplateType implements BaseEnumCode<String> {
	PHONE_VERIFICATION_TEMPLATE("PVT", "휴대폰 번호인증", "code38");

	private final String code;
	private final String desc;
	private final String templateCode;
}
