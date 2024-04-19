package co.kr.compig.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType implements BaseEnumCode<String> {
	GENERAL("GENERAL", "일반간병"), //일반간병
	FAMILY("FAMILY", "가족간병"); //가족간병

	private final String code;
	private final String desc;
}
