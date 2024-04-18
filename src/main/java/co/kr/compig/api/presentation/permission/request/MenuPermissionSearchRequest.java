package co.kr.compig.api.presentation.permission.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class MenuPermissionSearchRequest extends PageableRequest {
	private String userNm; // 사용자명
	private String menuNm; // 메뉴명
}
