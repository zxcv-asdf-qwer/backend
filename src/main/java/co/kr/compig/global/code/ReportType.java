package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType implements BaseEnumCode<String> {
	SPAM("SPA", "스팸 홍보"),
	PORNOGRAPHY("POR", "음란물"),
	ILLEGAL_INFO("ILL", "불법 정보"),
	TERM_ABUSE("TER", "욕설"),
	PERSONAL_INFORMATION_EXPOSURE("PER", "개인정보 노출"),
	ETC("ETC", "기타");

	private final String code;
	private final String desc;
}
