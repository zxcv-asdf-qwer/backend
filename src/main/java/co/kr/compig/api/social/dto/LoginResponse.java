package co.kr.compig.api.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginResponse {

  private String access_token;
  private String expires_in;
  private String refresh_expires_in;
  private String refresh_token;
  private String token_type;
  private String session_state;
  private String scope;

}
