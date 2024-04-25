package co.kr.compig.api.presentation.inquiry.response;

import java.time.LocalDateTime;

import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.dto.BaseAudit;
import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "질문 ID")
	private Long questionId; // 질문 ID
	@Schema(description = "질문 제목")
	private String questionTitle; // 질문 제목
	@Schema(description = "질문 내용")
	private String questionContent; // 질문 내용
	@Schema(description = "답변 유무")
	private IsYn isAnswer; // 답변 유무
	@Schema(description = "답변 내용")
	private String answerContent; // 답변 내용
	@Schema(description = "답변 업데이트 날짜")
	private LocalDateTime answerUpdateOn; // 답변 업데이트 날짜
}
