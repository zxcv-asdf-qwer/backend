package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.KaKaoLoginResponse;
import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.api.social.kakao.KakaoUserApi;
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
@Qualifier("kakaoLogin")
public class KakaoLoginServiceImpl implements SocialLoginService {

  private final KakaoUserApi kakaoUserApi;

  @Override
  public MemberRegisterType getServiceName() {
    return MemberRegisterType.KAKAO;
  }

  @Override
  public SocialUserResponse tokenToSocialUserResponse(LoginRequest loginRequest) {
    ResponseEntity<?> response = kakaoUserApi.accessTokenToUserInfo(
        "Bearer " + loginRequest.getToken());

    log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
    log.info(response.toString());

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    KaKaoLoginResponse kaKaoLoginResponse = gson.fromJson(
        response.getBody().toString(),
        KaKaoLoginResponse.class
    );

    return SocialUserResponse.builder()
        .sub(kaKaoLoginResponse.getId())
        .memberRegisterType(getServiceName())
        .email(kaKaoLoginResponse.getKakao_account().getEmail())
        .build();
  }

  @Override
  public void revoke(LeaveRequest leaveRequest) {

  }

}
