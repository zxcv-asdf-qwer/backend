package co.kr.compig.api.domain.patient;

import co.kr.compig.api.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Builder.Default
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_patient"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member = new Member(); // Member id
}
