package co.kr.compig.api.domain.terms;

import co.kr.compig.api.presentation.board.request.TermsUpdateRequest;
import co.kr.compig.api.presentation.terms.response.TermsDetailResponse;
import co.kr.compig.api.presentation.terms.response.TermsListResponse;
import co.kr.compig.api.presentation.terms.response.TermsResponse;
import co.kr.compig.global.code.TermsType;
import co.kr.compig.global.code.UseYn;
import co.kr.compig.global.code.converter.TermsTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@SequenceGenerator(
	name = "terms_seq_gen",
	sequenceName = "terms_seq",
	initialValue = 1,
	allocationSize = 1
)
public class Terms {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terms_seq_gen")
	@Column(name = "terms_id")
	private Long id;

	@Column(length = 10)
	@Convert(converter = TermsTypeConverter.class)
	private TermsType termsType;

	@Column
	private String contents;

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private UseYn useYn = UseYn.Y; // 사용 여부

	/* =================================================================
   * Default columns
   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	public TermsDetailResponse toTermsDetailResponse() {
		TermsDetailResponse termsDetailResponse = TermsDetailResponse.builder()
			.termsId(this.id)
			.termsType(this.termsType)
			.contents(this.contents)
			.build();

		termsDetailResponse.setCreatedAndUpdated(this.createdAndModified);
		return termsDetailResponse;
	}

	public void update(TermsUpdateRequest termsUpdateRequest) {
		this.termsType = termsUpdateRequest.getTermsType();
		this.contents = termsUpdateRequest.getContents();
	}

	public TermsResponse toResponse() {
		TermsResponse termsResponse = TermsResponse.builder()
			.termsId(this.id)
			.termsType(this.termsType)
			.createdOn(this.createdAndModified.getCreatedOn().toLocalDate())
			.createdBy(this.createdAndModified.getCreatedBy().getUserNm())
			.build();

		return termsResponse;
	}

	public TermsListResponse toTermListResponse() {
		TermsListResponse termsListResponse = TermsListResponse.builder()
			.termsId(this.id)
			.createdOn(this.createdAndModified.getCreatedOn().toLocalDate())
			.contents(this.contents)
			.build();

		return termsListResponse;
	}

	public void delete() {
		this.useYn = UseYn.N;
	}
}
