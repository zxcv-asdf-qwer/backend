package co.kr.compig.api.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoLoginResponse {
  //{
  //    "id": 324234,
  //    "connected_at": "2024-03-06T14:58:31Z",
  //    "kakao_account": {
  //        "has_email": true,
  //        "email_needs_agreement": false,
  //        "is_email_valid": true,
  //        "is_email_verified": true,
  //        "email": "sdfsdf@kakao.com"
  //    }
  //}

  private String id;
  private String connected_at;
  private KakaoAccount kakao_account;

  @Getter
  public static class KakaoAccount {

    private String has_email;
    private String email_needs_agreement;
    private String is_email_valid;
    private String is_email_verified;
    private String email;
  }

}