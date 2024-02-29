package co.kr.compig.service.social;


import co.kr.compig.api.social.dto.LoginResponse;
import co.kr.compig.api.social.dto.SocialAuthResponse;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.domain.member.Member;
import co.kr.compig.domain.member.MemberRepository;
import co.kr.compig.service.member.MemberService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialUserService {

  private final MemberService memberService;

  private final MemberRepository memberRepository;

  private final List<SocialLoginService> loginServices;

  public LoginResponse doSocialLogin(MemberRegisterType memberRegisterType,
      String authorizationCode) {
    SocialLoginService loginService = this.getLoginService(memberRegisterType);

    SocialAuthResponse socialAuthResponse = loginService.getAccessToken(authorizationCode);
    SocialUserResponse socialUserResponse = loginService.idTokenToResponse(
        socialAuthResponse.getId_token());
    //소셜로그인 성공 후
    //사용자 아이디 중복 조회
    Optional<Member> byEmail = memberRepository.findByEmail(socialUserResponse.getEmail());
    if (byEmail.isEmpty()) {
      memberService.basicCreate(socialUserResponse.getEmail());

    }

    //없으면 회원가입 실행
    //키클락 로그인 실행
    return LoginResponse.builder()
//        .email(socialUserResponse.getEmail())
        .build();
  }

  private SocialLoginService getLoginService(MemberRegisterType memberRegisterType) {
    for (SocialLoginService loginService : loginServices) {
      if (memberRegisterType.equals(loginService.getServiceName())) {
        log.info("login service name: {}", loginService.getServiceName());
        return loginService;
      }
    }
    return new LoginServiceImpl();
  }


}
