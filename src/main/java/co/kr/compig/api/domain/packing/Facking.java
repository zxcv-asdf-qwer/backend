package co.kr.compig.api.domain.packing;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.kr.compig.api.domain.code.PeriodType;
import co.kr.compig.api.domain.code.converter.PeriodTypeConverter;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.presentation.packing.request.PackingUpdateRequest;
import co.kr.compig.api.presentation.packing.response.PackingDetailResponse;
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
	name = "facking_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "facking_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Facking {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "facking_seq_gen")
	@Column(name = "facking_id")
	private Long id;

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startDateTime; // 시작 날짜

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endDateTime; // 종료 날짜

	@Column(nullable = false)
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전

	@Column
	@Convert(converter = PeriodTypeConverter.class)
	private PeriodType periodType; // 시간제, 기간제

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@ManyToOne
	@JoinColumn(name = "care_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_facking"))
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private CareOrder careOrder;

	@Builder.Default
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "settle_id", nullable = false, foreignKey = @ForeignKey(name = "fk02_facking"))
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Settle settle = new Settle();

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

	/* =================================================================
 	 * Business
       ================================================================= */
	public PackingDetailResponse toPackingDetailResponse() {
		return PackingDetailResponse.builder()
			.packingId(this.id)
			.careOrderId(this.careOrder.getId())
			.build();
	}

	public void update(PackingUpdateRequest packingUpdateRequest) {
	}

}
