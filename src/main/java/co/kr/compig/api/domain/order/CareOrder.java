package co.kr.compig.api.domain.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.kr.compig.api.domain.apply.Apply;
import co.kr.compig.api.domain.code.CareOrderProcessType;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.OrderStatus;
import co.kr.compig.api.domain.code.converter.CareOrderProcessTypeConverter;
import co.kr.compig.api.domain.code.converter.OrderStatusConverter;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.NoMember;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.presentation.apply.response.ApplyCareOrderResponse;
import co.kr.compig.api.presentation.order.request.CareOrderUpdateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
	@Convert(converter = OrderStatusConverter.class)
	private OrderStatus orderStatus = OrderStatus.MATCHING_WAITING; // 공고 상태

	@Column
	@Enumerated(EnumType.STRING)
	private IsYn publishYn; // 게시 여부

	@Column
	private String title; // 제목

	@Column(columnDefinition = "TEXT")
	private String orderRequest; // 요청사항

	@Column
	@Convert(converter = CareOrderProcessTypeConverter.class)
	private CareOrderProcessType careOrderProcessType; // 매칭 구분

  /* =================================================================
   * Domain mapping
     ================================================================= */

	@Builder.Default
	@OneToMany(
		mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Apply> applys = new HashSet<>();

	@Builder.Default
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk01_care_order"))
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Member member = new Member(); // Member id

	@Builder.Default
	@JoinColumn(name = "no_member_id", foreignKey = @ForeignKey(name = "fk02_care_order"))
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private NoMember noMember = new NoMember(); // Member id

	@Builder.Default
	@JoinColumn(name = "order_patient_id", nullable = false, foreignKey = @ForeignKey(name = "fk03_care_order"))
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private OrderPatient orderPatient = new OrderPatient();

	@Builder.Default
	@OneToMany(mappedBy = "careOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Packing> packages = new HashSet<>();

	@Builder.Default
	@OneToMany(
		mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Payment> payments = new HashSet<>();

	/* =================================================================
	 * Relation method
	   ================================================================= */

	public void addPacking(Packing packing) {
		this.packages.add(packing);
		packing.setCareOrder(this);
	}

	public void addPayment(Payment payment) {
		this.payments.add(payment);
		payment.setCareOrder(this);
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

	public CareOrderDetailResponse toCareOrderDetailResponse() {
		Set<ApplyCareOrderResponse> applyResponses = applys.stream()
			.map(Apply::toApplyCareOrderResponse) // Apply 객체를 ApplyDetailResponse 객체로 매핑
			.collect(Collectors.toSet());

		return CareOrderDetailResponse.builder()
			.id(this.id)
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.orderStatus(this.orderStatus)
			.publishYn(this.publishYn)
			.careOrderProcessType(this.careOrderProcessType)
			.orderRequest(this.orderRequest)
			.userNm(member.getUserNm())
			.telNo(member.getTelNo())
			.name(orderPatient.getName())
			.gender(orderPatient.getGender())
			.birthDate(orderPatient.getBirthDate())
			.height(orderPatient.getHeight())
			.weight(orderPatient.getWeight())
			.diseaseNms(orderPatient.getDiseaseNms())
			.selfToiletAvailabilities(orderPatient.getSelfToiletAvailabilities())
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
