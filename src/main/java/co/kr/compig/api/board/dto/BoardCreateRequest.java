package co.kr.compig.api.board.dto;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.ContentsType;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.domain.board.Board;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateRequest {

  @NotBlank
  @Length(min = 2, max = 100)
  private String title; // 게시글 제목

  @NotBlank
  @Length(min = 2)
  private String contents; // 게시글 내용

  @NotNull
  private BoardType boardType; // 게시글 유형

  @NotNull
  private ContentsType contentsType;


  private IsYn pinYn; // 상단 고정 여부

  public Board converterEntity() {
    return Board.builder()
        .title(this.title)
        .contents(this.contents)
        .boardType(this.boardType)
        .contentsType(this.contentsType)
        .pinYn(this.pinYn)
        .build();

  }

}
