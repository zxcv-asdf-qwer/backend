package co.kr.compig.api.presentation.member.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class MemberSearchRequest extends PageableRequest {
	private String userNm;
	private String telNo;

}