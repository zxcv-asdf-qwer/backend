package co.kr.compig.api.domain.order;

import static co.kr.compig.global.code.OrderStatus.*;
import static co.kr.compig.global.code.PeriodType.*;
import static co.kr.compig.global.utils.CalculateUtil.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.common.util.CollectionUtil;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.kr.compig.api.domain.apply.Apply;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.memo.Memo;
import co.kr.compig.api.domain.packing.Facking;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.domain.review.Review;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderTerminateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderPageResponse;
import co.kr.compig.api.presentation.order.response.GuardianCareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.UserCareOrderResponse;
import co.kr.compig.global.code.ApplyStatus;
import co.kr.compig.global.code.CareOrderProcessType;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.OrderStatus;
import co.kr.compig.global.code.OrderType;
import co.kr.compig.global.code.PeriodType;
import co.kr.compig.global.code.converter.CareOrderProcessTypeConverter;
import co.kr.compig.global.code.converter.OrderStatusConverter;
import co.kr.compig.global.code.converter.OrderTypeConverter;
import co.kr.compig.global.code.converter.PeriodTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import co.kr.compig.global.error.exception.BizException;
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
	private LocalDateTime startDateTime; // 시작 날짜

	@Column
	private LocalDateTime endDateTime; // 종료 날짜

	@Column
	private LocalDateTime realEndDateTime; //진짜 종료 날짜

	@Column(length = 15)
	@Builder.Default
	@Convert(converter = OrderStatusConverter.class)
	private OrderStatus orderStatus = OrderStatus.MATCHING_WAITING; // 공고 상태

	@Column(length = 10)
	@Convert(converter = OrderTypeConverter.class)
	private OrderType orderType; // 공고 종류

	@Column
	@Enumerated(EnumType.STRING)
	private IsYn publishYn; // 게시 여부

	@Column
	private String title; // 제목

	@Column(columnDefinition = "TEXT")
	private String orderRequest; // 요청사항

	@Column(length = 15)
	@Convert(converter = CareOrderProcessTypeConverter.class)
	private CareOrderProcessType careOrderProcessType; // 매칭 구분

	@Column(nullable = false)
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전

	@Column
	@Convert(converter = PeriodTypeConverter.class)
	private PeriodType periodType; // 시간제, 기간제

	@Column
	private Integer partTime; //파트타임 시간 시간제 일 경우 필수

  /* =================================================================
   * Domain mapping
     ================================================================= */

	@Builder.Default
	@OneToMany(
		mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Apply> applys = new HashSet<>();

	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk01_order_patient"))
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Member member; // Member id 보호자

	@Builder.Default
	@JoinColumn(name = "order_patient_id", nullable = false, foreignKey = @ForeignKey(name = "fk03_care_order"))
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private OrderPatient orderPatient = new OrderPatient();

	@Builder.Default
	@OneToMany(mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Packing> packages = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Facking> fackages = new HashSet<>();

	@Builder.Default
	@OneToMany(
		mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Payment> payments = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Memo> memos = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Review> reviews = new HashSet<>();

	@Builder.Default
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "settle_id", foreignKey = @ForeignKey(name = "fk04_care_order"))
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Settle settle = new Settle();
	/* =================================================================
	 * Relation method
	   ================================================================= */

	public void addPacking(Packing packing) {
		this.packages.add(packing);
		packing.setCareOrder(this);
	}

	public void addFacking(Facking facking) {
		this.fackages.add(facking);
		facking.setCareOrder(this);
	}

	public void addPayment(Payment payment) {
		this.payments.add(payment);
		payment.setCareOrder(this);
	}

	public void addMemo(Memo memo) {
		this.memos.add(memo);
		memo.setCareOrder(this);
	}

	public void removeApply(final Apply apply) {
		this.applys.remove(apply);
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
		CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
			.amount(this.amount)
			.periodType(this.periodType)
			.partTime(this.partTime)
			.build();
		if (this.orderType.equals(OrderType.GENERAL)) {
			CareOrderDetailResponse build = CareOrderDetailResponse.builder()
				.orderId(this.id)
				.memberId(this.member.getId())
				.userNm(this.member.getUserNm())
				.telNo(this.member.getTelNo())
				.startDateTime(this.startDateTime)
				.endDateTime(this.endDateTime)
				.realEndDateTime(this.realEndDateTime)
				.orderStatus(this.orderStatus)
				.orderType(this.orderType)
				.publishYn(this.publishYn)
				.careOrderProcessType(this.careOrderProcessType)
				.periodType(this.periodType)
				.amount(this.amount)
				.totalPrice(calculatePaymentPriceOneDay(calculateRequest, this.settle.getGuardianFees()))
				.orderRequest(this.orderRequest)
				.orderPatient(this.orderPatient.toOrderPatientDetailResponse())
				.build();
			build.setCreatedAndUpdated(this.createdAndModified);
			return build;
		} else {
			CareOrderDetailResponse build = CareOrderDetailResponse.builder()
				.orderId(this.id)
				.memberId(this.member.getId())
				.userNm(this.member.getUserNm())
				.telNo(this.member.getTelNo())
				.startDateTime(this.startDateTime)
				.endDateTime(this.endDateTime)
				.realEndDateTime(this.realEndDateTime)
				.orderStatus(this.orderStatus)
				.orderType(this.orderType)
				.publishYn(this.publishYn)
				.careOrderProcessType(this.careOrderProcessType)
				.periodType(this.periodType)
				.amount(this.amount)
				.totalPrice(calculatePaymentPriceOneDay(calculateRequest, this.settle.getGuardianFees()))
				.orderRequest(this.orderRequest)
				.fackingDetailResponse(
					this.fackages.stream().findFirst().map(Facking::toFackingDetailResponse).orElse(null))
				.build();
			build.setCreatedAndUpdated(this.createdAndModified);
			return build;
		}

	}

	public GuardianCareOrderDetailResponse toGuardianCareOrderDetailResponse() {
		CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
			.amount(this.amount)
			.periodType(this.periodType)
			.partTime(this.partTime)
			.build();
		if (this.orderStatus.equals(MATCHING_COMPLETE)) {
			GuardianCareOrderDetailResponse build = GuardianCareOrderDetailResponse.builder()
				.orderId(this.id)
				.memberId(this.member.getId())
				.userNm(this.member.getUserNm())
				.telNo(this.member.getTelNo())
				.startDateTime(this.startDateTime)
				.endDateTime(this.endDateTime)
				.realEndDateTime(this.realEndDateTime)
				.orderStatus(this.orderStatus)
				.orderType(this.orderType)
				.publishYn(this.publishYn)
				.careOrderProcessType(this.careOrderProcessType)
				.periodType(this.periodType)
				.amount(this.amount)
				.totalPrice(calculatePaymentPriceOneDay(calculateRequest, this.settle.getGuardianFees()))
				.orderRequest(this.orderRequest)
				.orderPatient(this.orderPatient.toOrderPatientDetailResponse())
				.partnerMemberResponse(this.applys.stream()
					.filter(apply -> apply.getApplyStatus().equals(ApplyStatus.MATCHING_COMPLETE))
					.map(Apply::getMember)
					.findFirst()
					.orElse(new Member())
					.toPartnerMemberResponse())
				.build();
			build.setCreatedAndUpdated(this.createdAndModified);
			return build;
		} else {
			GuardianCareOrderDetailResponse build = GuardianCareOrderDetailResponse.builder()
				.orderId(this.id)
				.memberId(this.member.getId())
				.userNm(this.member.getUserNm())
				.telNo(this.member.getTelNo())
				.startDateTime(this.startDateTime)
				.endDateTime(this.endDateTime)
				.realEndDateTime(this.realEndDateTime)
				.orderStatus(this.orderStatus)
				.orderType(this.orderType)
				.publishYn(this.publishYn)
				.careOrderProcessType(this.careOrderProcessType)
				.periodType(this.periodType)
				.amount(this.amount)
				.totalPrice(calculatePaymentPriceOneDay(calculateRequest, this.settle.getGuardianFees()))
				.orderRequest(this.orderRequest)
				.orderPatient(this.orderPatient.toOrderPatientDetailResponse())
				.build();
			build.setCreatedAndUpdated(this.createdAndModified);
			return build;
		}
	}

	public CareOrderPageResponse toCareOrderPageResponse() {
		CareOrderPageResponse build = CareOrderPageResponse.builder()
			.orderId(this.id)
			.memberId(this.member.getId())
			.userNm(this.member.getUserNm())
			.telNo(this.member.getTelNo())
			.locationType(this.orderPatient.getLocationType())
			.address1(this.orderPatient.getAddress1())
			.address2(this.orderPatient.getAddress2())
			.hospitalNm(this.orderPatient.getHospitalName())
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.realEndDateTime(this.realEndDateTime)
			.orderStatus(this.orderStatus)
			.periodType(this.periodType)
			.amount(this.amount)
			.applyCount(this.applys.size())
			.memoCount(this.memos.size())
			.memberType(this.member.getMemberType())
			.build();
		build.setCreatedAndUpdated(this.createdAndModified);
		return build;
	}

	public void createOrderPacking() {
		if (CollectionUtil.isNotEmpty(this.packages)) {
			return;
		}
		long daysBetween;
		if (this.periodType.equals(PART_TIME)) { //시간제
			// 시작 날짜(2024-05-20 22:00:00) - 종료 날짜(2024-05-22 02:00:00), 파트타임 시간: 4시간
			// 시작 날짜부터 종료 날짜까지 2일 Packing 객체 생성
			// 시작 날짜(2024-05-20 10:00:00) - 종료 날짜(2024-05-22 15:00:00), 파트타임 시간: 5시간
			// 시작 날짜부터 종료 날짜까지 3일 Packing 객체 생성
			daysBetween = ChronoUnit.DAYS.between(this.startDateTime, this.endDateTime) + 1;

			for (int i = 0; i < daysBetween; i++) {
				LocalDateTime startDateTime = this.startDateTime.plusDays(i);
				LocalDateTime endDateTime = startDateTime.plusHours(this.partTime);
				Packing build = Packing.builder()
					.careOrder(this)
					.startDateTime(startDateTime)
					.endDateTime(endDateTime)
					.build();
				this.addPacking(build);
			}
		}

		if (this.periodType.equals(PERIOD)) { //기간제
			// 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
			// 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
			daysBetween = ChronoUnit.DAYS.between(this.startDateTime, this.endDateTime);
			for (int i = 0; i < daysBetween; i++) {
				LocalDateTime startDateTime = this.startDateTime.plusDays(i);
				LocalDateTime endDateTime = startDateTime.plusDays(1);
				Packing build = Packing.builder()
					.careOrder(this)
					.startDateTime(startDateTime)
					.endDateTime(endDateTime)
					.build();
				this.addPacking(build);
			}
		}
	}

	public CareOrder extension(LocalDateTime startDateTime, LocalDateTime endDateTime, Settle settle) {
		//1.start 연장하려는 간병인이, 보호자가 선택한 연장기간 중에, 매칭된 다른 간병공고와 겹치지 않은지 체크
		Member matchApplyMember = this.applys
			.stream()
			.filter(apply -> apply.getApplyStatus().equals(ApplyStatus.MATCHING_COMPLETE))
			.map(Apply::getMember)
			.findFirst()
			.orElseThrow(() -> new BizException("현재 간병 공고의 매칭된 지원자가 없습니다."));
		// 겹치는 간병 공고가 있는지 체크
		boolean hasOverlap = matchApplyMember.getApplys().stream().anyMatch(apply -> {
			LocalDateTime existingStart = apply.getCareOrder().getStartDateTime();
			LocalDateTime existingEnd = apply.getCareOrder().getEndDateTime();
			// 겹치는 조건 검사: 요청 시작 시간이 기존 종료 시간 이전이고 요청 종료 시간이 기존 시작 시간 이후인 경우
			return startDateTime.isBefore(existingEnd)
				&& endDateTime.isAfter(existingStart);
		});
		if (hasOverlap) {
			throw new BizException("매칭하고자 하는 간병인의 간병 기간이 겹칩니다.");
		}
		//1. end
		return CareOrder.builder()
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.realEndDateTime(this.realEndDateTime)
			.orderStatus(MATCHING_COMPLETE)
			.orderType(this.orderType)
			.publishYn(IsYn.Y)
			.orderRequest(this.orderRequest)
			.careOrderProcessType(CareOrderProcessType.AUTO)
			.applys(this.applys.stream()
				.filter(apply -> apply.getApplyStatus().equals(ApplyStatus.MATCHING_COMPLETE))
				.collect(Collectors.toSet()))
			.amount(this.amount)
			.periodType(this.periodType)
			.partTime(this.partTime)
			.member(this.member)
			.orderPatient(this.orderPatient)
			.settle(settle)
			.build();
	}

	public void cancelOrder(CareOrderTerminateRequest request) {
		this.orderStatus = ORDER_CANCEL;
		this.realEndDateTime = request.getRealEndDateTime();
	}

	public boolean isPayment() {
		return this.payments != null;
	}

	public UserCareOrderResponse toUserCareOrderResponse() {
		UserCareOrderResponse userCareOrderResponse = UserCareOrderResponse.builder()
			.id(this.id)
			.orderStatus(this.orderStatus)
			.orderType(this.orderType)
			.applyCount(this.applys.size())
			.address1(this.orderPatient.getAddress1())
			.address2(this.orderPatient.getAddress2())
			.periodType(this.periodType)
			.amount(this.amount)
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.build();

		userCareOrderResponse.setCreatedAndUpdated(this.createdAndModified);
		return userCareOrderResponse;
	}
}
