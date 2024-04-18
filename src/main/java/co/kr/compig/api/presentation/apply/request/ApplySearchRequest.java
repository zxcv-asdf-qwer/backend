package co.kr.compig.api.presentation.apply.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class ApplySearchRequest extends PageableRequest {
	private String memberId;
}
