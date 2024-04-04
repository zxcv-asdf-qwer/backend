package co.kr.compig.api.presentation.patient.request;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.gson.Gson;

import co.kr.compig.api.domain.code.DiseaseCode;
import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.LocationType;
import co.kr.compig.api.domain.code.ToiletType;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.patient.Patient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPatientCreateRequest {

	@NotBlank
	private String memberId; // 멤버 ID

	@NotBlank
	@Pattern(regexp = "^[\\sㄱ-ㅎ가-힣A-Za-z0-9_-]{2,100}$")
	@Length(min = 2, max = 50)
	private String patientNm; // 환자 이름

	@NotNull
	private GenderCode gender; // 환자 성별

	@NotNull
	@Min(0)
	private Integer patientAge; // 환자 나이

	@NotNull
	@Min(0)
	private Integer patientHeight; // 환자 키

	@NotNull
	@Min(0)
	private Integer patientWeight; // 환자 체중

	@NotNull
	private LocationType locationType; // 간병 장소 종류

	@NotBlank
	private String addressCd; // 간병 장소 우편 번호

	@NotBlank
	private String address1; // 간병 장소 주소

	@NotBlank
	private String address2; // 간병 장소 상세 주소

	@NotNull
	private List<DiseaseCode> diseaseNm; // 질환

	@NotNull
	private ToiletType selfToiletAvailability; // 대소변 해결 여부

	@NotNull
	private GenderCode genderPreference; // 선호 성별

	@NotNull
	private IsYn covid19Test; // 코로나 검사 필요 여부

	private String requestedTerm; // 요청 사항

	public Patient converterEntity(Member member) {
		return Patient.builder()
			.patientNm(this.patientNm)
			.gender(this.gender)
			.patientAge(this.patientAge)
			.patientHeight(this.patientHeight)
			.patientWeight(this.patientWeight)
			.diseaseNm(new Gson().toJson(this.diseaseNm))
			.selfToiletAvailability(this.selfToiletAvailability)
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
