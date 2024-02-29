package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.SocialAuthResponse;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {

  MemberRegisterType getServiceName();

  SocialAuthResponse getAccessToken(String authorizationCode);

  SocialUserResponse idTokenToResponse(String authorizationCode);

  SocialUserResponse getUserInfo(String accessToken);
}
