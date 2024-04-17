package co.kr.compig.api.presentation.app.response;

import co.kr.compig.global.code.AppOsType;
import co.kr.compig.global.code.IsYn;
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
	private AppOsType osCode; // 요청된 디바이스 os이름
	private String lastVer; // 앱 사용 가능한 최신 버전 정보
	private String lastVerNm; // 앱 사용 가능한 최신 버전 이름
	private String minVer; // 앱 사용 가능한 최소 버전 정보
	private String minVerNm; // 앱 사용 가능한 최소 버전 이름
	private IsYn forceUpdate; // 강제 업데이트 여부
}
