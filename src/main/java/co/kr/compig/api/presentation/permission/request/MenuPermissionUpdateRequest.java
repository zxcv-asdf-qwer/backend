package co.kr.compig.api.presentation.permission.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuPermissionUpdateRequest {

	private String groupKey;
	private String memberId;
	private Long menuId;
}
