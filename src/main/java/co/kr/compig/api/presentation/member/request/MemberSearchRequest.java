package co.kr.compig.api.presentation.member.request;

import java.util.List;

import co.kr.compig.global.code.UserType;
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
	private List<UserType> userType;

}