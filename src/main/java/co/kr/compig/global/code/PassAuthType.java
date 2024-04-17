package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassAuthType implements BaseEnumCode<String> {
	M("MOBILE", "root"),
	X("MATCHING_MANAGEMENT", "매칭 관리"),
	S("MANUAL_REGISTRATION", "수기 관리"),
	U("MESSAGE_MANAGEMENT", "메시지 관리");

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
