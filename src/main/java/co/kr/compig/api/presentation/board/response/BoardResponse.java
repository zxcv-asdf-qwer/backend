package co.kr.compig.api.presentation.board.response;

import co.kr.compig.global.code.BoardType;
import co.kr.compig.global.code.ContentsType;
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
public class BoardResponse extends BaseAudit {

	private Long boardId; // 게시글 id
	private String title; // 게시글 제목
	private BoardType boardType; // 게시글 유형
	private ContentsType contentsType; // 콘텐츠 유형

}
