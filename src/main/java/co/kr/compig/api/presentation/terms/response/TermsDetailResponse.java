package co.kr.compig.api.presentation.terms.response;

import co.kr.compig.global.code.TermsType;
import co.kr.compig.global.dto.BaseAudit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class TermsDetailResponse extends BaseAudit {

	@Schema(description = "약관 ID")
	private Long termsId;
	@Schema(description = "약관 타입")
	private TermsType termsType;
	@Schema(description = "내용")
	private String contents;
}