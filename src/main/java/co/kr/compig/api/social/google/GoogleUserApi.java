package co.kr.compig.api.social.google;

import co.kr.compig.common.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "googleUser", url = "https://accounts.google.com", configuration = OpenFeignConfig.class)
public interface GoogleUserApi {

  @GetMapping("/o/oauth2/revoke")
  ResponseEntity<String> revokeAccessToken(@RequestParam String token);
}