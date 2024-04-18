package co.kr.compig.api.domain.patient;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.NoMember;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.patient.request.OrderPatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.OrderPatientDetailResponse;
import co.kr.compig.global.code.DiseaseCode;
import co.kr.compig.global.code.GenderCode;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.LocationType;
import co.kr.compig.global.code.ToiletType;
import co.kr.compig.global.code.converter.DiseaseCodeListConverter;
import co.kr.compig.global.code.converter.ToiletTypeListConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import jakarta.persistence.OneToMany;
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
	name = "order_patient_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "order_patient_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class OrderPatient {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_patient_seq_gen")
	@Column(name = "order_patient_id")
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

	@Column(length = 10)
	private String addressCd; // 간병 장소 우편 번호

	@Column(length = 200)
	private String address1; // 간병 장소 주소

	@Column(length = 200)
	private String address2; // 간병 장소 상세 주소

	/* =================================================================
	* Domain mapping
	================================================================= */

	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk01_order_patient"))
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Member member; // Member id

	@JoinColumn(name = "no_member_id", foreignKey = @ForeignKey(name = "fk02_order_patient"))
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private NoMember noMember; // Member id

	@Builder.Default
	@OneToMany(mappedBy = "orderPatient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<CareOrder> careOrders = new HashSet<>();

	public OrderPatientDetailResponse toOrderPatientDetailResponse() {
		return OrderPatientDetailResponse.builder()
			.id(this.id)
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
			.build();
	}

	public void update(OrderPatientUpdateRequest orderPatientUpdateRequest) {
		this.name = orderPatientUpdateRequest.getName();
		this.gender = orderPatientUpdateRequest.getGender();
		this.birthDate = orderPatientUpdateRequest.getBirthDate();
		this.height = orderPatientUpdateRequest.getHeight();
		this.weight = orderPatientUpdateRequest.getWeight();
		this.diseaseNms = orderPatientUpdateRequest.getDiseaseNms();
		this.selfToiletAvailabilities = orderPatientUpdateRequest.getSelfToiletAvailabilities();
		this.moveAvailability = orderPatientUpdateRequest.getMoveAvailability();
		this.mealAvailability = orderPatientUpdateRequest.getMealAvailability();
		this.genderPreference = orderPatientUpdateRequest.getGenderPreference();
		this.covid19Test = orderPatientUpdateRequest.getCovid19Test();
		this.patientRequest = orderPatientUpdateRequest.getPatientRequest();
		this.addressCd = orderPatientUpdateRequest.getAddressCd();
		this.address1 = orderPatientUpdateRequest.getAddress1();
		this.address2 = orderPatientUpdateRequest.getAddress2();
	}
}
