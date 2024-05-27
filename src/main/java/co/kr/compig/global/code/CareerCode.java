package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CareerCode implements BaseEnumCode<String> {
	F("FRESH", "신입"), // 신입
	O("OLD", "경력"); // 경력

	private final String code;
	private final String desc;
}
