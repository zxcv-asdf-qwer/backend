package co.kr.compig.api.domain.item;

import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.settle.Settle;
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
	name = "item_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "item_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
	@Column(name = "item_id")
	private Long id;

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@JoinColumn(name = "packing_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_item"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Packing packing = new Packing();

	@JoinColumn(name = "settle_id", nullable = false, foreignKey = @ForeignKey(name = "fk02_item"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Settle settle;
}
