package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyStatus implements BaseEnumCode<String> {
	MATCHING_WAIT("WAI", "매칭대기"), // 매칭 대기
	MATCHING_COMPLETE("COM", "매칭완료"); // 매칭 완료

	private final String code;
	private final String desc;
}
