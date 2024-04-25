package co.kr.compig.api.presentation.terms.request;

import co.kr.compig.global.code.TermsType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class TermsSearchRequest extends PageableRequest {

	@Schema(description = "약관 타입", example = "SERVICE_POLICY")
	private TermsType termsType; // 약관 타입
}
