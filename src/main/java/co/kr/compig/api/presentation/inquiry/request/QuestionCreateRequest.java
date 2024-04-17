package co.kr.compig.api.presentation.inquiry.request;

import org.hibernate.validator.constraints.Length;

import co.kr.compig.global.code.QuestionType;
import co.kr.compig.api.domain.inquiry.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateRequest {
	@NotNull
	private QuestionType questionType; // 질문 유형

	@NotBlank
	@Length(min = 2, max = 100)
	private String questionTitle; // 질문 제목

	@NotBlank
	@Length(min = 2)
	private String questionContents; // 질문 내용

	public Question converterEntity() {
		return Question.builder()
			.questionType(this.questionType)
			.questionTitle(this.questionTitle)
			.questionContent(this.questionContents)
			.build();
	}
}
