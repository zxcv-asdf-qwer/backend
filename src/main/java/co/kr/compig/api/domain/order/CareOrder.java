package co.kr.compig.api.domain.order;

import java.util.HashSet;
import java.util.Set;

import co.kr.compig.api.domain.apply.Apply;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.patient.OrderPatient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	name = "care_order_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "care_order_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class CareOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "care_order_seq_gen")
	@Column(name = "care_order_id")
	private Long id;


  /* =================================================================
   * Domain mapping
     ================================================================= */

	@Builder.Default
	@OneToMany(
		mappedBy = "careOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Apply> applys = new HashSet<>();

	@Builder.Default
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_care_order"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member = new Member(); // Member id

	@Builder.Default
	@JoinColumn(name = "order_patient_id", nullable = false, foreignKey = @ForeignKey(name = "fk02_care_order"))
	@OneToOne(fetch = FetchType.LAZY)
	private OrderPatient orderPatient = new OrderPatient();

}