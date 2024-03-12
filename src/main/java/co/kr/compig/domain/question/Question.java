package co.kr.compig.domain.question;

import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.QuestionType;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.code.converter.QuestionTypeConverter;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.domain.answer.Answer;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	name = "question_seq_gen",
	sequenceName = "question_seq",
	initialValue = 1,
	allocationSize = 1
)
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_seq_gen")
	@Column(name = "question_id")
	private Long id;

	@Column(length = 10)
	@Convert(converter = QuestionTypeConverter.class)
	private QuestionType questionType;    // 질문유형

	@Column
	private String questionTitle;    // 질문 제목

	@Column
	private String questionContent;    // 질문 내용

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private IsYn isAnswer = IsYn.N;    // 답변 유무

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private UseYn useYn = UseYn.Y; // 사용 유무
	/* =================================================================
	* Domain mapping
	================================================================= */
	@OneToOne(mappedBy = "question", fetch = FetchType.LAZY)
	private Answer answer;
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
