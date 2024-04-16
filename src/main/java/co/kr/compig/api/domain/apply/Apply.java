package co.kr.compig.api.domain.apply;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.kr.compig.api.domain.code.ApplyStatus;
import co.kr.compig.api.domain.code.converter.ApplyStatusConverter;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.apply.request.ApplyUpdateRequest;
import co.kr.compig.api.presentation.apply.response.ApplyCareOrderResponse;
import co.kr.compig.api.presentation.apply.response.ApplyDetailResponse;
import co.kr.compig.api.presentation.apply.response.ApplyResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
	name = "apply_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "apply_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Apply {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "apply_seq_gen")
	@Column(name = "apply_id")
	private Long id;

	@Column(length = 10)
	@Convert(converter = ApplyStatusConverter.class)
	@Builder.Default
	private ApplyStatus applyStatus = ApplyStatus.MATCHING_WAIT;

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@JoinColumn(name = "care_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_apply"))
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private CareOrder careOrder = new CareOrder();

	@Builder.Default
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk02_apply"))
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Member member = new Member();

	public ApplyDetailResponse toApplyDetailResponse(Member member, CareOrder careOrder) {
		return ApplyDetailResponse.builder()
			.id(this.id)
			.memberId(member.getId())
			.careOrderId(careOrder.getId())
			.build();
	}

	public void update(ApplyUpdateRequest applyUpdateRequest) {
		this.applyStatus = applyUpdateRequest.getApplyStatus();
	}

	public ApplyCareOrderResponse toApplyCareOrderResponse() {
		return ApplyCareOrderResponse.builder()
			.applyId(this.id)
			.applyStatus(this.applyStatus)
			.build();
	}

	public ApplyResponse toApplyResponse() {
		return ApplyResponse.builder()
			.applyId(this.id)
			.memberId(this.member.getId())
			.careOrderId(this.careOrder.getId())
			.applyStatus(this.applyStatus)
			.build();
	}
}

