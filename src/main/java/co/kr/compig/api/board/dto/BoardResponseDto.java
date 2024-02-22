package co.kr.compig.api.board.dto;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.domain.board.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class BoardResponseDto {
  private String title; // 게시글 제목
  private String content; // 게시글 내용
  private BoardType boardType; // 게시글 유형

  public BoardResponseDto(Board board){
    this.title = board.getTitle();
    this.content = board.getContent();
    this.boardType = board.getBoardType();
  }
}
