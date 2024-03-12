package co.kr.compig.domain.answer;

import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.domain.question.Question;
import jakarta.persistence.Column;
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
	name = "answer_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "answer_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_seq_gen")
	@Column(name = "answer_id")
	private Long id;

	@Column
	private String answerTitle;    // 답변 제목

	@Column
	private String answerContent;    // 답변 내용

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private UseYn useYn = UseYn.Y;    // 사용 유무

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "f01_answer"))
	@OneToOne(fetch = FetchType.LAZY)
	private Question question = new Question();

	/* =================================================================
 	* Default columns
      ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	/* =================================================================
	 * Relation method
	   ================================================================= */

}