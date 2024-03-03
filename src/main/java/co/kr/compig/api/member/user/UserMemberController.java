package co.kr.compig.api.member.user;

import co.kr.compig.api.member.dto.MemberResponse;
import co.kr.compig.api.member.dto.MemberUpdateRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.member.MemberService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/members", produces = "application/json")
public class UserMemberController {

  private final MemberService memberService;

  @PutMapping
  @Transactional
  public ResponseEntity<Response<?>> userUpdate(
      @RequestBody MemberUpdateRequest memberUpdateRequest) {
    memberService.updateMember(memberUpdateRequest);
    return ResponseEntity.created(URI.create("/pb/members")).build();
  }

  @PostMapping("/picture")
  @Transactional
  public ResponseEntity<Response<?>> userPictureUpdate(@RequestPart(name = "file") MultipartFile picture) {
    memberService.userPictureUpdate(picture);
    return ResponseEntity.created(URI.create("/pb/members/picture")).build();
  }

  @GetMapping
  @Transactional
  public ResponseEntity<Response<MemberResponse>> getUser() {
    return ResponseEntity.ok(Response.<MemberResponse>builder()
        .data(memberService.getUser())
        .build());
  }

  @PutMapping("/leave")
  @Transactional
  public ResponseEntity<Response<?>> userLeave(
      @RequestBody MemberUpdateRequest memberUpdateRequest) {
    memberService.updateMember(memberUpdateRequest);
    return ResponseEntity.created(URI.create("/pb/members/leave")).build();
  }

}
