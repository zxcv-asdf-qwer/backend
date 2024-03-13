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
	String osCode,
	@NotBlank
	String lastVer,
	@NotBlank
	String lastVerNm,
	@NotBlank
	String minVer,
	@NotBlank
	String minVerNm,
	@NotBlank
	@Pattern(regexp = "^[YN]$")
	String forceUpdate
) {

	public AppVersion toEntity() {
		return AppVersion.builder()
			.osCode(AppOsType.of(osCode))
			.lastVer(lastVer)
			.lastVerNm(lastVerNm)
			.minVer(minVer)
			.minVerNm(minVerNm)
			.forceUpdate(IsYn.of(forceUpdate))
			.build();
	}
}
