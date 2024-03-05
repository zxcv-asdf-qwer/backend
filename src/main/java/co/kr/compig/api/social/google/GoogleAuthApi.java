package co.kr.compig.api.social.google;

import co.kr.compig.common.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "googleAuth", url = "https://oauth2.googleapis.com", configuration = OpenFeignConfig.class)
public interface GoogleAuthApi {

  @GetMapping("/tokeninfo")
  ResponseEntity<String> getAccessTokenToTokenInfo(
      @RequestParam("access_token") String access_token);

}