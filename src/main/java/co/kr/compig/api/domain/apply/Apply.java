package co.kr.compig.api.domain.apply;

import co.kr.compig.api.domain.code.ApplyStatusCode;
import co.kr.compig.api.domain.code.converter.ApplyStatusConverter;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.apply.request.ApplyUpdateRequest;
import co.kr.compig.api.presentation.apply.response.ApplyCareOrderResponse;
import co.kr.compig.api.presentation.apply.response.ApplyDetailResponse;
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
	private ApplyStatusCode applyStatusCode = ApplyStatusCode.MATCHING_WAIT;

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@JoinColumn(name = "care_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_apply"))
	@ManyToOne(fetch = FetchType.LAZY)
	private CareOrder careOrder = new CareOrder();

	@Builder.Default
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk02_apply"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member = new Member();

	public ApplyDetailResponse toApplyDetailResponse(Member member, CareOrder careOrder) {
		return ApplyDetailResponse.builder()
			.id(this.id)
			.memberId(member.getId())
			.careOrderId(careOrder.getId())
			.build();
	}

	public void update(ApplyUpdateRequest applyUpdateRequest) {
		this.applyStatusCode = applyUpdateRequest.getApplyStatusCode();
	}

	public ApplyCareOrderResponse toApplyCareOrderResponse() {
		return ApplyCareOrderResponse.builder()
			.applyId(this.id)
			.applyStatusCode(this.applyStatusCode)
			.build();
	}
}

