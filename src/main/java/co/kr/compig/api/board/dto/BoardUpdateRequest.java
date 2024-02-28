package co.kr.compig.api.board.dto;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.ContentsType;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.UseYn;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequest {

  private BoardType boardType;
  private ContentsType contentsType;
  private String title;
  private String smallTitle;
  private String contents;
  private UseYn useYn;
  private IsYn pinYn;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime endDate;

}
