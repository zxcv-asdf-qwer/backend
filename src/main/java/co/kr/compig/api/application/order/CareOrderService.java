package co.kr.compig.api.application.order;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.member.NoMemberService;
import co.kr.compig.api.application.patient.OrderPatientService;
import co.kr.compig.api.application.settle.SettleService;
import co.kr.compig.api.domain.code.MemberType;
import co.kr.compig.api.domain.code.OrderStatus;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.NoMember;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.order.CareOrderRepository;
import co.kr.compig.api.domain.order.CareOrderRepositoryCustom;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.patient.Patient;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.presentation.order.request.AdminCareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.request.CareOrderUpdateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderResponse;
import co.kr.compig.global.dto.pagination.PageResponse;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CareOrderService {

	private final MemberService memberService;
	private final NoMemberService noMemberService;
	private final SettleService settleService;
	private final OrderPatientService orderPatientService;
	private final CareOrderRepository careOrderRepository;
	private final CareOrderRepositoryCustom careOrderRepositoryCustom;

	public Long createCareOrderAdmin(AdminCareOrderCreateRequest adminCareOrderCreateRequest) {
		if (adminCareOrderCreateRequest.getMemberType().equals(MemberType.MEMBER)) {
			Member member = memberService.getMemberById(adminCareOrderCreateRequest.getMemberId());
			Patient patientById = member.getPatients()
				.stream()
				.filter(patient -> patient.getId().equals(adminCareOrderCreateRequest.getPatientId()))
				.findFirst()
				.orElseThrow(NotExistDataException::new);
			// ModelMapper modelMapper = new ModelMapper();
			// modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE).setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.LOOSE);

			OrderPatient orderPatient = orderPatientService.save(patientById.toOrderPatient());

			CareOrder careOrder = careOrderRepository.save(
				adminCareOrderCreateRequest.converterEntity(member, orderPatient));
			Settle recentSettle = settleService.getRecentSettle();
			// int totalPrice = 0;
			// 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
			// 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
			long daysBetween = ChronoUnit.DAYS.between(careOrder.getStartDateTime(), careOrder.getEndDateTime());
			for (int i = 0; i <= daysBetween; i++) {
				LocalDateTime startDateTime = careOrder.getStartDateTime().plusDays(i);
				LocalDateTime endDateTime = startDateTime.plusDays(1);
				Packing build = Packing.builder()
					.careOrder(careOrder)
					.settle(recentSettle)
					.periodType(adminCareOrderCreateRequest.getPeriodType())
					.amount(adminCareOrderCreateRequest.getAmount())
					.startDateTime(startDateTime)
					.endDateTime(endDateTime)
					.build();
				careOrder.addPacking(build);
				// totalPrice += build.calculatePaymentPriceOneDay();
			}
			// //TODO 결제pg요청 프로세스
			// careOrder.addPayment(Payment.builder()
			// 	.price(totalPrice)//보호자 수수료 적용한 금액(보호자가 지불해야 하는 금액)(간병일 전체)
			// 	.build());
			return careOrder.getId();
		} else {
			NoMember noMember = noMemberService.getNoMemberById(adminCareOrderCreateRequest.getMemberId());
			Patient patientById = noMember.getPatients()
				.stream()
				.filter(patient -> patient.getId().equals(adminCareOrderCreateRequest.getPatientId()))
				.findFirst()
				.orElseThrow(NotExistDataException::new);
			// ModelMapper modelMapper = new ModelMapper();
			// modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE).setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.LOOSE);

			OrderPatient orderPatient = orderPatientService.save(patientById.toOrderPatient());

			CareOrder careOrder = careOrderRepository.save(
				adminCareOrderCreateRequest.converterEntity(noMember, orderPatient));
			Settle recentSettle = settleService.getRecentSettle();
			// int totalPrice = 0;
			// 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
			// 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
			long daysBetween = ChronoUnit.DAYS.between(careOrder.getStartDateTime(), careOrder.getEndDateTime());
			for (int i = 0; i <= daysBetween; i++) {
				LocalDateTime startDateTime = careOrder.getStartDateTime().plusDays(i);
				LocalDateTime endDateTime = startDateTime.plusDays(1);
				Packing build = Packing.builder()
					.careOrder(careOrder)
					.settle(recentSettle)
					.periodType(adminCareOrderCreateRequest.getPeriodType())
					.amount(adminCareOrderCreateRequest.getAmount())
					.startDateTime(startDateTime)
					.endDateTime(endDateTime)
					.build();
				careOrder.addPacking(build);
				// totalPrice += build.calculatePaymentPriceOneDay();
			}
			// //TODO 결제pg요청 프로세스
			// careOrder.addPayment(Payment.builder()
			// 	.price(totalPrice)//보호자 수수료 적용한 금액(보호자가 지불해야 하는 금액)(간병일 전체)
			// 	.build());
			return careOrder.getId();
		}
	}

	public Long createCareOrderGuardian(CareOrderCreateRequest careOrderCreateRequest) {
		Member member = memberService.getMemberById(SecurityUtil.getMemberId());
		Patient patientById = member.getPatients()
			.stream()
			.filter(patient -> patient.getId().equals(careOrderCreateRequest.getPatientId()))
			.findFirst()
			.orElseThrow(NotExistDataException::new);
		// ModelMapper modelMapper = new ModelMapper();
		// modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE).setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.LOOSE);

		OrderPatient orderPatient = orderPatientService.save(patientById.toOrderPatient());

		CareOrder careOrder = careOrderRepository.save(
			careOrderCreateRequest.converterEntity(member, orderPatient));
		Settle recentSettle = settleService.getRecentSettle();
		// int totalPrice = 0;
		// 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
		// 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
		long daysBetween = ChronoUnit.DAYS.between(careOrder.getStartDateTime(), careOrder.getEndDateTime());
		for (int i = 0; i <= daysBetween; i++) {
			LocalDateTime startDateTime = careOrder.getStartDateTime().plusDays(i);
			LocalDateTime endDateTime = startDateTime.plusDays(1);
			Packing build = Packing.builder()
				.careOrder(careOrder)
				.settle(recentSettle)
				.periodType(careOrderCreateRequest.getPeriodType())
				.amount(careOrderCreateRequest.getAmount())
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
			careOrder.addPacking(build);
			// totalPrice += build.calculatePaymentPriceOneDay();
		}
		// //TODO 결제pg요청 프로세스
		// careOrder.addPayment(Payment.builder()
		// 	.price(totalPrice)//보호자 수수료 적용한 금액(보호자가 지불해야 하는 금액)(간병일 전체)
		// 	.build());
		return careOrder.getId();
	}

	@Transactional(readOnly = true)
	public PageResponse<CareOrderResponse> pageListCareOrder(CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable) {

		Page<CareOrderResponse> page = careOrderRepositoryCustom.findPage(careOrderSearchRequest, pageable);
		return new PageResponse<>(page.getContent(), pageable, page.getTotalElements());
	}

	@Transactional(readOnly = true)
	public CareOrderDetailResponse getCareOrder(Long careOrderId) {
		CareOrder careOrder = this.getCareOrderById(careOrderId);
		return careOrder.toCareOrderDetailResponse();
	}

	@Transactional(readOnly = true)
	public CareOrder getCareOrderById(Long careOrderId) {
		return careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
	}

	public Long updateCareOrder(Long careOrderId, CareOrderUpdateRequest careOrderUpdateRequest) {
		CareOrder careOrder = careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
		if (!careOrder.getOrderStatus().equals(OrderStatus.MATCHING_WAITING)) {
			throw new BizException("공고를 수정 할 수 없습니다.");
		}
		careOrder.update(careOrderUpdateRequest);
		return careOrder.getId();
	}

	public void cancelCareOrder(Long careOrderId) {
		CareOrder careOrder = careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
		careOrder.cancelOrder();
	}

	public Slice<CareOrderResponse> pageListCareOrderCursor(CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable) {
		return careOrderRepositoryCustom.findAllByCondition(careOrderSearchRequest, pageable);
	}
}
