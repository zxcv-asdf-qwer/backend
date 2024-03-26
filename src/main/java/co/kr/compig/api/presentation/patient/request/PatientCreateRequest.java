package co.kr.compig.api.presentation.patient.request;

import org.hibernate.validator.constraints.Length;

import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.LocationType;
import co.kr.compig.api.domain.code.ToiletType;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.patient.Patient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreateRequest {

	@NotBlank
	@Length(min = 2, max = 50)
	private String patientNm; // 환자 이름

	@NotNull
	private GenderCode gender; // 환자 성별

	@NotNull
	private Integer patientAge; // 환자 나이

	@NotNull
	private Integer patientHeight; // 환자 신장

	@NotNull
	private Integer patientWeight; // 환자 몸무게

	@NotBlank
	private String diseaseNm; // 진단명

	@NotNull
	private ToiletType selfToiletAvailability; // 대소변 해결 여부

	@NotNull
	private IsYn moveAvailability; // 거동 가능 여부

	@NotNull
	private IsYn mealAvailability; // 식사 가능 여부

	@NotNull
	private GenderCode genderPreference; // 선호 성별

	@NotNull
	private IsYn covid19Test; // 코로나 검사 필요 여부

	private String requestedTerm; // 요청 사항

	@NotNull
	private LocationType locationType; // 간병 장소 종류

	@NotBlank
	private String addressCd; // 간병 장소 우편 번호

	@NotBlank
	private String address1; // 간병 장소 주소

	@NotBlank
	private String address2; // 간병 장소 상세 주소

	public Patient converterEntity(Member member) {
		return Patient.builder()
			.patientNm(this.patientNm)
			.gender(this.gender)
			.patientAge(this.patientAge)
			.patientHeight(this.patientHeight)
			.patientWeight(this.patientWeight)
			.diseaseNm(this.diseaseNm)
			.selfToiletAvailability(this.selfToiletAvailability)
			.moveAvailability(this.moveAvailability)
			.mealAvailability(this.mealAvailability)
			.genderPreference(this.genderPreference)
			.covid19Test(this.covid19Test)
			.requestedTerm(this.requestedTerm)
			.locationType(this.locationType)
			.addressCd(this.addressCd)
			.address1(this.address1)
			.address2(this.address2)
			.member(member)
			.build();
	}
}
