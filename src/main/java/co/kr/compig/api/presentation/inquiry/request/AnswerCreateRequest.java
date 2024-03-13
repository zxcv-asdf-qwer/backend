package co.kr.compig.api.presentation.inquiry.request;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import co.kr.compig.api.domain.inquiry.Answer;
import co.kr.compig.api.domain.inquiry.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateRequest {
	@NotBlank
	private String answerTitle; // 답변 제목

	@NotBlank
	private String answerContent; // 답변 내용

	public Answer converterEntity(Question question) {
		return Answer.builder()
			.answerTitle(this.answerTitle)
			.answerContent(this.answerContent)
			.question(question)
			.build();
	}
}
