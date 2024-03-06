package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.KeycloakRequestAccessTokenDto;
import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.LoginResponse;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.api.social.keycloak.KeycloakAuthApi;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.keycloak.KeycloakProperties;
import co.kr.compig.common.utils.GsonLocalDateTimeAdapter;
import co.kr.compig.domain.member.Member;
import co.kr.compig.domain.member.MemberRepository;
import co.kr.compig.service.member.MemberService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialUserService {

  private final MemberService memberService;
  private final MemberRepository memberRepository;
  private final List<SocialLoginService> loginServices;
  private final KeycloakAuthApi keycloakAuthApi;
  private final KeycloakProperties keycloakProperties;
  private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

  private SocialLoginService getLoginService(MemberRegisterType memberRegisterType) {
    for (SocialLoginService loginService : loginServices) {
      if (memberRegisterType.equals(loginService.getServiceName())) {
        log.info("login service name: {}", loginService.getServiceName());
        return loginService;
      }
    }
    return new LoginServiceImpl();
  }

  public LoginResponse doSocialLogin(LoginRequest loginRequest) {
    SocialLoginService loginService = this.getLoginService(loginRequest.getMemberRegisterType());
    SocialUserResponse socialUserResponse = loginService.tokenToSocialUserResponse(
        loginRequest);

    Optional<Member> optionalMember = memberRepository.findByUserId(socialUserResponse.getSub());
    Member member = optionalMember.orElseGet(() -> {
      // 중복되지 않는 경우 새 회원 생성 후 반환
      String newMemberId = memberService.socialCreate(socialUserResponse.convertEntity());
      return memberRepository.findById(newMemberId)
          .orElseThrow(() -> new RuntimeException("회원 생성 후 조회 실패"));
    });

    // 공통 로직 처리: 키클락 로그인 실행
    return this.getKeycloakAccessToken(member.getEmail(),
        member.getEmail() + member.getMemberRegisterType());
    // 키클락 로그인 실행
  }

  private LoginResponse getKeycloakAccessToken(String userId, String userPw) {
    ResponseEntity<?> response = keycloakAuthApi.getAccessToken(
        KeycloakRequestAccessTokenDto.builder()
            .client_id(keycloakProperties.getClientId())
            .client_secret(keycloakProperties.getClientSecret())
            .username(userId)
            .password(userPw)
            .build()
    );
    log.info("keycloak user response");
    log.info(response.toString());

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    LoginResponse loginResponse = gson.fromJson(
        response.getBody().toString(),
        LoginResponse.class
    );

    loginResponse.setEmail(userId);
    JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(
        oAuth2ResourceServerProperties.getJwt().getIssuerUri());
    // LoginResponse에서 토큰 문자열 가져오기
    String jwtToken = loginResponse.getAccess_token();
    // 토큰 디코딩 및 파싱하여 Jwt 객체 얻기
    Jwt jwt = jwtDecoder.decode(jwtToken);
    loginResponse.setRoles(jwt.getClaim("groups"));

    return loginResponse;
  }

  public void doSocialRevoke(LeaveRequest leaveRequest) {
    SocialLoginService loginService = this.getLoginService(leaveRequest.getMemberRegisterType());
    loginService.revoke(leaveRequest);
    memberService.socialUserLeave(leaveRequest);
  }
}
