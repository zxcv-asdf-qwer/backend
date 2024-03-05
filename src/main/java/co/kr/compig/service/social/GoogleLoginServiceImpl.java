package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.GoogleLoginResponse;
import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.api.social.google.GoogleAuthApi;
import co.kr.compig.api.social.google.GoogleUserApi;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;


@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("googleLogin")
public class GoogleLoginServiceImpl implements SocialLoginService {

  private final GoogleAuthApi googleAuthApi;
  private final GoogleUserApi googleUserApi;

  @Override
  public MemberRegisterType getServiceName() {
    return MemberRegisterType.GOOGLE;
  }

  @Override
  public SocialUserResponse tokenToSocialUserResponse(LoginRequest loginRequest) {
    try {
      ResponseEntity<?> response = googleAuthApi.getAccessTokenToTokenInfo(
          loginRequest.getToken());

      log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
      log.info(response.toString());

      Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
          .create();

      GoogleLoginResponse googleLoginResponse = gson.fromJson(response.getBody().toString(),
          GoogleLoginResponse.class);

      return SocialUserResponse.builder()
          .sub(googleLoginResponse.getSub())
          .memberRegisterType(getServiceName())
          .email(googleLoginResponse.getEmail())
          .build();
    } catch (HttpServerErrorException e) {
      log.error("Google getUserInfo HttpServerErrorException - Status : {}, Message : {}",
          e.getStatusCode(), e.getMessage());
    } catch (UnknownHttpStatusCodeException e) {
      log.error("Google getUserInfo UnknownHttpStatusCodeException - Status : {}, Message : {}",
          e.getStatusCode(), e.getMessage());
    }

    return null;
  }

  @Override
  public void revoke(LeaveRequest leaveRequest) {
    try {
      ResponseEntity<String> response = googleUserApi.revokeAccessToken(
          leaveRequest.getToken());

      log.info(getServiceName().getCode() + " revoke");
      log.info(response.toString());

    } catch (HttpServerErrorException e) {
      log.error("Google revokeAccessToken HttpServerErrorException - Status : {}, Message : {}",
          e.getStatusCode(), e.getMessage());
    } catch (UnknownHttpStatusCodeException e) {
      log.error("Google revokeAccessToken UnknownHttpStatusCodeException - Status : {}, Message : {}",
          e.getStatusCode(), e.getMessage());
    }
  }

}
