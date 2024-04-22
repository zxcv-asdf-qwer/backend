package co.kr.compig.api.domain.memo;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.memo.response.MemoResponse;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
@SequenceGenerator(
	name = "memo_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "memo_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Memo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memo_seq_gen")
	@Column(name = "memo_id")
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String contents; //내용

	/* =================================================================
 	 * Domain mapping
     ================================================================= */
	@ManyToOne
	@JoinColumn(name = "care_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_memo"))
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private CareOrder careOrder;
	/* =================================================================
	 * Relation method
	   ================================================================= */

	public void setCareOrder(CareOrder careOrder) {
		this.careOrder = careOrder;
	}
	/* =================================================================
	 * Default columns
	 ================================================================= */

	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	public MemoResponse toMemoResponse() {
		MemoResponse memoResponse = MemoResponse.builder()
			.memoId(this.id)
			.contents(this.contents)
			.build();

		memoResponse.setCreatedAndUpdated(this.createdAndModified);
		return memoResponse;
	}

	/* =================================================================
	 * Business login
     ================================================================= */

}
