package co.kr.compig.api.domain.review;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.review.request.ReviewUpdateRequest;
import co.kr.compig.api.presentation.review.response.ReviewDetailResponse;
import co.kr.compig.api.presentation.review.response.ReviewResponse;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	name = "review_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "review_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq_gen")
	@Column(name = "review_id")
	private Long id;

	@Column
	private String contents; //내용

	@Column
	private int point; //점수

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private IsYn publish; //게시 여부

	/* =================================================================
 	 * Domain mapping
      ================================================================= */
	@ManyToOne
	@JoinColumn(name = "care_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_review"))
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private CareOrder careOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk02_review"))
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Member member; // Member id

	@Builder.Default
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Report> reports = new HashSet<>();
	/* =================================================================
	 * Relation method
	   ================================================================= */

	public void setCareOrder(CareOrder careOrder) {
		this.careOrder = careOrder;
	}

	/* =================================================================
	 * Default columns
	   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	public ReviewDetailResponse toReviewDetailResponse() {
		ReviewDetailResponse response = ReviewDetailResponse.builder()
			.reviewId(this.id)
			.orderId(this.careOrder.getId())
			.contents(this.contents)
			.point(this.point)
			.build();

		response.setCreatedAndUpdated(this.createdAndModified);

		return response;
	}

	public void update(ReviewUpdateRequest reviewUpdateRequest) {
		this.contents = reviewUpdateRequest.getContents();
		this.point = reviewUpdateRequest.getPoint();
	}

	public ReviewResponse toReview() {
		ReviewResponse response = ReviewResponse.builder()
			.reviewId(this.id)
			.orderId(this.careOrder.getId())
			.contents(this.contents)
			.point(this.point)
			.build();

		response.setCreatedAndUpdated(this.createdAndModified);
		return response;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void delete() {
		this.publish = IsYn.N;
	}

	/* =================================================================
 	 * Business
       ================================================================= */
}
