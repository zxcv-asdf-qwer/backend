package co.kr.compig.api.domain.member;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.patient.Patient;
import co.kr.compig.api.presentation.member.response.NoMemberResponse;
import co.kr.compig.api.presentation.member.response.UserMainSearchResponse;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
	uniqueConstraints = {
		@UniqueConstraint(name = "uk01_no_member", columnNames = {"userNm", "telNo"})
	}
)
@SequenceGenerator(
	name = "no_member_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "no_member_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class NoMember {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "no_member_seq_gen")
	@Column(name = "no_member_id")
	private Long id;

	@Column(length = 100)
	private String userNm; // 사용자 명

	@Column(length = 100)
	private String telNo; // 전화번호


	/* =================================================================
	 * Domain mapping
	   ================================================================= */

	@Builder.Default
	@OneToMany(mappedBy = "noMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Patient> patients = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "noMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<OrderPatient> orderPatients = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "noMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<CareOrder> careOrders = new HashSet<>();

	/* =================================================================
	 * Relation method
	   ================================================================= */

	/* =================================================================
	 * Default columns
	   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

  /* =================================================================
   * Business
     ================================================================= */

	public NoMemberResponse toNoMemberResponse() {
		return NoMemberResponse.builder()
			.noMemberId(this.id)
			.userNm(this.userNm)
			.telNo(this.telNo)
			.build();
	}
	public UserMainSearchResponse toUserMainSearchResponse() {
		return UserMainSearchResponse.builder()
			.memberId(this.id.toString())
			.userNm(this.userNm)
			.telNo(this.telNo)
			.build();
	}

}
