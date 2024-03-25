package co.kr.compig.global.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@SuperBuilder(toBuilder = true)
public class PageableRequest {
	private String cursorId; // 커서 id
	private String keywordType; // 검색조건
	private String keyword;
}
