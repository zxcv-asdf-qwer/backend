package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.GoogleLoginResponse;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.utils.GsonLocalDateTimeAdapter;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("googleLogin")
public class GoogleLoginServiceImpl implements SocialLoginService {

  private GoogleIdTokenVerifier googleIdTokenVerifier;

  @Value("${social.client.google.rest-api-key}")
  private String googleAppKey;

  @PostConstruct
  public void initialize() {
    log.info("### Initializing googleIdTokenVerifier");
    googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
        new GsonFactory())
        .setAudience(Collections.singletonList(googleAppKey))
        .build();
  }

  @Override
  public MemberRegisterType getServiceName() {
    return MemberRegisterType.GOOGLE;
  }

  @Override
  public SocialUserResponse tokenToSocialUserResponse(LoginRequest loginRequest) {
    String jsonString = "";
    try {
      //토큰 검증
      GoogleIdToken verifiedIdToken = googleIdTokenVerifier.verify(loginRequest.getToken());
      GoogleIdToken.Payload payload = verifiedIdToken.getPayload();
      jsonString = payload.toString();
    } catch (GeneralSecurityException | IOException e) {
      log.warn(e.getLocalizedMessage());
    }

    log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
    log.info(jsonString.toString());

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    GoogleLoginResponse googleLoginResponse = gson.fromJson(jsonString, GoogleLoginResponse.class);

    return SocialUserResponse.builder()
        .sub(googleLoginResponse.getSub())
        .memberRegisterType(getServiceName())
        .email(googleLoginResponse.getEmail())
        .build();
  }

  @Override
  public SocialUserResponse revoke(LoginRequest loginRequest) {
    return null;
  }

}
