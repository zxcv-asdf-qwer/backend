package co.kr.compig.api.presentation.board.request;

import co.kr.compig.global.code.TermsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TermsUpdateRequest {
	@Schema(description = "약관 타입", example = "PERSONAL_INFORMATION_POLICY")
	private TermsType termsType;
	@Schema(description = "약관 내용", example = "약관 내용")
	private String contents;
}
