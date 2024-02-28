package co.kr.compig.api.member.user;

import co.kr.compig.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/members", produces = "application/json")
public class UserMemberController {

  private final MemberService memberService;

}
