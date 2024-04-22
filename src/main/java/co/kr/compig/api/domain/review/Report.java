package co.kr.compig.api.domain.review;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.kr.compig.api.presentation.review.response.ReportDetailResponse;
import co.kr.compig.api.presentation.review.response.ReportResponse;
import co.kr.compig.global.code.ReportType;
import co.kr.compig.global.code.converter.ReportTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	name = "report_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "report_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_seq_gen")
	@Column(name = "report_id")
	private Long id;

	@Column
	@Convert(converter = ReportTypeConverter.class)
	private ReportType reportType;

	private String contents; //내용
	/* =================================================================
	 * Relation method
	   ================================================================= */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_report"))
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Review review; // Member id

	/* =================================================================
	 * Default columns
	   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	public ReportDetailResponse toReportDetailResponse(ReportResponse reportResponse) {
		ReportDetailResponse reportDetailResponse = ReportDetailResponse.builder()
			.reportResponse(reportResponse)
			.reportId(this.id)
			.reviewCreatedBy(this.review.getCreatedAndModified().getCreatedBy().getUserNm())
			.contents(this.contents)
			.build();

		reportDetailResponse.setCreatedAndUpdated(this.createdAndModified);
		return reportDetailResponse;
	}

	public ReportResponse toResponse() {
		ReportResponse reportResponse = ReportResponse.builder()
			.reportId(this.id)
			.reviewCreatedBy(this.review.getCreatedAndModified().getCreatedBy().getUserNm())
			.reportType(this.reportType)
			.build();

		reportResponse.setCreatedAndUpdated(this.createdAndModified);
		return reportResponse;
	}
	/* =================================================================
 	 * Business
       ================================================================= */
}
