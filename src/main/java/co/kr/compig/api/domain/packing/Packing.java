package co.kr.compig.api.domain.packing;

import static co.kr.compig.global.code.PeriodType.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.domain.wallet.Wallet;
import co.kr.compig.api.presentation.packing.request.PackingUpdateRequest;
import co.kr.compig.api.presentation.packing.response.PackingDetailResponse;
import co.kr.compig.global.code.PeriodType;
import co.kr.compig.global.code.converter.PeriodTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import co.kr.compig.global.error.exception.BizException;
import jakarta.persistence.CascadeType;
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
	name = "packing_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "packing_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Packing {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "packing_seq_gen")
	@Column(name = "packing_id")
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
	@JoinColumn(name = "care_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_packing"))
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private CareOrder careOrder;

	@Builder.Default
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "settle_id", nullable = false, foreignKey = @ForeignKey(name = "fk02_packing"))
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Settle settle = new Settle();

	@Builder.Default
	@OneToMany(
		mappedBy = "packing", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Wallet> wallets = new HashSet<>();

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

	//지불해야 할 금액 계산(간병일 하루)
	public Integer calculatePaymentPriceOneDay() {
		if (this.amount == null) {
			throw new BizException("금액을 입력해주세요.");
		}
		if (this.periodType == PART_TIME) {
			// 두 날짜와 시간 사이의 차이 계산
			Duration duration = Duration.between(startDateTime, endDateTime);
			// 차이를 시간 단위로 변환
			long hours = duration.toHours();
			// 금액 * 시간
			long result = this.amount.longValue() * hours;

			// 100에서 수수료를 더한 뒤, 100으로 나누어 실제 곱해야 할 비율을 계산합니다.
			// discountRate가 Integer이므로, 100.0과 같이 실수로 나누어 자동 형변환을 유도합니다.
			double multiplier = (100.0 + this.settle.getGuardianFees()) / 100.0;

			// 이제 result에 multiplier를 곱하여 보호자 수수료를 적용한 값을 구합니다.
			long paymentResult = (long)(result * multiplier);

			return (int)paymentResult;
		}
		if (this.periodType == PERIOD) {
			// 100에서 수수료를 더한 뒤, 100으로 나누어 실제 곱해야 할 비율을 계산합니다.
			// discountRate가 Integer이므로, 100.0과 같이 실수로 나누어 자동 형변환을 유도합니다.
			double multiplier = (100.0 + this.settle.getGuardianFees()) / 100.0;

			// 이제 result에 multiplier를 곱하여 보호자 수수료를 적용한 값을 구합니다.
			long paymentResult = (long)(this.amount * multiplier);

			return (int)paymentResult;
		}
		throw new BizException("계산 될 수 없습니다.");
	}
}
