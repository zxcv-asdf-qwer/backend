package co.kr.compig.api.presentation.permission.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class MenuPermissionSearchRequest extends PageableRequest {
	private String userNm; // 사용자명
	private String menuNm; // 메뉴명
}
