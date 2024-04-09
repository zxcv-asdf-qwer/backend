package co.kr.compig.api.domain.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.api.domain.apply.Apply;
import co.kr.compig.api.domain.code.CareOrderRegisterType;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.OrderStatusCode;
import co.kr.compig.api.domain.code.PeriodType;
import co.kr.compig.api.domain.code.converter.CareOrderRegisterTypeConverter;
import co.kr.compig.api.domain.code.converter.OrderStatusCodeConverter;
import co.kr.compig.api.domain.code.converter.PeriodTypeConverter;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.presentation.apply.response.ApplyCareOrderResponse;
import co.kr.compig.api.presentation.order.request.CareOrderUpdateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import jakarta.persistence.OneToOne;
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
	name = "care_order_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "care_order_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class CareOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "care_order_seq_gen")
	@Column(name = "care_order_id")
	private Long id;

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startDateTime; // 시작 날짜

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endDateTime; // 종료 날짜

	@Column
	@Builder.Default
	@Convert(converter = OrderStatusCodeConverter.class)
	private OrderStatusCode orderStatus = OrderStatusCode.MATCHING_WAITING; // 공고 상태

	@Column
	@Enumerated(EnumType.STRING)
	private IsYn publishYn; // 게시 여부

	@Column
	@Convert(converter = PeriodTypeConverter.class)
	private PeriodType periodType; // 시간제, 기간제

	@Column
	private String title; // 제목

	@Column(columnDefinition = "TEXT")
	private String orderRequest; // 요청사항

	@Column
	@Convert(converter = CareOrderRegisterTypeConverter.class)
	private CareOrderRegisterType careOrderRegisterType; // 등록 구분

  /* =================================================================
   * Domain mapping
     ================================================================= */

	@Builder.Default
	@OneToMany(
		mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Apply> applys = new HashSet<>();

	@Builder.Default
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_care_order"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member = new Member(); // Member id

	@Builder.Default
	@JoinColumn(name = "order_patient_id", nullable = false, foreignKey = @ForeignKey(name = "fk02_care_order"))
	@OneToOne(fetch = FetchType.LAZY)
	private OrderPatient orderPatient = new OrderPatient();

	public CareOrderDetailResponse toCareOrderDetailResponse() {
		Set<ApplyCareOrderResponse> applyResponses = applys.stream()
			.map(Apply::toApplyCareOrderResponse) // Apply 객체를 ApplyDetailResponse 객체로 매핑
			.collect(Collectors.toSet());

		return CareOrderDetailResponse.builder()
			.id(this.id)
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.orderStatusCode(this.orderStatus)
			.publishYn(this.publishYn)
			.periodType(this.periodType)
			.careOrderRegisterType(this.careOrderRegisterType)
			.orderRequest(this.orderRequest)
			.userNm(member.getUserNm())
			.telNo(member.getTelNo())
			.name(orderPatient.getName())
			.gender(orderPatient.getGender())
			.birthDate(orderPatient.getBirthDate())
			.height(orderPatient.getHeight())
			.weight(orderPatient.getWeight())
			.diseaseNm(orderPatient.getDiseaseNm())
			.selfToiletAvailability(orderPatient.getSelfToiletAvailability())
			.moveAvailability(orderPatient.getMoveAvailability())
			.mealAvailability(orderPatient.getMealAvailability())
			.genderPreference(orderPatient.getGenderPreference())
			.covid19Test(orderPatient.getCovid19Test())
			.patientRequest(orderPatient.getPatientRequest())
			.locationType(orderPatient.getLocationType())
			.addressCd(orderPatient.getAddressCd())
			.address1(orderPatient.getAddress1())
			.address2(orderPatient.getAddress2())
			.applies(applyResponses)
			.build();
	}

	public void update(CareOrderUpdateRequest careOrderUpdateRequest) {
		this.startDateTime = careOrderUpdateRequest.getStartDateTime();
		this.endDateTime = careOrderUpdateRequest.getEndDateTime();
	}
}
