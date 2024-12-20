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
public class ReviewDetailResponse extends BaseAudit {

	private Long reviewId; // 리뷰 id
	private Long orderId; // 공고 ID
	private String contents; // 내용
	private int point; // 별점
}
