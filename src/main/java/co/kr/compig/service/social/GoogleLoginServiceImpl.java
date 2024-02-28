package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.GoogleLoginResponse;
import co.kr.compig.api.social.dto.GoogleRequestAccessTokenDto;
import co.kr.compig.api.social.dto.SocialAuthResponse;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("googleLogin")
public class GoogleLoginServiceImpl implements SocialLoginService {
  private final GoogleAuthApi googleAuthApi;
  private final GoogleUserApi googleUserApi;

  @Value("${social.client.google.rest-api-key}")
  private String googleAppKey;
  @Value("${social.client.google.secret-key}")
  private String googleAppSecret;
  @Value("${social.client.google.redirect-uri}")
  private String googleRedirectUri;
  @Value("${social.client.google.grant_type}")
  private String googleGrantType;

  @Override
  public MemberRegisterType getServiceName() {
    return MemberRegisterType.GOOGLE;
  }

  @Override
  public SocialAuthResponse getAccessToken(String authorizationCode) {
    ResponseEntity<?> response = googleAuthApi.getAccessToken(
        GoogleRequestAccessTokenDto.builder()
            .code(authorizationCode)
            .client_id(googleAppKey)
            .clientSecret(googleAppSecret)
            .redirect_uri(googleRedirectUri)
            .grant_type(googleGrantType)
            .build()
    );

    log.info("google auth info");
    log.info(response.toString());

    return new Gson()
        .fromJson(
            response.getBody().toString(),
            SocialAuthResponse.class
        );
  }

  @Override
  public SocialUserResponse getUserInfo(String accessToken) {
    ResponseEntity<?> response = googleAuthApi.getTokenInfo(accessToken);

    log.info("google user response");
    log.info(response.toString());

    String jsonString = response.getBody().toString();

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    GoogleLoginResponse googleLoginResponse = gson.fromJson(jsonString, GoogleLoginResponse.class);
    return SocialUserResponse.builder()
        .id(googleLoginResponse.getId())
        .email(googleLoginResponse.getEmail())
        .build();
  }

}
