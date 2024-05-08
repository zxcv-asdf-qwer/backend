package co.kr.compig.api.presentation.member;

import java.net.URI;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.presentation.member.request.PartnerMemberUpdate;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "보호자 회원", description = "회원 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/partner/members", produces = "application/json")
public class PartnerMemberController {

	private final MemberService memberService;

	@Operation(summary = "간병인 memberId 수정")
	@PutMapping("/{memberId}")
	public ResponseEntity<Response<?>> updatePartnerById(@PathVariable String memberId,
		@RequestBody @Valid PartnerMemberUpdate partnerMemberUpdate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.updatePartnerById(memberId, partnerMemberUpdate)))
			.build());
	}

	@Operation(summary = "프로필 사진 수정")
	@PostMapping(path = "/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response<?>> userPictureUpdate(@RequestPart(name = "picture") MultipartFile picture) {
		memberService.userPictureUpdate(picture);
		return ResponseEntity.created(URI.create("/partner/members/picture")).build();
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<Response<MemberResponse>> getUser() {
		return ResponseEntity.ok(Response.<MemberResponse>builder()
			.data(memberService.getMemberResponseByMemberId(SecurityUtil.getMemberId()))
			.build());
	}

	@Operation(summary = "탈퇴")
	@PutMapping(path = "/leave")
	public ResponseEntity<Response<?>> userLeave() {
		memberService.doUserLeave(SecurityUtil.getMemberId());
		return ResponseEntity.ok().build();
	}

}
