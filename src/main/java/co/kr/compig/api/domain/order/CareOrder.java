package co.kr.compig.api.domain.order;

import java.util.HashSet;
import java.util.Set;

import co.kr.compig.api.domain.apply.Apply;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

}
