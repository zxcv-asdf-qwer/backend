package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DomesticForeignCode implements BaseEnumCode<String> {
	D("DOMESTIC", "내국인"), // 내국인
	F("FOREIGN", "외국인"); // 외국인

	private final String code;
	private final String desc;
}
