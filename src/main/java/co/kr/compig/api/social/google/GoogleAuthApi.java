package co.kr.compig.api.social.google;

import co.kr.compig.api.social.dto.GoogleRequestAccessTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "googleAuth", url = "https://oauth2.googleapis.com")
public interface GoogleAuthApi {

  /**
   * POST /token HTTP/1.1 Host: oauth2.googleapis.com Content-Type:
   * application/x-www-form-urlencoded
   * <p>
   * code=4/P7q7W91a-oMsCeLvIaQm6bTrgtp7& client_id=your_client_id&
   * client_secret=your_client_secret& redirect_uri=https%3A//oauth2.example.com/code&
   * grant_type=authorization_code
   */
  @PostMapping("/token")
  ResponseEntity<String> getAccessToken(@RequestBody GoogleRequestAccessTokenDto requestDto);

  @PostMapping("/tokeninfo")
  ResponseEntity<String> getAccessTokenToTokenInfo(
      @RequestParam("access_token") String access_token);

  @PostMapping("/revoke")
  ResponseEntity<String> revokeAccessToken(@RequestBody String token);
}