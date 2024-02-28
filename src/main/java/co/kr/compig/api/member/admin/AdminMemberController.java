package co.kr.compig.api.member.admin;

import co.kr.compig.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/members", produces = "application/json")
public class AdminMemberController {

  private final MemberService memberService;
}
