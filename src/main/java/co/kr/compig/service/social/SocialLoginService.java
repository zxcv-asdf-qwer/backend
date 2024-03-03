package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.LoginResponse;
import co.kr.compig.api.social.dto.SocialAuthResponse;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {

  MemberRegisterType getServiceName();

  //access token, id token 받아오기
  SocialAuthResponse getTokens(String authorizationCode);

  //id token 검증
  SocialUserResponse idTokenToResponse(String authorizationCode);

  //keycloak Login 후 access token 받아오기
  LoginResponse getKeycloakAccessToken(String authorizationCode, String userPw);
}
