package co.kr.compig.api.presentation.member;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.request.MemberUpdateRequest;
import co.kr.compig.api.presentation.member.response.MemberPageResponse;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/members", produces = "application/json")
public class UserMemberController {

	private final MemberService memberService;

	@PutMapping
	public ResponseEntity<Response<?>> userUpdate(
		@RequestBody MemberUpdateRequest memberUpdateRequest) {
		memberService.updateMember(memberUpdateRequest);
		return ResponseEntity.created(URI.create("/pb/members")).build();
	}

	@PostMapping(path = "/picture")
	public ResponseEntity<Response<?>> userPictureUpdate(@RequestPart(name = "file") MultipartFile picture) {
		memberService.userPictureUpdate(picture);
		return ResponseEntity.created(URI.create("/pb/members/picture")).build();
	}

	@GetMapping
	public ResponseEntity<Response<MemberResponse>> getUser() {
		return ResponseEntity.ok(Response.<MemberResponse>builder()
			.data(memberService.getUser())
			.build());
	}

	//google kakao naver 앱에서 탈퇴 후 -> /pb/members/leave
	@PutMapping(path = "/leave")
	public ResponseEntity<Response<?>> userLeave(@RequestBody LeaveRequest leaveRequest) {
		memberService.userLeave(leaveRequest);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/cursor")
	public ResponseEntity<SliceResponse<MemberPageResponse>> getPageCursor(
		@RequestBody @Valid MemberSearchRequest memberSearchRequest, Pageable pageable) {
		Slice<MemberPageResponse> slice = memberService.getUserPageCursor(memberSearchRequest, pageable);
		SliceResponse<MemberPageResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable,
			slice.hasNext());
		return ResponseEntity.ok(sliceResponse);
	}
}
