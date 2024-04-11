package co.kr.compig.api.domain.settle;

import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.presentation.settle.response.SettleResponse;
import co.kr.compig.global.embedded.Created;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	name = "settle_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "settle_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Settle {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settle_seq_gen")
	@Column(name = "settle_id")
	private Long id;

	@Column
	private Long settleGroupId; // 간병 요소 그룹 ID

	@Column
	private String element; // 요소명

	@Column
	private Integer amount; // 금액

	@Column
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private UseYn useYn = UseYn.Y;

	/* =================================================================
	* Domain mapping
	================================================================= */
	// @Builder.Default
	// @JoinColumn(name = "settle_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_settle"))
	// @ManyToOne(fetch = FetchType.LAZY)
	// private SettleGroup settleGroup = new SettleGroup();

	/* =================================================================
	* Relation method
	================================================================= */
	public SettleResponse toSettleResponse() {
		return SettleResponse.builder()
			.settleId(this.id)
			.element(this.element)
			.amount(this.amount)
			.settleGroupId(this.settleGroupId)
			.useYn(this.useYn)
			.build();
	}

	/* =================================================================
	* Default columns
	================================================================= */
	@Embedded
	@Builder.Default
	private Created createdAndModified = new Created();

	public void setUseYn() {
		this.useYn = UseYn.N;
	}
}
