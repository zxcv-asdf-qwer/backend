package co.kr.compig.common.dto.pagination;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PageableRequest {
  private Long cursorId; // 커서 id
  private String keywordType; // 검색조건
  private String keyword;
}
