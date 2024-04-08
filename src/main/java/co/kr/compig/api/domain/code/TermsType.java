package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TermsType implements BaseEnumCode<String> {
	SERVICE("SER", "서비스 이용약관"),
	;

	private final String code;
	private final String desc;
}
