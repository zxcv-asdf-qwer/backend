package co.kr.compig.api.domain.patient;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.presentation.patient.request.PatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.PatientDetailResponse;
import co.kr.compig.api.presentation.patient.response.PatientResponse;
import co.kr.compig.global.code.DiseaseCode;
import co.kr.compig.global.code.GenderCode;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.LocationType;
import co.kr.compig.global.code.ToiletType;
import co.kr.compig.global.code.converter.DiseaseCodeListConverter;
import co.kr.compig.global.code.converter.ToiletTypeListConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
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
	name = "patient_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "patient_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_seq_gen")
	@Column(name = "patient_id")
	private Long id;

	@Column(length = 50)
	private String name;    // 환자 이름

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private GenderCode gender;    // 환자 성별

	@Column
	private LocalDate birthDate; // 환자 나이

	@Column
	private Integer height; // 환자 신장

	@Column
	private Integer weight; // 환자 몸무게

	@Column(columnDefinition = "json")
	@JdbcTypeCode(SqlTypes.JSON)
	@Convert(converter = DiseaseCodeListConverter.class)
	private List<DiseaseCode> diseaseNms; // 진단명 리스트

	@Column(columnDefinition = "json")
	@JdbcTypeCode(SqlTypes.JSON)
	@Convert(converter = ToiletTypeListConverter.class)
	private List<ToiletType> selfToiletAvailabilities; // 대소변 해결 여부

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private IsYn moveAvailability; // 거동 가능 여부

	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private IsYn mealAvailability; // 식사 가능 여부

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private GenderCode genderPreference; // 선호 성별

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private IsYn covid19Test; // 코로나 검사 필요 여부

	@Column
	private String patientRequest; // 요청 사항

	@Column
	@Enumerated(EnumType.STRING)
	private LocationType locationType; // 간병 장소 종류

	@Column(length = 200)
	private String address1; // 간병 장소 주소

	@Column(length = 200)
	private String address2; // 간병 장소 상세 주소

	@Column(length = 100)
	private String hospitalName; // 병원명

	/* =================================================================
	* Domain mapping
	================================================================= */

	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk01_patient"))
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Member member; // Member id

	/* =================================================================
	* Relation method
	================================================================= */

	/* =================================================================
   * Default columns
   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	public PatientDetailResponse toPatientDetailResponse() {
		return PatientDetailResponse.builder()
			.id(this.id)
			.name(this.name)
			.gender(this.gender)
			.birthDate(this.birthDate)
			.height(this.height)
			.weight(this.weight)
			.diseaseNms(this.diseaseNms)
			.selfToiletAvailabilities(this.selfToiletAvailabilities)
			.genderPreference(this.genderPreference)
			.covid19Test(this.covid19Test)
			.patientRequest(this.patientRequest)
			.locationType(this.locationType)
			.address1(this.address1)
			.address2(this.address2)
			.hospitalName(this.hospitalName)
			.memberId(this.member.getId())
			.build();
	}

	public void update(PatientUpdateRequest patientUpdateRequest) {
		this.name = patientUpdateRequest.getName();
		this.gender = patientUpdateRequest.getGender();
		this.birthDate = patientUpdateRequest.getBirthDate();
		this.height = patientUpdateRequest.getHeight();
		this.weight = patientUpdateRequest.getWeight();
		this.diseaseNms = patientUpdateRequest.getDiseaseNms();
		this.selfToiletAvailabilities = patientUpdateRequest.getSelfToiletAvailabilities();
		this.genderPreference = patientUpdateRequest.getGenderPreference();
		this.covid19Test = patientUpdateRequest.getCovid19Test();
		this.patientRequest = patientUpdateRequest.getPatientRequest();
		this.address1 = patientUpdateRequest.getAddress1();
		this.address2 = patientUpdateRequest.getAddress2();
		this.hospitalName = patientUpdateRequest.getHospitalName();
	}

	public PatientResponse toPatientResponse() {
		return PatientResponse.builder()
			.id(this.id)
			.name(this.name)
			.build();
	}

	public OrderPatient toOrderPatient() {
		return OrderPatient.builder()
			.name(this.name) // 환자 이름
			.gender(this.gender) // 환자 성별
			.birthDate(this.birthDate) // 환자 나이 (실제로는 생년월일이지만 나이를 계산할 수 있음)
			.height(this.height) // 환자 신장
			.weight(this.weight) // 환자 몸무게
			.diseaseNms(this.diseaseNms) // 진단명 리스트
			.selfToiletAvailabilities(this.selfToiletAvailabilities) // 대소변 해결 여부
			.moveAvailability(this.moveAvailability) // 거동 가능 여부
			.mealAvailability(this.mealAvailability) // 식사 가능 여부
			.genderPreference(this.genderPreference) // 선호 성별
			.covid19Test(this.covid19Test) // 코로나 검사 필요 여부
			.patientRequest(this.patientRequest) // 요청 사항
			.locationType(this.locationType) // 간병 장소 종류
			.address1(this.address1) // 간병 장소 주소
			.address2(this.address2) // 간병 장소 상세 주소
			.hospitalName(this.hospitalName) //병원명
			.member(this.member)
			.build();
	}
}
