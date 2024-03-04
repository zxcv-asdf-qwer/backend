package co.kr.compig.api.social.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KaKaoRequestLeaveDto {

  @Builder.Default
  private String target_id_type = "user_id";
  private String target_id;
}
