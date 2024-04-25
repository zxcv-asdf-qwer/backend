package co.kr.compig.api.presentation.terms.request;

import co.kr.compig.api.domain.terms.Terms;
import co.kr.compig.global.code.TermsType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermsCreateRequest {

	@NotNull
	@Schema(description = "약관타입", example = "SERVICE_POLICY")
	private TermsType termsType;
	@NotBlank
	@Schema(description = "약관 내용", example = "약관 내용")
	private String contents;

	public Terms converterEntity() {
		return Terms.builder()
			.termsType(this.termsType)
			.contents(this.contents)
			.build();
	}
}
