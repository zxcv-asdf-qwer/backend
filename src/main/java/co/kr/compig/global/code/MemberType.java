package co.kr.compig.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberType implements BaseEnumCode<String> {
	NO_MEMBER("NO_MEMBER", "비회원"), // 비회원
	MEMBER("MEMBER", "회원"); // 회원

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
