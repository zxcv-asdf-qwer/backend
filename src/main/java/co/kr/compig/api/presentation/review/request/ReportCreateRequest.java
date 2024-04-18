package co.kr.compig.api.presentation.review.request;

import co.kr.compig.api.domain.review.Report;
import co.kr.compig.api.domain.review.Review;
import co.kr.compig.global.code.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportCreateRequest {

	private ReportType reportType;
	private String contents;

	public Report converterEntity(Review review) {
		return Report.builder()
			.reportType(this.reportType)
			.contents(this.contents)
			.review(review)
			.build();
	}
}
