package co.kr.compig.api.social.kakao;

import co.kr.compig.api.social.dto.KaKaoRequestLeaveDto;
import co.kr.compig.common.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "kakaoAuth", url = "https://kapi.kakao.com",configuration = OpenFeignConfig.class)
public interface KakaoUserApi {

  //OIDC: 사용자 정보 가져오기
  //https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#oidc-user-info
  @GetMapping(value = "/v1/oidc/userinfo")
  ResponseEntity<String> accessTokenToUserInfo(@RequestHeader("Authorization") String token);

  @PostMapping("/v1/user/unlink")
  ResponseEntity<String> revokeAccessToken(@RequestHeader("Authorization") String token,
      @RequestBody KaKaoRequestLeaveDto kaKaoRequestLeaveDto);
}