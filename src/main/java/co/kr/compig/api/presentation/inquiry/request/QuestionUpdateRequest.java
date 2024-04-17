package co.kr.compig.api.presentation.inquiry.request;

import co.kr.compig.global.code.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequest {
	private QuestionType questionType; // 질문 유형
	private String questionTitle; // 질문 제목
	private String questionContent; // 질문 내용
}
