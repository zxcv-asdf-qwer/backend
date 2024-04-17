package co.kr.compig.api.presentation.app.request;

import co.kr.compig.api.domain.app.AppVersion;
import co.kr.compig.global.code.AppOsType;
import co.kr.compig.global.code.IsYn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppVersionCreateRequest(
	@NotNull
	AppOsType osCode, // 요청된 디바이스 os이름
	@NotBlank
	String lastVer, // 앱 사용 가능한 최신 버전 정보
	@NotBlank
	String lastVerNm, // 앱 사용 가능한 최신 버전 이름
	@NotBlank
	String minVer, // 앱 사용 가능한 최소 버전 정보
	@NotBlank
	String minVerNm, // 앱 사용 가능한 최소 버전 이름
	@NotNull
	IsYn forceUpdate // 강제 업데이트 여부
) {

	public AppVersion toEntity() {
		return AppVersion.builder()
			.osCode(osCode)
			.lastVer(lastVer)
			.lastVerNm(lastVerNm)
			.minVer(minVer)
			.minVerNm(minVerNm)
			.forceUpdate(forceUpdate)
			.build();
	}
}