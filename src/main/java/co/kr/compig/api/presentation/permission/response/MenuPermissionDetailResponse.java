package co.kr.compig.api.presentation.permission.response;

import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MenuPermissionDetailResponse extends BaseAudit {
	private Long id; // 메뉴 권한 ID
	private String groupKey; // group key
	private String memberId; // 멤버 ID
	private String userNm; // 사용자 명
	private Long menuId; // 메뉴 ID
	private String menuNm; // 메뉴명
}
