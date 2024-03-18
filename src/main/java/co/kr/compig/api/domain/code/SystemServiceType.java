package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemServiceType implements BaseEnumCode<String> {
	SMS("SMS", "SMS");

	private final String code;
	private final String desc;

}
