package co.kr.compig.api.presentation.terms.request;

import co.kr.compig.global.code.TermsType;
import co.kr.compig.api.domain.terms.Terms;
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
	private TermsType termsType;

	@NotBlank
	private String contents;

	public Terms converterEntity() {
		return Terms.builder()
			.termsType(this.termsType)
			.contents(this.contents)
			.build();
	}
}
