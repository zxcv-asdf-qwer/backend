package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberType implements BaseEnumCode<String> {
	NO_MEMBER("NOM", "비회원"), // 비회원
	MEMBER("MEM", "회원"); // 회원

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
