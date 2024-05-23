package co.kr.compig.api.presentation.info.request;

import co.kr.compig.global.code.infoTemplateType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class InfoTemplateSearchRequest extends PageableRequest {
	private infoTemplateType infoTemplateType; //SMS 템플릿 코드
	private String templateCode; //카카오 알림톡 템플릿 코드
	private String contents; //내용
}
