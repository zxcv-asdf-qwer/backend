package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserType implements BaseEnumCode<String> {
	SYS_ADMIN("SYS_ADMIN", "슈퍼관리자", null),
	SYS_USER("SYS_USER", "내부사용자", null),
	OPERATION("OPERATION", "운영관리", SYS_USER),
	PRODUCT("OPERATION", "상품관리", SYS_USER),
	DESIGN("OPERATION", "디자인관리", SYS_USER),
	PROMOTION("OPERATION", "프로모션관리", SYS_USER),
	ORDER("OPERATION", "주문관리", SYS_USER),
	SETTLE("OPERATION", "정산관리", SYS_USER),
	MEMBER("OPERATION", "회원관리", SYS_USER),
	COMMUNITY("OPERATION", "커뮤니티관리", SYS_USER),
	STATISTICS("OPERATION", "통계자료관리", SYS_USER),
	USER("USER", "일반사용자", null),
	GUARDIAN("GUARDIAN", "보호자/환자", USER),
	PARTNER("PARTNER", "케어매니저", USER),
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
