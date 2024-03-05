package co.kr.compig.api.social.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "googleAuth", url = "https://oauth2.googleapis.com")
public interface GoogleAuthApi {

  @GetMapping("/tokeninfo")
  ResponseEntity<String> getAccessTokenToTokenInfo(
      @RequestParam("access_token") String access_token);

  @PostMapping("/revoke")
  ResponseEntity<String> revokeAccessToken(@RequestBody String token);
}