package co.kr.compig.api.domain.packing;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.presentation.packing.request.PackingUpdateRequest;
import co.kr.compig.api.presentation.packing.response.FackingDetailResponse;
import co.kr.compig.global.code.LocationType;
import co.kr.compig.global.code.PeriodType;
import co.kr.compig.global.code.converter.LocationTypeConverter;
import co.kr.compig.global.code.converter.PeriodTypeConverter;
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
	private LocalDateTime startDateTime; // 시작 날짜

	@Column
	private LocalDateTime endDateTime; // 종료 날짜

	@Column(nullable = false)
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전

	@Column
	@Convert(converter = PeriodTypeConverter.class)
	private PeriodType periodType; // 시간제, 기간제

	@Column
	private Integer partTime; //파트타임 시간 시간제 일 경우 필수

	@Column(length = 100)
	private String partnerNm; // 간병인 이름

	@Column(length = 100)
	private String partnerTelNo; // 간병인 전화번호

	@Column
	@Convert(converter = LocationTypeConverter.class)
	private LocationType locationType = LocationType.HOME; // 간병 장소 종류

	@Column(length = 10)
	private String addressCd; // 간병 장소 우편 번호

	@Column(length = 200)
	private String address1; // 간병 장소 주소

	@Column(length = 200)
	private String address2; // 간병 장소 상세 주소

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
	public FackingDetailResponse toFackingDetailResponse() {
		FackingDetailResponse build = FackingDetailResponse.builder()
			.fackingId(this.id)
			.careOrderId(this.careOrder.getId())
			.amount(this.amount)
			.periodType(this.periodType)
			.partTime(this.partTime)
			.partnerNm(this.partnerNm)
			.partnerTelNo(this.partnerTelNo)
			.locationType(this.locationType)
			.addressCd(this.addressCd)
			.address1(this.address1)
			.address2(this.address2)
			.build();
		build.setCreatedAndUpdated(this.createdAndModified);
		return build;
	}

	public void update(PackingUpdateRequest packingUpdateRequest) {
	}

}
