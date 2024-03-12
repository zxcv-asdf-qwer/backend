package co.kr.compig.common.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AlarmTypeCode implements BaseEnumCode<String> {
	SMS("SMS", "메뉴"),
	PUSH("PUSH", "푸시");

	private final String code;
	private final String desc;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDesc() {
		return desc;
	}
}
