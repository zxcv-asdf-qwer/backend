package co.kr.compig.api.presentation.app.request;

import co.kr.compig.api.domain.app.AppVersion;
import co.kr.compig.global.code.AppOsType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppVersionCreateRequest {

	@NotNull
	private AppOsType osCode; // 요청된 디바이스 OS 유형

	@NotBlank
	private String minVer; // 최소 버전

	@NotBlank
	private String updateUrl; // 엽데이트 ULR

	private String contents; // 내용

	public AppVersion converterEntity() {
		return AppVersion.builder()
			.osCode(this.osCode)
			.minVer(this.minVer)
			.updateUrl(this.updateUrl)
			.contents(this.contents)
			.build();
	}
}