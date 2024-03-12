package co.kr.compig.api.infrastructure.auth.social.common;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.infrastructure.auth.social.common.model.SocialLoginRequest;
import co.kr.compig.api.infrastructure.auth.social.common.model.SocialLoginResponse;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.member.MemberService;
import co.kr.compig.service.social.SocialUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialController {

	private final SocialUserService socialUserService;
	private final MemberService memberService;

	@PostMapping("/login")
	public ResponseEntity<SocialLoginResponse> doSocialLogin(
		@RequestBody SocialLoginRequest socialLoginRequest) {

		return ResponseEntity.created(URI.create("/login"))
			.body(
				socialUserService.doSocialLogin(socialLoginRequest));
	}

	//apple 만 따로 탈퇴
	//google kakao naver 앱에서 탈퇴 후 -> /pb/members/leave
	@PostMapping("/leave")
	public ResponseEntity<Response<?>> userLeave(@RequestBody LeaveRequest leaveRequest) {
		socialUserService.doSocialRevoke(leaveRequest);
		return ResponseEntity.ok().build();
	}
}
