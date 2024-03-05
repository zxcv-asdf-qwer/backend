package co.kr.compig.api.social;

import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.LoginResponse;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.service.social.SocialUserService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialController {

  private final SocialUserService socialUserService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> doSocialLogin(
      @RequestBody LoginRequest loginRequest) {

    return ResponseEntity.created(URI.create("/login"))
        .body(
            socialUserService.doSocialLogin(MemberRegisterType.valueOf(loginRequest.getMemberRegisterType()), loginRequest));
  }


}
