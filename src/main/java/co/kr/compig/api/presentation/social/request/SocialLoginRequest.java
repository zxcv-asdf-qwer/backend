package co.kr.compig.api.presentation.social.request;

import co.kr.compig.common.code.MemberRegisterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SocialLoginRequest {

	private String token;
	private MemberRegisterType memberRegisterType;
}
