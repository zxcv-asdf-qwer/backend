package co.kr.compig.api.presentation.review.response;

import java.time.LocalDateTime;

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
public class ReviewDetailResponse extends BaseAudit {
	private Long id; // 공고 ID
	private String createdBy; // 작성자
	private LocalDateTime createOn; // 작성일
	private String contents; // 내용
	private int point; // 별점
}
