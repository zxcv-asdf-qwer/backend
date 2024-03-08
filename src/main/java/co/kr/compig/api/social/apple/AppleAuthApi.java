package co.kr.compig.api.social.apple;

import co.kr.compig.api.social.dto.AppleSocialTokenInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "appleAuth", url = "https://appleid.apple.com")
public interface AppleAuthApi {

  @PostMapping("/auth/token")
  AppleSocialTokenInfoResponse getIdToken(
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("grant_type") String grantType,
      @RequestParam("code") String code
  );
}