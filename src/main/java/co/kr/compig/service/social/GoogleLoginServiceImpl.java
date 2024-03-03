package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.GoogleLoginResponse;
import co.kr.compig.api.social.dto.GoogleRequestAccessTokenDto;
import co.kr.compig.api.social.dto.KeycloakRequestAccessTokenDto;
import co.kr.compig.api.social.dto.LoginResponse;
import co.kr.compig.api.social.dto.SocialAuthResponse;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.api.social.google.GoogleAuthApi;
import co.kr.compig.api.social.keycloak.KeycloakAuthApi;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.keycloak.KeycloakProperties;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("googleLogin")
public class GoogleLoginServiceImpl implements SocialLoginService {

  private final GoogleAuthApi googleAuthApi;
  private final KeycloakAuthApi keycloakAuthApi;
  private final KeycloakProperties keycloakProperties;
  private GoogleIdTokenVerifier googleIdTokenVerifier;

  @Value("${social.client.google.rest-api-key}")
  private String googleAppKey;
  @Value("${social.client.google.secret-key}")
  private String googleAppSecret;
  @Value("${social.client.google.redirect-uri}")
  private String googleRedirectUri;
  @Value("${social.client.google.grant_type}")
  private String googleGrantType;

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
  public SocialAuthResponse getTokens(String authorizationCode) {
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
  public SocialUserResponse idTokenToResponse(String idToken) {
    String jsonString = "";
    try {
      //토큰 검증
      GoogleIdToken verifiedIdToken = googleIdTokenVerifier.verify(idToken);
      GoogleIdToken.Payload payload = verifiedIdToken.getPayload();
      jsonString = payload.toString();
    } catch (GeneralSecurityException | IOException e) {
      log.warn(e.getLocalizedMessage());
    }

    log.info("idTokenToResponse");
    log.info(jsonString.toString());

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .create();

    GoogleLoginResponse googleLoginResponse = gson.fromJson(jsonString, GoogleLoginResponse.class);

    return SocialUserResponse.builder()
        .memberRegisterType(MemberRegisterType.GOOGLE)
        .email(googleLoginResponse.getEmail())
        .build();
  }

  @Override
  public LoginResponse getKeycloakAccessToken(String userId, String userPw) {
    ResponseEntity<?> response = keycloakAuthApi.getAccessToken(
        KeycloakRequestAccessTokenDto.builder()
            .client_id(keycloakProperties.getClientId())
            .client_secret(keycloakProperties.getClientSecret())
            .username(userId)
            .password(userPw)
            .build()
    );
    log.info("keycloak user response");
    log.info(response.toString());

    LoginResponse loginResponse = new Gson()
        .fromJson(
            response.getBody().toString(),
            LoginResponse.class
        );
    loginResponse.setEmail(userId);

    return loginResponse;
  }

}
