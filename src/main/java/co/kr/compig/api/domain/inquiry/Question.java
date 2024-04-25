package co.kr.compig.api.domain.inquiry;

import co.kr.compig.api.presentation.inquiry.request.QuestionUpdateRequest;
import co.kr.compig.api.presentation.inquiry.response.QuestionDetailResponse;
import co.kr.compig.api.presentation.inquiry.response.QuestionResponse;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.QuestionType;
import co.kr.compig.global.code.UseYn;
import co.kr.compig.global.code.converter.QuestionTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
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

	public QuestionDetailResponse toQuestionDetailResponse() {
		QuestionDetailResponse build = QuestionDetailResponse.builder()
			.questionId(this.id)
			.questionTitle(this.questionTitle)
			.questionContent(this.questionContent)
			.isAnswer(this.isAnswer)
			.build();
		build.setCreatedAndUpdated(this.createdAndModified);
		return build;
	}

	public void update(QuestionUpdateRequest questionUpdateRequest) {
		this.questionTitle = questionUpdateRequest.getQuestionTitle();
		this.questionContent = questionUpdateRequest.getQuestionContent();
	}

	public void updateIsAnswer(IsYn isYn) {
		this.isAnswer = isYn;
	}

	public QuestionResponse toQuestionResponse() {
		QuestionResponse questionResponse = QuestionResponse.builder()
			.questionId(this.id)
			.questionTitle(this.questionTitle)
			.isAnswer(this.isAnswer)
			.build();

		questionResponse.setCreatedAndUpdated(this.createdAndModified);
		return questionResponse;
	}
}
