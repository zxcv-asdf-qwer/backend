package co.kr.compig.api.presentation.app.response;

import co.kr.compig.api.domain.code.AppOsType;
import co.kr.compig.api.domain.code.IsYn;
import lombok.Builder;

@Builder
public record AppVersionResponse(
	AppOsType osCode,
	String lastVer,
	String lastVerNm,
	String minVer,
	String minVerNm,
	IsYn forceUpdate
) {
}
