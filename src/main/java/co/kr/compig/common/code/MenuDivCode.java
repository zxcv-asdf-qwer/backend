package co.kr.compig.common.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MenuDivCode implements BaseEnumCode<String> {

	OPERATION("OPERATION", "OPERATION"),
	CARE("CARE", "CARE"),
	MEMBER("MEMBER", "MEMBER"),
	BOARD("BOARD", "BOARD"),
	SETTLE("SETTLE", "SETTLE");

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
