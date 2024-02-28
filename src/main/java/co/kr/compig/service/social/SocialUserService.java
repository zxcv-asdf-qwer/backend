package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.LoginResponse;
import co.kr.compig.api.social.dto.SocialAuthResponse;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialUserService {
    private final List<SocialLoginService> loginServices;
    public LoginResponse doSocialLogin(MemberRegisterType memberRegisterType, String authorizationCode) {
        SocialLoginService loginService = this.getLoginService(memberRegisterType);

        SocialAuthResponse socialAuthResponse = loginService.getAccessToken(authorizationCode);

        SocialUserResponse socialUserResponse = loginService.getUserInfo(socialAuthResponse.getAccess_token());
        log.info("socialUserResponse {} ", socialUserResponse.toString());

        return LoginResponse.builder()
                .id(1L)
                .build();
    }

    private SocialLoginService getLoginService(MemberRegisterType memberRegisterType){
        for (SocialLoginService loginService: loginServices) {
            if (memberRegisterType.equals(loginService.getServiceName())) {
                log.info("login service name: {}", loginService.getServiceName());
                return loginService;
            }
        }
        return new LoginServiceImpl();
    }


}
