package co.kr.compig.api.social.apple;

import co.kr.compig.api.social.dto.ApplePublicKeyResponse;
import co.kr.compig.api.social.dto.AppleRefreshTokenResponse;
import co.kr.compig.api.social.dto.AppleSocialTokenResponse;
import jakarta.ws.rs.core.MediaType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "appleAuth", url = "https://appleid.apple.com")
public interface AppleAuthApi {
  /**
   * 승인 부여 코드 검증, 유효한 code 인지 Apple Server에 확인 요청
   * Apple Document URL ‣ https://developer.apple.com/documentation/sign_in_with_apple/generate_and_validate_tokens
   */
  @PostMapping(value = "/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED)
  AppleSocialTokenResponse getTokens( //idToken, accessToken, refreshToken
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("grant_type") String grantType,
      @RequestParam("code") String code
  );

  /**
   * 기존 새로 고침 토큰 유효성 검사
   * Apple Document URL ‣ https://developer.apple.com/documentation/sign_in_with_apple/generate_and_validate_tokens
   */
  @PostMapping(value = "/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED)
  AppleRefreshTokenResponse getRefreshToken( //refreshToken
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("grant_type") String grantType,
      @RequestParam("refresh_token") String refreshToken
  );

  /**
   * 토큰 서명 확인을 위해 Apple의 공개 키 가져오기
   * Apple Document URL ‣ https://developer.apple.com/documentation/sign_in_with_apple/fetch_apple_s_public_key_for_verifying_token_signature
   */
  @GetMapping(value = "/auth/keys")
  ApplePublicKeyResponse getPublicKey();
}