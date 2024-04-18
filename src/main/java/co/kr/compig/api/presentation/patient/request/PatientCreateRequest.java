package co.kr.compig.api.presentation.patient.request;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.patient.Patient;
import co.kr.compig.global.code.DiseaseCode;
import co.kr.compig.global.code.GenderCode;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.LocationType;
import co.kr.compig.global.code.ToiletType;
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
	private String name; // 환자 이름

	@NotNull
	private GenderCode gender; // 환자 성별

	@NotNull
	private LocalDate birthDate; // 환자 나이

	@NotNull
	private Integer height; // 환자 신장

	@NotNull
	private Integer weight; // 환자 몸무게

	@NotBlank
	private List<DiseaseCode> diseaseNms; // 진단명

	@NotNull
	private List<ToiletType> selfToiletAvailabilities; // 대소변 해결 여부

	@NotNull
	private IsYn moveAvailability; // 거동 가능 여부

	@NotNull
	private IsYn mealAvailability; // 식사 가능 여부

	@NotNull
	private GenderCode genderPreference; // 선호 성별

	@NotNull
	private IsYn covid19Test; // 코로나 검사 필요 여부

	private String patientRequest; // 요청 사항

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
			.name(this.name)
			.gender(this.gender)
			.birthDate(this.birthDate)
			.height(this.height)
			.weight(this.weight)
			.diseaseNms(this.diseaseNms)
			.selfToiletAvailabilities(this.selfToiletAvailabilities)
			.moveAvailability(this.moveAvailability)
			.mealAvailability(this.mealAvailability)
			.genderPreference(this.genderPreference)
			.covid19Test(this.covid19Test)
			.patientRequest(this.patientRequest)
			.locationType(this.locationType)
			.addressCd(this.addressCd)
			.address1(this.address1)
			.address2(this.address2)
			.member(member)
			.build();
	}
}
