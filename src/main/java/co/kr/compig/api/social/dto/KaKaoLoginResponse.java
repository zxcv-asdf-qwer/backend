package co.kr.compig.api.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoLoginResponse {

  private String sub;
  private String email;
  private String picture;
}