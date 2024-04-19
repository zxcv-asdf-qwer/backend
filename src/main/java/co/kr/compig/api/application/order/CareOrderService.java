package co.kr.compig.api.application.order;

import static co.kr.compig.global.utils.CalculateUtil.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.patient.OrderPatientService;
import co.kr.compig.api.application.settle.SettleService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.order.CareOrderRepository;
import co.kr.compig.api.domain.order.CareOrderRepositoryCustom;
import co.kr.compig.api.domain.packing.Facking;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.patient.Patient;
import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.presentation.order.request.AdminCareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderExtensionsRequest;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.request.FamilyCareOrderCreateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderResponse;
import co.kr.compig.global.code.OrderType;
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
	private final SettleService settleService;
	private final OrderPatientService orderPatientService;
	private final CareOrderRepository careOrderRepository;
	private final CareOrderRepositoryCustom careOrderRepositoryCustom;

	public Long createCareOrderAdmin(AdminCareOrderCreateRequest adminCareOrderCreateRequest) {
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
		}
		return careOrder.getId();
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
		}

		return careOrder.getId();
	}

	public Long createFamilyCareOrderGuardian(FamilyCareOrderCreateRequest familyCareOrderCreateRequest) {
		Member member = memberService.getMemberById(SecurityUtil.getMemberId());
		Patient patientById = member.getPatients()
			.stream()
			.filter(patient -> patient.getId().equals(familyCareOrderCreateRequest.getPatientId()))
			.findFirst()
			.orElseThrow(NotExistDataException::new);
		// ModelMapper modelMapper = new ModelMapper();
		// modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE).setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.LOOSE);

		OrderPatient orderPatient = orderPatientService.save(patientById.toOrderPatient());

		CareOrder careOrder = careOrderRepository.save(
			familyCareOrderCreateRequest.converterEntity(member, orderPatient));
		Settle recentSettle = settleService.getRecentSettle();

		int totalPrice = 0;

		Facking build = familyCareOrderCreateRequest.toEntity(careOrder, recentSettle);

		careOrder.addFacking(build);
		// 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
		// 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
		long daysBetween = ChronoUnit.DAYS.between(careOrder.getStartDateTime(), careOrder.getEndDateTime());
		for (int i = 0; i <= daysBetween; i++) {
			totalPrice += calculatePaymentPriceOneDay(familyCareOrderCreateRequest.toCareOrderCalculateRequest(),
				recentSettle.getGuardianFees());
		}

		// TODO 결제pg요청 프로세스
		//return을 결제 url 로 넘기기
		careOrder.addPayment(Payment.builder()
			.price(totalPrice)//보호자 수수료 적용한 금액(보호자가 지불해야 하는 금액)(간병일 전체)
			.build());
		return careOrder.getId();
	}

	@Transactional(readOnly = true)
	public Page<CareOrderDetailResponse> pageListCareOrder(CareOrderSearchRequest careOrderSearchRequest) {

		return careOrderRepositoryCustom.findPage(careOrderSearchRequest);
	}

	@Transactional(readOnly = true)
	public CareOrderDetailResponse getCareOrder(Long careOrderId) {
		CareOrder careOrder = this.getCareOrderById(careOrderId);
		return careOrder.toCareOrderDetailResponse();
	}

	@Transactional(readOnly = true)
	public int getCareOrderCalculate(CareOrderCalculateRequest careOrderCalculateRequest) {
		int totalPrice = 0;
		long daysBetween = ChronoUnit.DAYS.between(careOrderCalculateRequest.getStartDateTime(),
			careOrderCalculateRequest.getEndDateTime());
		Settle recentSettle = settleService.getRecentSettle();
		for (int i = 0; i <= daysBetween; i++) {
			totalPrice += calculatePaymentPriceOneDay(careOrderCalculateRequest, recentSettle.getGuardianFees());
		}
		return totalPrice;
	}

	@Transactional(readOnly = true)
	public CareOrder getCareOrderById(Long careOrderId) {
		return careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
	}

	public Long extensionsCareOrder(Long careOrderId, CareOrderExtensionsRequest careOrderExtensionsRequest) {
		CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
			.startDateTime(careOrderExtensionsRequest.getStartDateTime())
			.endDateTime(careOrderExtensionsRequest.getEndDateTime())
			.amount(careOrderExtensionsRequest.getAmount())
			.periodType(careOrderExtensionsRequest.getPeriodType())
			.build();

		CareOrder careOrder = careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
		//간병 연장
		CareOrder extensionOrder = careOrder.extension(careOrderExtensionsRequest.getStartDateTime(),
			careOrderExtensionsRequest.getEndDateTime());

		int totalPrice = 0;
		long daysBetween = ChronoUnit.DAYS.between(careOrderExtensionsRequest.getStartDateTime(),
			careOrderExtensionsRequest.getEndDateTime());
		Settle recentSettle = settleService.getRecentSettle();

		for (int i = 0; i <= daysBetween; i++) {
			if (extensionOrder.getOrderType().equals(OrderType.GENERAL)) {
				// 지원자 o, packing
				// 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
				// 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
				LocalDateTime startDateTime = extensionOrder.getStartDateTime().plusDays(i);
				LocalDateTime endDateTime = startDateTime.plusDays(1);
				Packing build = Packing.builder()
					.careOrder(extensionOrder)
					.settle(recentSettle)
					.periodType(careOrderExtensionsRequest.getPeriodType())
					.amount(careOrderExtensionsRequest.getAmount())
					.startDateTime(startDateTime)
					.endDateTime(endDateTime)
					.build();
				extensionOrder.addPacking(build);
			}

			totalPrice += calculatePaymentPriceOneDay(calculateRequest, recentSettle.getGuardianFees());
		}
		log.info(String.valueOf(totalPrice));

		// TODO 결제pg요청 프로세스
		//return을 결제 url 로 넘기기
		return extensionOrder.getId();
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
