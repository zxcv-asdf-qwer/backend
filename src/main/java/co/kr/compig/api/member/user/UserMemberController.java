package co.kr.compig.api.member.user;

import co.kr.compig.api.member.dto.MemberUpdateRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.member.MemberService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/members", produces = "application/json")
public class UserMemberController {

  private final MemberService memberService;

  @PutMapping
  @Transactional
  public ResponseEntity<Response<?>> userUpdate(
      @ModelAttribute MemberUpdateRequest memberUpdateRequest) {
    memberService.updateMember(memberUpdateRequest);
    return ResponseEntity.created(URI.create("/pb/members")).build();
  }

}
