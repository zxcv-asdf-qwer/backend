package co.kr.compig.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardType implements BaseEnumCode<String> {
	NOTICE("NOT", "공지사항"), // 공지사항
	FAQ("FAQ", "FAQ"), // FAQ
	NEWS("NEW", "소식"), // 소식
	INFO("INF", "간병정보"), // 간병정보
	EDUCATION("EDU", "교육"), // 교육
	EVENT("EVE", "이벤트"); // 이벤트

	private final String code;
	private final String desc;
}
