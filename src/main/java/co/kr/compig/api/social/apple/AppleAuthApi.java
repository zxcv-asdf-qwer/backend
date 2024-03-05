package co.kr.compig.api.social.apple;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "appleAuth", url = "https://appleid.apple.com/auth/token")
public interface AppleAuthApi {


}