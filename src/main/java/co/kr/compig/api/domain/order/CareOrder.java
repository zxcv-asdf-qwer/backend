package co.kr.compig.api.domain.order;

import static co.kr.compig.global.code.OrderStatus.*;
import static co.kr.compig.global.utils.CalculateUtil.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderTerminateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderPageResponse;
import co.kr.compig.global.code.ApplyStatus;
import co.kr.compig.global.code.CareOrderProcessType;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.OrderStatus;
import co.kr.compig.global.code.OrderType;
import co.kr.compig.global.code.converter.CareOrderProcessTypeConverter;
import co.kr.compig.global.code.converter.OrderStatusConverter;
import co.kr.compig.global.code.converter.OrderTypeConverter;
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
	private OrderType orderType; // 공고 상태

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
	@OneToMany(mappedBy = "careOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Packing> packages = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "careOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Facking> fackages = new HashSet<>();

	@Builder.Default
	@OneToMany(
		mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Payment> payments = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "careOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Memo> memos = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "careOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Review> reviews = new HashSet<>();
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
		if (this.orderType.equals(OrderType.GENERAL)) {
			Optional<Packing> firstPacking = this.packages.stream()
				.findFirst();
			AtomicInteger totalPrice = new AtomicInteger();

			this.packages.forEach(packing -> {
				CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
					.amount(packing.getAmount())
					.periodType(packing.getPeriodType())
					.partTime(packing.getPartTime())
					.build();
				totalPrice.addAndGet(calculatePaymentPriceOneDay(calculateRequest,
					packing.getSettle().getGuardianFees()));
			});
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
				.periodType(firstPacking
					.map(Packing::getPeriodType)
					.orElse(null))
				.amount(firstPacking
					.map(Packing::getAmount)
					.orElse(null))
				.totalPrice(totalPrice.get())
				.orderRequest(this.orderRequest)
				.orderPatient(this.orderPatient.toOrderPatientDetailResponse())
				.build();
			build.setCreatedAndUpdated(this.createdAndModified);
			return build;
		} else {
			Optional<Facking> firstFacking = this.fackages.stream()
				.findFirst();

			AtomicInteger totalPrice = new AtomicInteger();

			this.fackages.forEach(facking -> {
				CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
					.amount(facking.getAmount())
					.periodType(facking.getPeriodType())
					.partTime(facking.getPartTime())
					.build();
				totalPrice.addAndGet(calculatePaymentPriceOneDay(calculateRequest,
					facking.getSettle().getGuardianFees()));
			});
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
				.periodType(firstFacking.map(Facking::getPeriodType).orElse(null))
				.amount(firstFacking.map(Facking::getAmount).orElse(null))
				.totalPrice(totalPrice.get())
				.orderRequest(this.orderRequest)
				.fackingDetailResponse(firstFacking.map(Facking::toFackingDetailResponse).orElse(null))
				.build();
			build.setCreatedAndUpdated(this.createdAndModified);
			return build;
		}

	}

	public CareOrderPageResponse toCareOrderPageResponse() {
		Optional<Packing> firstPacking = packages.stream()
			.findFirst();
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
			.periodType(firstPacking
				.map(Packing::getPeriodType)
				.orElse(null))
			.amount(firstPacking
				.map(Packing::getAmount)
				.orElse(null))
			.applyCount(this.applys.size())
			.memoCount(this.memos.size())
			.memberType(this.member.getMemberType())
			.build();
		build.setCreatedAndUpdated(this.createdAndModified);
		return build;
	}

	public CareOrder extension(LocalDateTime startDateTime, LocalDateTime endDateTime) {
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
			.orderStatus(OrderStatus.MATCHING_COMPLETE)
			.orderType(this.orderType)
			.publishYn(IsYn.Y)
			.orderRequest(this.orderRequest)
			.careOrderProcessType(CareOrderProcessType.AUTO)
			.applys(this.applys.stream()
				.filter(apply -> apply.getApplyStatus().equals(ApplyStatus.MATCHING_COMPLETE))
				.collect(Collectors.toSet()))
			.member(this.member)
			.orderPatient(this.orderPatient)
			.build();
	}

	public void cancelOrder(CareOrderTerminateRequest request) {
		this.orderStatus = ORDER_CANCEL;
		this.realEndDateTime = request.getRealEndDateTime();
	}

	public boolean isPayment() {
		return this.payments != null;
	}

}
