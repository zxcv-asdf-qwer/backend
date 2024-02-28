package co.kr.compig.api.member;

import co.kr.compig.api.member.dto.AdminMemberCreate;
import co.kr.compig.api.member.dto.GuardianMemberCreate;
import co.kr.compig.api.member.dto.PartnerMemberCreate;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.member.MemberService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/members", produces = "application/json")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/admin")
  @Transactional
  public ResponseEntity<Response<?>> adminCreate(@RequestPart(value = "data") @Valid AdminMemberCreate adminMemberCreate) {
    return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
        .data(Map.of("memberId", memberService.adminCreate(adminMemberCreate)))
        .build());
  }

  @PostMapping("/guardian")
  @Transactional
  public ResponseEntity<Response<?>> guardianCreate(@RequestPart(value = "data") @Valid GuardianMemberCreate guardianMemberCreate) {
    return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
        .data(Map.of("memberId", memberService.guardianCreate(guardianMemberCreate)))
        .build());
  }

  @PostMapping("/partner")
  @Transactional
  public ResponseEntity<Response<?>> partnerCreate(@RequestPart(value = "data") @Valid PartnerMemberCreate partnerMemberCreate) {
    return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
        .data(Map.of("memberId", memberService.partnerCreate(partnerMemberCreate)))
        .build());
  }
}
