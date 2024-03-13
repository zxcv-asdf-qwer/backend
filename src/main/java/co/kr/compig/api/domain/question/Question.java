package co.kr.compig.api.domain.question;

import co.kr.compig.api.question.dto.QuestionDetailResponse;
import co.kr.compig.api.question.dto.QuestionUpdateRequest;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.common.code.QuestionType;
import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import co.kr.compig.api.domain.answer.Answer;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

	@Column
	private QuestionType questionType;    // 질문유형

	@Column
	private String questionTitle;    // 질문 제목

	@Column
	private String questionContent;    // 질문 내용

	@Column
	@Builder.Default
	private IsYn isAnswer = IsYn.N;    // 답변 유무

	@Column
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
	public QuestionDetailResponse toQuestionDetailResponse() {
		return QuestionDetailResponse.builder()
			.questionId(this.id)
			.questionType(this.questionType)
			.questionTitle(this.questionTitle)
			.questionContent(this.questionContent)
			.isAnswer(this.isAnswer)
			.build();
	}

	public void update(QuestionUpdateRequest questionUpdateRequest) {
		this.questionTitle = questionUpdateRequest.getQuestionTitle();
		this.questionContent = questionUpdateRequest.getQuestionContent();
		this.questionType = questionUpdateRequest.getQuestionType();
	}

}
