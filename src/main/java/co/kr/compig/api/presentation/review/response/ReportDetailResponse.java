package co.kr.compig.api.presentation.review.response;

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
public class ReportDetailResponse extends BaseAudit {

	private ReportResponse reportResponse;
	private Long reportId; //신고 ID
	private String reviewCreatedBy; //리뷰 작성자
	private String contents; //리뷰 내용

}
