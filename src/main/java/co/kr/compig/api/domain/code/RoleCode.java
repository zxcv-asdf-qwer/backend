package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleCode {
	ALL_ACCESS("ALL_ACCESS"), //전부 다 됨
	BASIC_ACCESS("BASIC_ACCESS"), //사내 공지사항 확인 가능 권한, 기본적인 시스템 접근 권한
	READ_ONLY("READ_ONLY"); //읽기만 가능

	private String code;

	public boolean hasRole(String role) {
		return role.endsWith(this.code);
	}
}
