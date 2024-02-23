package co.kr.compig.api.member.admin;

import co.kr.compig.api.member.dto.AdminMemberCreate;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.member.MemberService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/members", produces = "application/json")
public class AdminMemberController {

  private final MemberService memberService;

  @PostMapping
  public ResponseEntity<Response<?>> create(@RequestBody @Valid AdminMemberCreate adminMemberCreate) {
    return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
        .data(Map.of("memberId", memberService.create(adminMemberCreate)))
        .build());
  }
}
