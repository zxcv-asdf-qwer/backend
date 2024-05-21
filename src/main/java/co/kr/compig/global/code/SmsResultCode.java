package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsResultCode implements BaseEnumCode<String> {
	PHONE_VERIFICATION_TEMPLATE("PVT", "휴대폰 번호인증"),
	;

	private final String code;
	private final String desc;
}
