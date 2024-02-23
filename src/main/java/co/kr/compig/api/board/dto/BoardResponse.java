package co.kr.compig.api.board.dto;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.dto.BaseAudit;
import co.kr.compig.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BoardResponse extends BaseAudit {


  private Long boardId; // 게시글 id
  private String title; // 게시글 제목
  private String contents; // 게시글 내용
  private BoardType boardType; // 게시글 유형
  private Integer viewCount; // 조회수

  public BoardResponse(Board board){
    this.boardId = board.getId();
    this.title = board.getTitle();
    this.contents = board.getContents();
    this.boardType = board.getBoardType();
    this.viewCount = board.getViewCount();
  }
}
