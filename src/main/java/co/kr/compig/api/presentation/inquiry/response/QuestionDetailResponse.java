package co.kr.compig.api.presentation.inquiry.response;

import java.time.LocalDateTime;

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
public class QuestionDetailResponse extends BaseAudit {

	private Long questionId; // 질문 ID
	private QuestionType questionType; // 질문 타입
	private String questionTitle; // 질문 제목
	private String questionContent; // 질문 내용
	private IsYn isAnswer; // 답변 유무
	private String answerTitle; // 답변 제목
	private String answerContent; // 답변 내용
	private LocalDateTime answerUpdateOn; // 답변 업데이트 날짜
}
