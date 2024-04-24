package co.kr.compig.api.presentation.app.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppVersionUpdateRequest {

	private String minVer; // 강제 업데이트 버전
	private String updateUrl; // 업데이트 url
	private String contents; // 내용
}
