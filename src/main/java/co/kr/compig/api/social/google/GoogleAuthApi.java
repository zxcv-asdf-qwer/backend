package co.kr.compig.api.social.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "googleAuth", url = "https://oauth2.googleapis.com")
public interface GoogleAuthApi {

  @PostMapping("/revoke")
  ResponseEntity<String> revokeAccessToken(@RequestBody String token);
}