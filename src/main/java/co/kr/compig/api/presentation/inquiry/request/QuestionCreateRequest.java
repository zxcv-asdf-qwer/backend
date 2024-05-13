package co.kr.compig.api.presentation.inquiry.request;

import org.hibernate.validator.constraints.Length;

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
public class QuestionCreateRequest {

	@NotBlank
	@Length(max = 30)
	@Schema(description = "질문 제목")
	private String questionTitle; // 질문 제목

	@NotBlank
	@Length(max = 300)
	@Schema(description = "질문 내용")
	private String questionContents; // 질문 내용

	public Question converterEntity() {
		return Question.builder()
			.questionTitle(this.questionTitle)
			.questionContent(this.questionContents)
			.build();
	}
}
