package co.kr.compig.api.presentation.inquiry.request;

import co.kr.compig.api.domain.inquiry.Answer;
import co.kr.compig.api.domain.inquiry.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
	@Schema(description = "답변 내용")
	private String answerContent; // 답변 내용

	public Answer converterEntity(Question question) {
		return Answer.builder()
			.answerContent(this.answerContent)
			.question(question)
			.build();
	}
}
