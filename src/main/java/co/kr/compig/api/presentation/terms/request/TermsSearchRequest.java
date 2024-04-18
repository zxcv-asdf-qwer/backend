package co.kr.compig.api.presentation.terms.request;

import java.time.LocalDate;

import co.kr.compig.global.code.TermsType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class TermsSearchRequest extends PageableRequest {
	private TermsType termsType; // 약관 타입
	private LocalDate createOn; // 약관 생성일
}
