package co.kr.compig.api.board.dto;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.IsYn;
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
public class BoardCreate {
  @NotBlank
  @Length(min = 2, max = 100)
  private String title; // 게시글 제목

  @NotBlank
  @Length(min = 2)
  private String content; // 게시글 내용

  @NotBlank
  private BoardType boardType; // 게시글 유형

  private IsYn pinYn; // 상단 고정 여부
}
