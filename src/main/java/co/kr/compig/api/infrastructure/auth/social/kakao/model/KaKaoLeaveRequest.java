package co.kr.compig.api.infrastructure.auth.social.kakao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KaKaoLeaveRequest {

	@Builder.Default
	private String target_id_type = "user_id";
	private String target_id;
}
