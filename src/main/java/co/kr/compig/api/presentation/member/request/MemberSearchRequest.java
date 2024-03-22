package co.kr.compig.api.presentation.member.request;

import co.kr.compig.api.domain.code.UserType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MemberSearchRequest extends PageableRequest {
	private UserType userType;
}