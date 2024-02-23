package co.kr.compig.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleCode {
  SYS_ADMIN("SYS_ADMIN"),
  INT_SYSTEM("INT_SYSTEM"),
  PARTNER("PARTNER");

  private String code;

  public boolean hasRole(String role) {
    return role.endsWith(this.code);
  }
}
