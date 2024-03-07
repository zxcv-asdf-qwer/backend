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

  private String token; //google//apple
  private String code; //kakao//naver
  private MemberRegisterType memberRegisterType;
}