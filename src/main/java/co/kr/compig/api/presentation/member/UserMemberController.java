package co.kr.compig.api.presentation.member;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.member.request.MemberUpdateRequest;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "회원 공통", description = "회원 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/user/members", produces = "application/json")
public class UserMemberController {

	private final MemberService memberService;

	@Operation(summary = "수정")
	@PutMapping
	public ResponseEntity<Response<?>> userUpdate(
		@RequestBody MemberUpdateRequest memberUpdateRequest) {
		memberService.updateMember(memberUpdateRequest);
		return ResponseEntity.created(URI.create("/user/members")).build();
	}

	@Operation(summary = "로그인한 사람 조회")
	@GetMapping
	public ResponseEntity<Response<MemberResponse>> getUser() {
		return ResponseEntity.ok(Response.<MemberResponse>builder()
			.data(memberService.getMemberResponseByMemberId(SecurityUtil.getMemberId()))
			.build());
	}

	@Operation(summary = "탈퇴")
	@PutMapping(path = "/leave")
	public ResponseEntity<Response<?>> userLeave(@RequestBody LeaveRequest leaveRequest) {
		memberService.doUserLeave(SecurityUtil.getMemberId(), leaveRequest);
		return ResponseEntity.ok().build();
	}
}
