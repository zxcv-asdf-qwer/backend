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
    //access token, id token 받아오기
    SocialAuthResponse socialAuthResponse = loginService.getTokens(authorizationCode);
    //id token 검증
    SocialUserResponse socialUserResponse = loginService.idTokenToResponse(
        socialAuthResponse.getId_token());

    Optional<Member> optionalMember = memberRepository.findByUserId(socialUserResponse.getId());
    Member member = optionalMember.orElseGet(() -> {
      // 중복되지 않는 경우 새 회원 생성 후 반환
      String newMemberId = memberService.socialCreate(socialUserResponse.convertEntity());
      return memberRepository.findById(newMemberId)
          .orElseThrow(() -> new RuntimeException("회원 생성 후 조회 실패"));
    });

    // 공통 로직 처리: 키클락 로그인 실행
    return loginService.getKeycloakAccessToken(member.getEmail(),
        member.getEmail() + member.getMemberRegisterType());
    // 키클락 로그인 실행
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
