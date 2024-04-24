package co.kr.compig.api.presentation.app.request;

import co.kr.compig.api.domain.app.AppVersion;
import co.kr.compig.global.code.AppOsType;
import io.swagger.v3.oas.annotations.media.Schema;
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
	@Schema(description = "요청된 디바이스 OS 유형", example = "IOS")
	private AppOsType osCode; // 요청된 디바이스 OS 유형

	@NotBlank
	@Schema(description = "최소 버전", example = "1.0.0")
	private String minVer; // 최소 버전

	@NotBlank
	@Schema(description = "업데이트 URL", example = "URL")
	private String updateUrl; // 엽데이트 URL

	@Schema(description = "내용", example = "내용")
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