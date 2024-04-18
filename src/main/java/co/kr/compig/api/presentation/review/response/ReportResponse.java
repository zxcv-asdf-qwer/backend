package co.kr.compig.api.presentation.review.response;

import java.time.LocalDateTime;

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

	private Long reportId; // 신고 ID
	private String reviewCreatedBy; // 리뷰 작성자
	private String reportCreatedBy; // 신고자
	private ReportType reportType; // 신고 사유
	private LocalDateTime createdOn; // 신고일
}
