package co.kr.compig.api.presentation.app.request;

import co.kr.compig.api.domain.app.AppVersion;
import co.kr.compig.api.domain.code.AppOsType;
import co.kr.compig.api.domain.code.IsYn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AppVersionRequest(
	@NotNull
	@Pattern(regexp = "^(IOS|AOS)$")
	AppOsType osCode,
	@NotNull
	Integer lastVer,
	@NotBlank
	String lastVerNm,
	@NotNull
	Integer minVer,
	@NotBlank
	String minVerNm,
	@NotNull
	@Pattern(regexp = "^[YN]$")
	IsYn forceUpdate
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
