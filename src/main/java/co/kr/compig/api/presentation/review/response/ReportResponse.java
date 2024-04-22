package co.kr.compig.api.presentation.review.response;

import co.kr.compig.global.code.ReportType;
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
public class ReportResponse extends BaseAudit {

	private ReviewDetailResponse reviewDetailResponse; // 리뷰 내용
	private Long reportId; //신고 ID
	private String contents; //리뷰 내용
	private ReportType reportType; // 분류

}
