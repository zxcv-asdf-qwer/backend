package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.AppleIdTokenPayload;
import co.kr.compig.api.social.dto.AppleSocialTokenResponse;
import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.utils.GsonLocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("appleLogin")
public class AppleLoginServiceImpl implements SocialLoginService {

  private final AppleGetMemberInfoService appleGetMemberInfoService;

  @Override
  public MemberRegisterType getServiceName() {
    return MemberRegisterType.APPLE;
  }

  @Override
  public SocialUserResponse tokenToSocialUserResponse(LoginRequest loginRequest) {
    //TODO idToken 유효성검사
    AppleIdTokenPayload appleLoginResponse = appleGetMemberInfoService.decodePayload(loginRequest.getToken(),
        AppleIdTokenPayload.class);

    log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
    log.info(appleLoginResponse.toString());

    return SocialUserResponse.builder()
        .sub(appleLoginResponse.getSub())
        .memberRegisterType(getServiceName())
        .email(appleLoginResponse.getEmail())
        .build();
  }

  @Override
  public void revoke(LeaveRequest leaveRequest) {
    try {
      AppleSocialTokenResponse tokens = appleGetMemberInfoService.getTokens(leaveRequest.getCode());
      appleGetMemberInfoService.revokeTokens(tokens);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
  }

}
