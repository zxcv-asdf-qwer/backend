package co.kr.compig.api.board.dto;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.ContentsType;
import co.kr.compig.common.dto.BaseAudit;
import co.kr.compig.domain.board.Board;
import java.time.LocalDateTime;
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
  private String smallTitle; // 게시글 소제목
  private String contents; // 게시글 내용
  private BoardType boardType; // 게시글 유형
  private ContentsType contentsType; // 콘텐츠 유형
  private Integer viewCount; // 조회수
  private String createdBy; // 작성자
  private LocalDateTime startDate; // 시작 날짜
  private LocalDateTime endDate; // 종료 날짜

  public BoardResponse(Board board) {
    this.boardId = board.getId();
    this.title = board.getTitle();
    this.smallTitle = board.getSmallTitle();
    this.contents = board.getContents();
    this.boardType = board.getBoardType();
    this.contentsType = board.getContentsType();
    this.viewCount = board.getViewCount();
    this.createdBy = board.getCreatedAndModified().getCreatedBy();
    this.startDate = board.getStartDate();
    this.endDate = board.getEndDate();
  }
}
