package co.kr.compig.api.board.dto;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.UseYn;
import lombok.Getter;

@Getter
public class BoardUpdate {
  private String title;
  private String content;
  private IsYn pinYn;
  private BoardType boardType;
  private UseYn useYn;
}
