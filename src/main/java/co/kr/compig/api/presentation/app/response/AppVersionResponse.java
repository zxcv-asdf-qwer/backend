package co.kr.compig.api.presentation.app.response;

import co.kr.compig.global.code.AppOsType;
import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class AppVersionResponse extends BaseAudit {
	private Long appVersionId;
	private AppOsType osCode; // 요청된 디바이스 os 유형
	private String minVer; // 앱 사용 가능한 최소 버전
	private String updateUrl; // 업데이트 URL
}
