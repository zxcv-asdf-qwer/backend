package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppOsType implements BaseEnumCode<String> {
	AOS("AOS", "app.type.aos"),
	IOS("IOS", "app.type.ios");

	private final String code;
	private final String desc;

}
