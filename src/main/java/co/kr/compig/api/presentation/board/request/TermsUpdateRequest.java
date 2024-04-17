package co.kr.compig.api.presentation.board.request;

import co.kr.compig.global.code.TermsType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TermsUpdateRequest {
	private TermsType termsType;
	private String contents;
}
