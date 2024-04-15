package co.kr.compig.api.presentation.order.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import co.kr.compig.api.domain.code.CareOrderProcessType;
import co.kr.compig.api.domain.code.DiseaseCode;
import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.LocationType;
import co.kr.compig.api.domain.code.OrderStatus;
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
	private OrderStatus orderStatus; // 공고 상태
	private IsYn publishYn; // 게시 여부
	private CareOrderProcessType careOrderProcessType; // 매칭 구분
	private String orderRequest; // 요청사항
	private String userNm; // 보호자명
	private String telNo; // 보호자번호
	private String name;    // 환자 이름
	private GenderCode gender;    // 환자 성별
	private LocalDate birthDate; // 환자 나이
	private Integer height; // 환자 신장
	private Integer weight; // 환자 몸무게
	private List<DiseaseCode> diseaseNms; // 진단명
	private List<ToiletType> selfToiletAvailabilities; // 대소변 해결 여부
	private IsYn moveAvailability; // 거동 가능 여부
	private IsYn mealAvailability; // 식사 가능 여부
	private GenderCode genderPreference; // 선호 성별
	private IsYn covid19Test; // 코로나 검사 필요 여부
	private String patientRequest; // 요청 사항
	private LocationType locationType; // 간병 장소 종류
	private String addressCd; // 간병 장소 우편 번호
	private String address1; // 간병 장소 주소
	private String address2; // 간병 장소 상세 주소
	private Set<ApplyCareOrderResponse> applies; // 지원자

}
