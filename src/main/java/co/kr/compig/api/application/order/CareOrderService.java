package co.kr.compig.api.application.order;

import static co.kr.compig.global.utils.CalculateUtil.*;
import static co.kr.compig.global.utils.KeyGen.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.application.info.InfoService;
import co.kr.compig.api.application.info.push.model.MessageDto;
import co.kr.compig.api.application.info.push.model.NoticeCode;
import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.patient.OrderPatientService;
import co.kr.compig.api.application.settle.SettleService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.QMember;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.order.CareOrderRepository;
import co.kr.compig.api.domain.order.CareOrderRepositoryCustom;
import co.kr.compig.api.domain.packing.Facking;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.patient.Patient;
import co.kr.compig.api.domain.push.Device;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.infrastructure.pay.PayApi;
import co.kr.compig.api.infrastructure.pay.model.SmsPayRequest;
import co.kr.compig.api.infrastructure.pay.model.SmsPayResponse;
import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderExtensionsRequest;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.request.CareOrderTerminateRequest;
import co.kr.compig.api.presentation.order.request.FamilyCareOrderCreateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderPageResponse;
import co.kr.compig.api.presentation.order.response.GuardianCareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.UserCareOrderResponse;
import co.kr.compig.global.code.LocationType;
import co.kr.compig.global.code.OrderStatus;
import co.kr.compig.global.code.UseYn;
import co.kr.compig.global.code.UserType;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.error.model.ErrorCode;
import co.kr.compig.global.utils.GsonLocalDateTimeAdapter;
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
	private final PayApi payApi;
	private final InfoService infoService;
	private final JPAQueryFactory jpaQueryFactory;

	@Value("${api.pay.mid}")
	private String payMid;

	/**
	 * 관리자 간병공고 등록
	 * 1 patient -> order_patient
	 * 2 order save
	 * 3 send noti
	 */
	public Long createCareOrderAdmin(String memberId, CareOrderCreateRequest careOrderCreateRequest) {
		Member member = memberService.getMemberById(memberId);
		//start 1 patient -> order_patient
		Patient patientById = member.getPatients()
			.stream()
			.filter(patient -> patient.getId().equals(careOrderCreateRequest.getPatientId()))
			.findFirst()
			.orElseThrow(NotExistDataException::new);
		// ModelMapper modelMapper = new ModelMapper();
		// modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE).setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.LOOSE);
		OrderPatient orderPatient = orderPatientService.save(patientById.toOrderPatient());
		//end 1 patient -> order_patient

		//start 2 order save
		CareOrder careOrder = careOrderRepository.save(
			careOrderCreateRequest.converterEntity(member, orderPatient));
		//end 2 order save

		// Settle recentSettle = settleService.getRecentSettle();
		// long daysBetween;
		// if (careOrderCreateRequest.getPeriodType().equals(PART_TIME)) { //시간제
		// 	// 종료 날짜(2024-05-20 22:00:00) - 시작 날짜(2024-05-22 02:00:00), 파트타임 시간: 4시간
		// 	// 시작 날짜부터 종료 날짜까지 2일 Packing 객체 생성
		// 	// 종료 날짜(2024-05-20 10:00:00) - 시작 날짜(2024-05-22 15:00:00), 파트타임 시간: 5시간
		// 	// 시작 날짜부터 종료 날짜까지 3일 Packing 객체 생성
		// 	daysBetween = ChronoUnit.DAYS.between(careOrder.getStartDateTime(), careOrder.getEndDateTime()) + 1;
		//
		// 	for (int i = 0; i < daysBetween; i++) {
		// 		LocalDateTime startDateTime = careOrder.getStartDateTime().plusDays(i);
		// 		LocalDateTime endDateTime = startDateTime.plusHours(careOrderCreateRequest.getPartTime());
		// 		Packing build = careOrderCreateRequest.toEntity(careOrder, recentSettle, startDateTime, endDateTime);
		//
		// 		careOrder.addPacking(build);
		// 	}
		// }
		//
		// if (careOrderCreateRequest.getPeriodType().equals(PERIOD)) { //기간제
		// 	// 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
		// 	// 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
		// 	daysBetween = ChronoUnit.DAYS.between(careOrder.getStartDateTime(), careOrder.getEndDateTime());
		// 	for (int i = 0; i < daysBetween; i++) {
		// 		LocalDateTime startDateTime = careOrder.getStartDateTime().plusDays(i);
		// 		LocalDateTime endDateTime = startDateTime.plusDays(1);
		// 		Packing build = careOrderCreateRequest.toEntity(careOrder, recentSettle, startDateTime, endDateTime);
		//
		// 		careOrder.addPacking(build);
		// 	}
		// }

		//start 3 send noti
		try {
			NoticeCode noticeCode = NoticeCode.NEW_CREATE_ORDER;
			Map<String, Object> data = Map.of(
				"location", careOrder.getOrderPatient().getLocationType() != LocationType.HOME ?
					careOrder.getOrderPatient().getHospitalName() :
					careOrder.getOrderPatient().getLocationType().getDesc(),
				"periodType", careOrder.getPeriodType(),
				"price", careOrder.getAmount()
			);
			List<Member> partners = jpaQueryFactory.selectFrom(QMember.member)
				.where(QMember.member.userType.eq(UserType.PARTNER)
					.and(QMember.member.devices.isNotEmpty())
					.and(QMember.member.useYn.eq(UseYn.Y))).fetch();

			MessageDto messageDto = MessageDto.builder()
				.noticeCode(noticeCode)
				.targetTokens(
					partners.stream()
						.flatMap(partner -> partner.getDevices().stream())
						.map(Device::getDeviceUuid)
						.collect(Collectors.toList())
				)
				.data(data)
				.build();

			infoService.send(messageDto);
		} catch (Exception e) {
			throw new BizException(ErrorCode.ERROR, "메세지 전송 중 에러가 발생하였습니다.");
		}
		//end 3 send noti
		return careOrder.getId();
	}

	/**
	 * 유저(보호자) 간병공고 등록
	 * 1 patient -> order_patient
	 * 2 order save
	 * 3 send noti
	 */
	public Long createCareOrderGuardian(CareOrderCreateRequest careOrderCreateRequest) {
		Member member = memberService.getMemberById(SecurityUtil.getMemberId());
		//start 1 patient -> order_patient
		Patient patientById = member.getPatients()
			.stream()
			.filter(patient -> patient.getId().equals(careOrderCreateRequest.getPatientId()))
			.findFirst()
			.orElseThrow(NotExistDataException::new);
		OrderPatient orderPatient = orderPatientService.save(patientById.toOrderPatient());
		//end 1 patient -> order_patient

		//start 2 order save
		CareOrder careOrder = careOrderRepository.save(
			careOrderCreateRequest.converterEntity(member, orderPatient));
		//end 2 order save

		//start 3 send noti
		try {
			NoticeCode noticeCode = NoticeCode.NEW_CREATE_ORDER;
			Map<String, Object> data = Map.of(
				"location", careOrder.getOrderPatient().getLocationType() != LocationType.HOME ?
					careOrder.getOrderPatient().getHospitalName() :
					careOrder.getOrderPatient().getLocationType().getDesc(),
				"periodType", careOrder.getPeriodType(),
				"price", careOrder.getAmount()
			);
			List<Member> partners = jpaQueryFactory.selectFrom(QMember.member)
				.where(QMember.member.userType.eq(UserType.PARTNER)
					.and(QMember.member.devices.isNotEmpty())
					.and(QMember.member.useYn.eq(UseYn.Y))).fetch();

			MessageDto messageDto = MessageDto.builder()
				.noticeCode(noticeCode)
				.targetTokens(
					partners.stream()
						.flatMap(partner -> partner.getDevices().stream())
						.map(Device::getDeviceUuid)
						.collect(Collectors.toList())
				)
				.data(data)
				.build();

			infoService.send(messageDto);
		} catch (Exception e) {
			throw new BizException(ErrorCode.ERROR, "메세지 전송 중 에러가 발생하였습니다.");
		}
		//end 3 send noti
		return careOrder.getId();
	}

	/**
	 * 관리자 가족 간병공고 등록
	 * 1 patient -> order_patient
	 * 2 order save
	 * 3 create facking
	 */
	public Long createFamilyCareOrderAdmin(String memberId,
		FamilyCareOrderCreateRequest familyCareOrderCreateRequest) {
		Member member = memberService.getMemberById(memberId);
		//start 1 patient -> order_patient
		Patient patientById = member.getPatients()
			.stream()
			.filter(patient -> patient.getId().equals(familyCareOrderCreateRequest.getPatientId()))
			.findFirst()
			.orElseThrow(NotExistDataException::new);
		// ModelMapper modelMapper = new ModelMapper();
		// modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE).setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.LOOSE);

		OrderPatient orderPatient = orderPatientService.save(patientById.toOrderPatient());
		//end 2 patient -> order_patient

		//start 2 order save
		CareOrder careOrder = careOrderRepository.save(
			familyCareOrderCreateRequest.converterEntity(member, orderPatient));
		//end 2 order save

		//start 3 create facking
		Settle recentSettle = settleService.getRecentSettle();
		Facking build = familyCareOrderCreateRequest.toEntity(careOrder, recentSettle);
		careOrder.addFacking(build);
		//end 3 create facking

		// Settle recentSettle = settleService.getRecentSettle();
		// int totalPrice = 0;
		//
		// Facking build = familyCareOrderCreateRequest.toEntity(careOrder, recentSettle);
		//
		// careOrder.addFacking(build);
		// // 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
		// // 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
		// long daysBetween = ChronoUnit.DAYS.between(careOrder.getStartDateTime(), careOrder.getEndDateTime());
		// for (int i = 0; i < daysBetween; i++) {
		// 	totalPrice += calculatePaymentPriceOneDay(familyCareOrderCreateRequest.toCareOrderCalculateRequest(),
		// 		recentSettle.getGuardianFees());
		// }

		// SmsPayRequest smsPayRequest = SmsPayRequest.builder()
		// 	.mid(payMid)
		// 	.moid("CARE" + getRandomTimeKey())
		// 	.goodsName(" " + daysBetween + "일") //상품명
		// 	.amt(String.valueOf(totalPrice))
		// 	.buyerName(member.getUserNm())
		// 	.buyerTel(member.getTelNo())
		// 	.build();
		//
		// // 결제pg요청 프로세스
		// ResponseEntity<String> response = payApi.requestSmsPay(smsPayRequest);
		// Gson gson = new GsonBuilder()
		// 	.setPrettyPrinting()
		// 	.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
		// 	.create();
		//
		// SmsPayResponse smsPayResponse = gson.fromJson(
		// 	Objects.requireNonNull(response.getBody()),
		// 	SmsPayResponse.class
		// );
		// //return을 결제 url 로 넘기기
		// careOrder.addPayment(smsPayResponse.toEntity(totalPrice));
		//start 3 send noti
		return careOrder.getId();
	}

	/**
	 * 유저(보호자) 가족 간병공고 등록
	 * 1 patient -> order_patient
	 * 2 order save
	 * 3 create facking
	 */
	public Long createFamilyCareOrderGuardian(FamilyCareOrderCreateRequest familyCareOrderCreateRequest) {
		Member member = memberService.getMemberById(SecurityUtil.getMemberId());
		//start 1 patient -> order_patient
		Patient patientById = member.getPatients()
			.stream()
			.filter(patient -> patient.getId().equals(familyCareOrderCreateRequest.getPatientId()))
			.findFirst()
			.orElseThrow(NotExistDataException::new);
		OrderPatient orderPatient = orderPatientService.save(patientById.toOrderPatient());
		//end 1 patient -> order_patient

		//start 2 order save
		CareOrder careOrder = careOrderRepository.save(
			familyCareOrderCreateRequest.converterEntity(member, orderPatient));
		//end 2 order save

		//start 3 create facking
		Settle recentSettle = settleService.getRecentSettle();
		Facking build = familyCareOrderCreateRequest.toEntity(careOrder, recentSettle);
		careOrder.addFacking(build);
		//end 3 create facking

		// // Settle recentSettle = settleService.getRecentSettle();
		// // int totalPrice = 0;
		//
		// Facking build = familyCareOrderCreateRequest.toEntity(careOrder, recentSettle);
		//
		// careOrder.addFacking(build);
		// // 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
		// // 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
		// long daysBetween = ChronoUnit.DAYS.between(careOrder.getStartDateTime(), careOrder.getEndDateTime());
		// for (int i = 0; i < daysBetween; i++) {
		// 	totalPrice += calculatePaymentPriceOneDay(familyCareOrderCreateRequest.toCareOrderCalculateRequest(),
		// 		recentSettle.getGuardianFees());
		// }
		//
		// SmsPayRequest smsPayRequest = SmsPayRequest.builder()
		// 	.mid(payMid)
		// 	.moid("CARE" + getRandomTimeKey())
		// 	.goodsName(" " + daysBetween + "일") //상품명
		// 	.amt(String.valueOf(totalPrice))
		// 	.buyerName(member.getUserNm())
		// 	.buyerTel(member.getTelNo())
		// 	.build();
		//
		// // 결제pg요청 프로세스
		// ResponseEntity<String> response = payApi.requestSmsPay(smsPayRequest);
		// Gson gson = new GsonBuilder()
		// 	.setPrettyPrinting()
		// 	.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
		// 	.create();
		//
		// SmsPayResponse smsPayResponse = gson.fromJson(
		// 	Objects.requireNonNull(response.getBody()),
		// 	SmsPayResponse.class
		// );
		// //return을 결제 url 로 넘기기
		// careOrder.addPayment(smsPayResponse.toEntity(totalPrice));

		return careOrder.getId();
	}

	@Transactional(readOnly = true)
	public Page<CareOrderPageResponse> pageListCareOrder(CareOrderSearchRequest careOrderSearchRequest) {

		return careOrderRepositoryCustom.findPage(careOrderSearchRequest);
	}

	@Transactional(readOnly = true)
	public CareOrderDetailResponse getCareOrder(Long careOrderId) {
		CareOrder careOrder = this.getCareOrderById(careOrderId);
		return careOrder.toCareOrderDetailResponse();
	}

	@Transactional(readOnly = true)
	public GuardianCareOrderDetailResponse getGuardianCareOrder(Long careOrderId) {
		CareOrder careOrder = this.getCareOrderById(careOrderId);
		return careOrder.toGuardianCareOrderDetailResponse();
	}

	@Transactional(readOnly = true)
	public int getCareOrderCalculate(CareOrderCalculateRequest careOrderCalculateRequest) {
		int totalPrice = 0;
		long daysBetween = ChronoUnit.DAYS.between(careOrderCalculateRequest.getStartDateTime(),
			careOrderCalculateRequest.getEndDateTime());
		Settle recentSettle = settleService.getRecentSettle();
		for (int i = 0; i < daysBetween; i++) {
			totalPrice += calculatePaymentPriceOneDay(careOrderCalculateRequest, recentSettle.getGuardianFees());
		}
		return totalPrice;
	}

	@Transactional(readOnly = true)
	public CareOrder getCareOrderById(Long careOrderId) {
		return careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
	}

	public String extensionsCareOrder(Long careOrderId, CareOrderExtensionsRequest careOrderExtensionsRequest) {
		CareOrder careOrder = careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
		//간병 연장
		CareOrder extensionOrder = careOrder.extension(careOrderExtensionsRequest.getStartDateTime(),
			careOrderExtensionsRequest.getEndDateTime());

		int totalPrice = 0;
		long daysBetween = ChronoUnit.DAYS.between(careOrderExtensionsRequest.getStartDateTime(),
			careOrderExtensionsRequest.getEndDateTime());
		Settle recentSettle = settleService.getRecentSettle();

		for (int i = 0; i < daysBetween; i++) {
			// 지원자 o, packing
			// 종료 날짜(2024-04-17 10:00:00) - 시작 날짜(2024-04-12 10:00:00)
			// 시작 날짜부터 종료 날짜까지 5일 Packing 객체 생성
			LocalDateTime startDateTime = extensionOrder.getStartDateTime().plusDays(i);
			LocalDateTime endDateTime = startDateTime.plusDays(1);

			Packing build = Packing.builder()
				.careOrder(extensionOrder)
				.settle(recentSettle)
				.periodType(careOrderExtensionsRequest.getPeriodType())
				.partTime(careOrderExtensionsRequest.getPartTime())
				.amount(careOrderExtensionsRequest.getAmount())
				.startDateTime(startDateTime)
				.endDateTime(endDateTime)
				.build();
			extensionOrder.addPacking(build);

			CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
				.amount(careOrderExtensionsRequest.getAmount())
				.periodType(careOrderExtensionsRequest.getPeriodType())
				.partTime(careOrderExtensionsRequest.getPartTime())
				.build();
			totalPrice += calculatePaymentPriceOneDay(calculateRequest, recentSettle.getGuardianFees());
		}
		log.info(String.valueOf(totalPrice));

		SmsPayRequest smsPayRequest = SmsPayRequest.builder()
			.mid(payMid)
			.moid("CARE" + getRandomTimeKey())
			.goodsName(" " + daysBetween + "일") //상품명
			.amt(String.valueOf(totalPrice))
			.buyerName(careOrder.getMember().getUserNm())
			.buyerTel(careOrder.getMember().getTelNo())
			.build();
		// 결제pg요청 프로세스
		ResponseEntity<String> response = payApi.requestSmsPay(smsPayRequest);
		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
			.create();

		SmsPayResponse smsPayResponse = gson.fromJson(
			Objects.requireNonNull(response.getBody()),
			SmsPayResponse.class
		);
		//return을 결제 url 로 넘기기
		careOrder.addPayment(smsPayResponse.toEntity(totalPrice));
		return smsPayResponse.getOrderUrl();
	}

	public void cancelCareOrder(Long careOrderId, CareOrderTerminateRequest careOrderTerminateRequest) {
		CareOrder careOrder = careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
		careOrder.cancelOrder(careOrderTerminateRequest);
	}

	public void cancelCareOrderForGuardian(Long careOrderId, CareOrderTerminateRequest careOrderTerminateRequest) {
		CareOrder careOrder = careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
		if (careOrder.getOrderStatus().equals(OrderStatus.MATCHING_WAITING)) {
			careOrder.cancelOrder(careOrderTerminateRequest);
			return;
		}
		throw new BizException("매칭 대기 상태가 아닙니다.");
	}

	public Slice<UserCareOrderResponse> pageListCareOrderCursor(CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable) {
		return careOrderRepositoryCustom.findAllByCondition(careOrderSearchRequest, pageable);
	}
}
