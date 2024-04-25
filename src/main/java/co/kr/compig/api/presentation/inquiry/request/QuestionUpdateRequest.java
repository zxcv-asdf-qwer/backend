package co.kr.compig.api.presentation.inquiry.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequest {

	@Schema(description = "질문 제목")
	private String questionTitle; // 질문 제목
	@Schema(description = "질문 내용")
	private String questionContent; // 질문 내용
}
