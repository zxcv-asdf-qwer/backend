package co.kr.compig.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class PageableRequest {

  private String keywordType; // 검색조건
  private String keyword;
}
