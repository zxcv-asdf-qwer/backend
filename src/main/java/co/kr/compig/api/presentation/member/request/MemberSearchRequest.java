package co.kr.compig.api.presentation.member.request;

import co.kr.compig.global.code.UserType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class MemberSearchRequest extends PageableRequest {
	private String userNm;
	private String telNo;

}