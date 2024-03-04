package co.kr.compig.service.social;

import co.kr.compig.api.social.apple.AppleAuthApi;
import co.kr.compig.api.social.dto.AppleLoginResponse;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.utils.GsonLocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("appleLogin")
public class AppleLoginServiceImpl implements SocialLoginService {

  private final AppleAuthApi appleAuthApi;

  @Override
  public MemberRegisterType getServiceName() {
    return MemberRegisterType.KAKAO;
  }

  @Override
  public SocialUserResponse tokenToSocialUserResponse(LoginRequest loginRequest) {
    JSONObject jsonObject = new JSONObject();
    try {
      SignedJWT signedJWT = SignedJWT.parse(loginRequest.getIdToken());
      JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
      jsonObject = new JSONObject(jwtClaimsSet.toJSONObject());
    } catch (ParseException e) {
      log.error(getServiceName().getCode() + " tokenToSocialUserResponse\n", e);
    }

    log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
    log.info(jsonObject.toString());

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    AppleLoginResponse appleLoginResponse = gson.fromJson(
        jsonObject.toString(),
        AppleLoginResponse.class
    );

    return SocialUserResponse.builder()
        .sub(appleLoginResponse.getSub())
        .memberRegisterType(getServiceName())
        .email(appleLoginResponse.getEmail())
        .build();
  }

  @Override
  public SocialUserResponse revoke(LoginRequest loginRequest) {
    return null;
  }

}
