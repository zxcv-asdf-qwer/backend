package co.kr.compig.api.presentation.review.request;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.review.Review;
import co.kr.compig.global.code.IsYn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

	@NotNull
	private int point; // 별점

	@NotBlank
	private String contents; // 내용

	private IsYn publish = IsYn.Y; //게시 여부

	@NotBlank
	private String memberId;

	@NotNull
	private Long careOrderId; // 공고 ID

	public Review converterEntity(Member member, CareOrder careOrder) {
		return Review.builder()
			.contents(this.contents)
			.point(this.point)
			.publish(this.publish)
			.member(member)
			.careOrder(careOrder)
			.build();
	}
}
