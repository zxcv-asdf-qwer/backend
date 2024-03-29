package co.kr.compig.api.presentation.permission.request;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.menu.Menu;
import co.kr.compig.api.domain.permission.MenuPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuPermissionCreateRequest {
	private String groupKey;
	private String memberId;
	private Long menuId;

	public MenuPermission converterEntity(Member member, Menu menu) {
		return MenuPermission.builder()
			.groupKey(this.groupKey)
			.member(member)
			.menu(menu)
			.build();
	}
}
