package co.kr.compig.api.social.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "kakaoAuth", url = "https://kapi.kakao.com")
public interface KakaoUserApi {

	@GetMapping(value = "/v2/user/me")
	ResponseEntity<String> accessTokenToUserInfo(@RequestHeader("Authorization") String accessToken);

}