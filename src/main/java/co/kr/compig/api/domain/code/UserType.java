package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserType implements BaseEnumCode<String> {
	SYS_ADMIN("SYS_ADMIN", "슈퍼관리자", null),
	SYS_USER("SYS_USER", "내부사용자", null),
	NO_USER("NO_USER", "비회원", null),
	GUARDIAN("GUARDIAN", "보호자/환자", null),
	PARTNER("PARTNER", "케어매니저", null),
	PARTNER_MASTER("PARTNER", "케어매니저", PARTNER),
	PARTNER_OPEN("PARTNER", "케어매니저", PARTNER),
	PARTNER_HOSPITAL_HANAM_S("PARTNER", "케어매니저", PARTNER),
	BLACK("BLACK", "블랙리스트", null);

	private final String code;
	private final String desc;
	private final UserType parents;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDesc() {
		return desc;
	}
}
