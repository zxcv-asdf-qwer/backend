package co.kr.compig.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleCode {
  SYS_ADMIN("SYS_ADMIN"), // 시스템사용자 (System)
  SYS_USER("SYS_USER"), // 내부사용자 (Internal)
  GUARDIAN("GUARDIAN"), // 보호자
  PARTNER("PARTNER");  // 간병인

  private String code;

  public boolean hasRole(String role) {
    return role.endsWith(this.code);
  }
}
