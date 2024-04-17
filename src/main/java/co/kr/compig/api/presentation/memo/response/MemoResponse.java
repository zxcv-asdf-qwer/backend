package co.kr.compig.api.presentation.memo.response;

import java.time.LocalDateTime;

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
public class MemoResponse extends BaseAudit {

	private Long memoId; // 메모 Id
	private String contents; // 메모 내용
	private String createdBy; // 작성자
	private LocalDateTime createdOn; // 작성일
}