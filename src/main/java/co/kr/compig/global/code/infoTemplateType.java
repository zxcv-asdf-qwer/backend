package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum infoTemplateType implements BaseEnumCode<String> {
	PHONE_VERIFICATION_TEMPLATE("PVT", "휴대폰 번호인증"),
	NEW_CREATE_ORDER("NCO", "공고 등록 알림");

	private final String code;
	private final String desc;
}
