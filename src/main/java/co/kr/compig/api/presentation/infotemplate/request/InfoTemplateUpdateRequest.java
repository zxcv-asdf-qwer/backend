package co.kr.compig.api.presentation.infotemplate.request;

import co.kr.compig.global.code.infoTemplateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoTemplateUpdateRequest {
	private infoTemplateType infoTemplateType; //SMS 템플릿 코드
	private String templateCode; //카카오 알림톡 템플릿 코드
	private String contents; //내용
}
