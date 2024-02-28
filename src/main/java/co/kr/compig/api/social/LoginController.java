package co.kr.compig.api.social;

import co.kr.compig.api.social.dto.LoginResponse;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.service.social.SocialUserService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
  private final SocialUserService socialUserService;

  @GetMapping("/google")
  public ResponseEntity<LoginResponse> doSocialLogin(@RequestParam(name = "code")String code) {

    return ResponseEntity.created(URI.create("/google"))
        .body(socialUserService.doSocialLogin(MemberRegisterType.GOOGLE, code));
  }
}
