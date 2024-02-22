package co.kr.compig.api.member.admin;

import static co.kr.compig.common.util.SecurityUtil.getCustomOauth2User;

import co.kr.compig.common.security.CustomOauth2User;
import co.kr.compig.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/members", produces = "application/json")
public class AdminMemberController {

  private final MemberService memberService;

  @GetMapping("/test")
  public String testController() {
    CustomOauth2User customOauth2User = getCustomOauth2User();
    return "ok?";
  }
}
