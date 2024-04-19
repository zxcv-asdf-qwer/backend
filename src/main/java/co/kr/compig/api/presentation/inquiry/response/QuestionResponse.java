package co.kr.compig.api.presentation.inquiry.response;

import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.QuestionType;
import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class QuestionResponse extends BaseAudit {
	private Long id; // 질문 ID
	private QuestionType questionType; // 질문 타입
	private String questionTitle; // 질문 제목
	private IsYn isAnswer; // 답변 유무
}
