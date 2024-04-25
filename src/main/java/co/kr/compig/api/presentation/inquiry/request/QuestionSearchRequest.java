package co.kr.compig.api.presentation.inquiry.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class QuestionSearchRequest extends PageableRequest {

	@Schema(description = "사용자 ID")
	private String memberId;
}
