package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CareOrderProcessType implements BaseEnumCode<String> {
	AUTO("AUT", "자동 매칭"), //간병인이 지원해서 보호자가 직접 간병인을 선택해서 매칭
	MANUAL("MAN", "수동 매칭"); //관리자가 수기로 간병인을 선택해서 매칭

	private final String code;
	private final String desc;
}
