package co.kr.compig.common.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SystemServiceType implements BaseEnumCode<String> {
	SMS("SMS", "SMS");

	private final String code;
	private final String desc;

	@Override
	public String getCode() {
		return null;
	}

	@Override
	public String getDesc() {
		return null;
	}
}
