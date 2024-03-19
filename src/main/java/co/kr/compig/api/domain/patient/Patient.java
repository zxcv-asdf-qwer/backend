package co.kr.compig.api.domain.patient;

import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.ToiletType;
import co.kr.compig.api.domain.code.converter.ToiletTypeConverter;
import co.kr.compig.api.domain.member.Member;
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
	private String patientNm;    // 환자 이름

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private GenderCode gender;    // 환자 성별

	@Column
	private Integer patientAge; // 환자 나이

	@Column
	private Integer patientHeight; // 환자 신장

	@Column
	private Integer patientWeight; // 환자 몸무게

	@Column(length = 50)
	private String diseaseNm; // 질환명

	@Column(length = 10)
	@Convert(converter = ToiletTypeConverter.class)
	private ToiletType selfToiletAvailability; // 대소변 해결 여부

	@Column(length = 1)
	private IsYn moveAvailability; // 거동 가능 여부

	@Column(length = 10)
	private IsYn mealAvailability; // 식사 가능 여부

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private GenderCode genderPreference; // 선호 성별

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private IsYn covid19Test; // 코로나 검사 필요 여부

	@Column
	private String requestedTerm; // 요청 사항

	@Column
	private String locationType; // 간병 장소 종류

	@Column(length = 10)
	private String addressCd; // 간병 장소 우편 번호

	@Column(length = 200)
	private String address1; // 간병 장소 주소

	@Column(length = 200)
	private String address2; // 간병 장소 상세 주소

	/* =================================================================
	* Domain mapping
	================================================================= */
	@Builder.Default
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_patient"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member = new Member(); // Member id
	/* =================================================================
	* Relation method
	================================================================= */

	/* =================================================================
   * Default columns
   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();
}
