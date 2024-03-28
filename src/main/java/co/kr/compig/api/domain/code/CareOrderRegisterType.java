package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CareOrderRegisterType implements BaseEnumCode<String> {
	AUTO("AUT", "자동 매칭"),
	MANUAL("MAN", "수동 매칭");

	private final String code;
	private final String desc;
}
