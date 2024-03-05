package co.kr.compig.api.social;

import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.LoginResponse;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.member.MemberService;
import co.kr.compig.service.social.SocialUserService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialController {

  private final SocialUserService socialUserService;
  private final MemberService memberService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> doSocialLogin(
      @RequestBody LoginRequest loginRequest) {

    return ResponseEntity.created(URI.create("/login"))
        .body(
            socialUserService.doSocialLogin(loginRequest));
  }

  @PutMapping("/leave")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<Response<?>> userLeave(@RequestBody LeaveRequest leaveRequest) {
    socialUserService.doSocialRevoke(leaveRequest);
    return ResponseEntity.ok().build();
  }
}
