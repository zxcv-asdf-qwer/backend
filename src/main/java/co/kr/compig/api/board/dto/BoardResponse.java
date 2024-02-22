package co.kr.compig.api.board.dto;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BoardResponse extends BaseAudit {


  private Long boardId;
  private String title; // 게시글 제목
  private String content; // 게시글 내용
  private BoardType boardType; // 게시글 유형

}
