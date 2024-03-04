package co.kr.compig.api.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequest {

  private String idToken; //google//apple
  private String accessToken; //kakao//naver
}
