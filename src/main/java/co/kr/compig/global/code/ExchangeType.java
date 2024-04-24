package co.kr.compig.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExchangeType implements BaseEnumCode<String> {

	HAND("HAND", "수기"),
	AUTO("AUTO", "자동");

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
