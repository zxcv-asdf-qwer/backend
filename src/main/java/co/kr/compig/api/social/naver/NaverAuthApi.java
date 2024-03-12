package co.kr.compig.api.social.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "naverAuth", url = "https://openapi.naver.com")
public interface NaverAuthApi {

	@GetMapping(value = "/v1/nid/me")
	ResponseEntity<String> accessTokenToUserInfo(@RequestHeader("Authorization") String accessToken);

}