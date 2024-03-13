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

	public static AppOsType of(final String appOsType) {
		if (appOsType.equals("AOS")) {
			return AppOsType.AOS;
		}

		return AppOsType.IOS;
	}
}
