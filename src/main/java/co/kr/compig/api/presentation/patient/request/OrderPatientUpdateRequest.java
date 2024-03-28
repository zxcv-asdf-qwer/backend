package co.kr.compig.api.presentation.patient.request;

import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.LocationType;
import co.kr.compig.api.domain.code.ToiletType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPatientUpdateRequest {
	private String patientNm; // 환자 이름
	private GenderCode gender; // 환자 성별
	private Integer patientAge; // 환자 나이
	private Integer patientHeight; // 환자 신장
	private Integer patientWeight; // 환자 몸무게
	private String diseaseNm; // 진단명
	private ToiletType selfToiletAvailability; // 대소변 해결 여부
	private IsYn moveAvailability; // 거동 가능 여부
	private IsYn mealAvailability; // 식사 가능 여부
	private GenderCode genderPreference; // 선호 성별
	private IsYn covid19Test; // 코로나 검사 필요 여부
	private String requestTerm; // 요청 사항
	private LocationType locationType; // 간병 장소 종류
	private String addressCd; // 간병 장소 우편 번호
	private String address1; // 간병 장소 주소
	private String address2; // 간병 장소 상세 주소
}
