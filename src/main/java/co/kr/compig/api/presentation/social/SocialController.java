package co.kr.compig.api.presentation.social;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.social.SocialUserService;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialCreateRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialLoginResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "소셜로그인", description = "소셜로그인 관련 API")
@RestController
@RequestMapping(path = "/pb/social")
@RequiredArgsConstructor
public class SocialController {

	private final SocialUserService socialUserService;

	@Operation(summary = "소셜 로그인")
	@PostMapping(path = "/login")
	public ResponseEntity<?> doSocialLogin(@RequestBody SocialLoginRequest socialLoginRequest) {
		return ResponseEntity.ok()
			.body(socialUserService.doSocialLogin(socialLoginRequest));
	}

	@PostMapping
	@Operation(summary = "웹용 소셜 회원가입")
	public ResponseEntity<SocialLoginResponse> doSocialCreate(@RequestBody SocialCreateRequest socialCreateRequest) {
		return ResponseEntity.created(URI.create("/social"))
			.body(socialUserService.doSocialCreate(socialCreateRequest));
	}

	//apple 만 따로 탈퇴
	//google kakao naver 앱에서 탈퇴 후 -> /pb/members/leave
	@Operation(summary = "탈퇴")
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping(path = "/leave")
	public ResponseEntity<Response<?>> userLeave(@RequestBody LeaveRequest leaveRequest) {
		socialUserService.doSocialRevoke(leaveRequest);
		return ResponseEntity.ok().build();
	}
}
