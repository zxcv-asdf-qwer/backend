package co.kr.compig.api.presentation.terms.response;

import co.kr.compig.global.code.TermsType;
import co.kr.compig.global.dto.BaseAudit;
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
	private Long termsId;
	private TermsType termsType;
	private String contents;
}