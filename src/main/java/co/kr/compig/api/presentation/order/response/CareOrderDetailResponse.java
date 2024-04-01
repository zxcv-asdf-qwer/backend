package co.kr.compig.api.presentation.order.response;

import java.time.LocalDateTime;
import java.util.Set;

import co.kr.compig.api.domain.code.CareOrderRegisterType;
import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.LocationType;
import co.kr.compig.api.domain.code.OrderStatusCode;
import co.kr.compig.api.domain.code.PeriodType;
import co.kr.compig.api.domain.code.ToiletType;
import co.kr.compig.api.presentation.apply.response.ApplyCareOrderResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CareOrderDetailResponse {

	private Long id; // 공고 ID
	private LocalDateTime startDateTime; // 시작 날짜
	private LocalDateTime endDateTime; // 종료 날짜
	private OrderStatusCode orderStatusCode; // 공고 상태
	private IsYn newStatus; // 신규 여부
	private PeriodType periodType; // 시간제, 기간제
	private CareOrderRegisterType careOrderRegisterType; // 등록 구분
	private String orderRequest; // 요청사항
	private String userNm; // 보호자명
	private String telNo; // 보호자번호
	private String patientNm;    // 환자 이름
	private GenderCode gender;    // 환자 성별
	private Integer patientAge; // 환자 나이
	private Integer patientHeight; // 환자 신장
	private Integer patientWeight; // 환자 몸무게
	private String diseaseNm; // 진단명
	private ToiletType selfToiletAvailability; // 대소변 해결 여부
	private IsYn moveAvailability; // 거동 가능 여부
	private IsYn mealAvailability; // 식사 가능 여부
	private GenderCode genderPreference; // 선호 성별
	private IsYn covid19Test; // 코로나 검사 필요 여부
	private String requestedTerm; // 요청 사항
	private LocationType locationType; // 간병 장소 종류
	private String addressCd; // 간병 장소 우편 번호
	private String address1; // 간병 장소 주소
	private String address2; // 간병 장소 상세 주소
	private Set<ApplyCareOrderResponse> applies; // 지원자

}