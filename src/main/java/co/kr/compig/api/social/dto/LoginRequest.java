package co.kr.compig.api.social.dto;

import co.kr.compig.common.code.MemberRegisterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequest {

  private String token;
  private MemberRegisterType memberRegisterType;
}
