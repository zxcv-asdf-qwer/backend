package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.NaverLoginResponse;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.api.social.naver.NaverAuthApi;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.utils.GsonLocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("naverLogin")
public class NaverLoginServiceImpl implements SocialLoginService {

  private final NaverAuthApi naverAuthApi;

  @Override
  public MemberRegisterType getServiceName() {
    return MemberRegisterType.NAVER;
  }

  @Override
  public SocialUserResponse tokenToSocialUserResponse(LoginRequest loginRequest) {
    ResponseEntity<?> response = naverAuthApi.accessTokenToUserInfo(
        "Bearer " + loginRequest.getToken());

    log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
    log.info(response.toString());

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    NaverLoginResponse naverLoginResponse = gson.fromJson(
        response.getBody().toString(),
        NaverLoginResponse.class
    );

    return SocialUserResponse.builder()
        .sub(naverLoginResponse.getId())
        .memberRegisterType(getServiceName())
        .email(naverLoginResponse.getEmail())
        .build();
  }

  @Override
  public SocialUserResponse revoke(LoginRequest loginRequest) {
    return null;
  }

}
