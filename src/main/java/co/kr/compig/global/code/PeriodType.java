package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PeriodType implements BaseEnumCode<String> {
	PART_TIME("PAR", "시간제"), // 시간제
	PERIOD("PER", "기간제"); // 기간제

	private final String code;
	private final String desc;

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}
}
