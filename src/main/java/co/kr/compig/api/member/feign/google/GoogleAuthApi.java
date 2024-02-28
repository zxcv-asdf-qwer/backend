package co.kr.compig.api.member.feign.google;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "googleAuth", url="https://oauth2.googleapis.com")
public interface GoogleAuthApi {
//  @PostMapping("/token")
//  ResponseEntity<String> getAccessToken(@RequestBody GoogleRequestAccessTokenDto requestDto);
}