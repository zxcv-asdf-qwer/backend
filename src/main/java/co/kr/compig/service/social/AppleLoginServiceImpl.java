package co.kr.compig.service.social;

import co.kr.compig.api.social.apple.AppleAuthApi;
import co.kr.compig.api.social.dto.AppleIdTokenPayload;
import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.utils.GsonLocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
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
    String jsonObject = "";
    try {
      AppleIdTokenPayload response = appleGetMemberInfoService.getTokens(loginRequest.getCode());
      jsonObject = response.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
    log.info(jsonObject);

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    AppleIdTokenPayload appleLoginResponse = gson.fromJson(
        jsonObject,
        AppleIdTokenPayload.class
    );

    return SocialUserResponse.builder()
        .sub(appleLoginResponse.getSub())
        .memberRegisterType(getServiceName())
        .email(appleLoginResponse.getEmail())
        .build();
  }

  @Override
  public void revoke(LeaveRequest leaveRequest) {
  }


}
