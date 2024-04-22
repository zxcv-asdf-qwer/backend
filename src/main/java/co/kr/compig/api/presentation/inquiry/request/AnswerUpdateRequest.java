package co.kr.compig.api.presentation.inquiry.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerUpdateRequest {

	@NotBlank
	private String answerContent; // 답변 내용
}
