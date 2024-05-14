package co.kr.compig.api.domain.apply;

import static co.kr.compig.global.utils.CalculateUtil.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.apply.response.ApplyDetailResponse;
import co.kr.compig.global.code.ApplyStatus;
import co.kr.compig.global.code.converter.ApplyStatusConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
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
	private ApplyStatus applyStatus;

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
	private Member member = new Member(); //간병인

	/* =================================================================
	 * Default columns
	 ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	public ApplyDetailResponse toApplyDetailResponse() {
		ApplyDetailResponse build = ApplyDetailResponse.builder()
			.applyId(this.id)
			.memberId(this.member.getId())
			.userNm(this.member.getUserNm())
			.gender(this.member.getGender())
			.age(calculateAgeFromJumin(this.member.getJumin1(), this.member.getJumin2()))
			.telNo(this.member.getTelNo())
			.careOrderId(this.careOrder.getId())
			.applyStatus(this.applyStatus)
			.build();
		build.setCreatedAndUpdated(this.createdAndModified);

		return build;
	}

	public void setApplyStatus(ApplyStatus applyStatus) {
		this.applyStatus = applyStatus;
	}
}

