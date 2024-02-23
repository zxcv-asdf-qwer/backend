package co.kr.compig.api.board.dto;

import co.kr.compig.common.dto.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BoardSearchRequest extends PageableRequest {

  private String title;
  private String contents;
}