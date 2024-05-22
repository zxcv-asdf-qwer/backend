package co.kr.compig.api.presentation.push.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class PushSearchRequest extends PageableRequest {
	private String contents; //내용
	private String receiverMemberName;
}
