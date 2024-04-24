package co.kr.compig.api.presentation.app.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppVersionUpdateRequest {

	@Schema(description = "강제 업데이트 버전", example = "1.0.1")
	private String minVer; // 강제 업데이트 버전
	@Schema(description = "업데이트 URL", example = "URL")
	private String updateUrl; // 업데이트 url
	@Schema(description = "내용", example = "내용")
	private String contents; // 내용
}
