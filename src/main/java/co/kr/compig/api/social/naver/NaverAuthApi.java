package co.kr.compig.api.social.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "naverAuth", url = "https://openapi.naver.com")
public interface NaverAuthApi {

  @GetMapping(value = "/v1/nid/me")
  ResponseEntity<String> accessTokenToUserInfo(@RequestHeader("Authorization") String token);

  // https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=jyvqXeaVOVmV&client_secret=527300A0_COq1_XV33cf&access_token=c8ceMEJisO4Se7uGCEYKK1p52L93bHXLnaoETis9YzjfnorlQwEisqemfpKHUq2gY&service_provider=NAVER
  @PostMapping("/revoke")
  ResponseEntity<String> revokeAccessToken(@RequestBody String token);
}