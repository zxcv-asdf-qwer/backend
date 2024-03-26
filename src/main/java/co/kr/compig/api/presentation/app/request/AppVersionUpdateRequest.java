package co.kr.compig.api.presentation.app.request;

import co.kr.compig.api.domain.code.AppOsType;
import co.kr.compig.api.domain.code.IsYn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AppVersionUpdateRequest(
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

}