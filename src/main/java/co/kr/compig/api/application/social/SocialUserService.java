package co.kr.compig.api.application.social;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.domain.code.ApplicationType;
import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.domain.code.UserType;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberRepository;
import co.kr.compig.api.infrastructure.auth.keycloak.KeycloakAuthApi;
import co.kr.compig.api.infrastructure.auth.keycloak.model.KeycloakAccessTokenRequest;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialCreateRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialLoginResponse;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.keycloak.KeycloakProperties;
import co.kr.compig.global.utils.GsonLocalDateTimeAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialUserService {

	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final List<SocialLoginService> loginServices;
	private final KeycloakAuthApi keycloakAuthApi;
	private final KeycloakProperties keycloakProperties;

	private SocialLoginService getLoginService(MemberRegisterType memberRegisterType) {
		for (SocialLoginService loginService : loginServices) {
			if (memberRegisterType.equals(loginService.getServiceName())) {
				log.info("login service name: {}", loginService.getServiceName());
				return loginService;
			}
		}
		return new LoginServiceImpl();
	}

	public Object doSocialLogin(SocialLoginRequest socialLoginRequest) {
		SocialLoginService loginService = this.getLoginService(socialLoginRequest.getMemberRegisterType());
		SocialUserResponse socialUserResponse;
		if (socialLoginRequest.getApplicationType() != ApplicationType.WEB) {
			socialUserResponse = loginService.appSocialUserResponse(
				socialLoginRequest);
		} else {
			socialUserResponse = loginService.webSocialUserResponse(
				socialLoginRequest);
		}

		Optional<Member> optionalMember = memberRepository.findByEmailAndUseYn(socialUserResponse.getEmail(), UseYn.Y);
		if (optionalMember.isPresent()) {
			Member member = optionalMember.get();
			// 공통 로직 처리: 키클락 로그인 실행
			return this.getKeycloakAccessToken(member.getEmail(),
				member.getEmail() + member.getMemberRegisterType() + "compig");
			// 키클락 로그인 실행
		}
		return socialUserResponse;
	}

	public SocialLoginResponse doSocialCreate(SocialCreateRequest socialCreateRequest) {
		Optional<Member> optionalMember = memberRepository.findByEmailAndUseYn(socialCreateRequest.getEmail(), UseYn.Y);
		if (optionalMember.isPresent()) {
			throw new BizException("이미 가입된 회원 입니다.");
		}
		Member member = optionalMember.orElseGet(() -> {
			// 중복되지 않는 경우 새 회원 생성 후 반환
			Member newMember = socialCreateRequest.converterEntity();
			memberService.setReferenceDomain(UserType.USER, newMember);
			newMember.createUserKeyCloak(socialCreateRequest.getSocialId(), socialCreateRequest.getUserNm());
			newMember.passwordEncode();

			String newMemberId = memberRepository.save(newMember).getId();

			return memberRepository.findById(newMemberId).orElseThrow(() -> new RuntimeException("회원 생성 후 조회 실패"));
		});
		// 공통 로직 처리: 키클락 로그인 실행
		return this.getKeycloakAccessToken(member.getEmail(),
			member.getEmail() + member.getMemberRegisterType() + "compig");
		// 키클락 로그인 실행
	}

	private SocialLoginResponse getKeycloakAccessToken(String userId, String userPw) {
		ResponseEntity<?> response = keycloakAuthApi.getAccessToken(
			KeycloakAccessTokenRequest.builder()
				.client_id(keycloakProperties.getClientId())
				.client_secret(keycloakProperties.getClientSecret())
				.username(userId)
				.password(userPw)
				.build()
		);
		log.info("keycloak user response");
		log.info(response.toString());

		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
			.create();

		return gson.fromJson(
			Objects.requireNonNull(response.getBody()).toString(),
			SocialLoginResponse.class
		);
	}

	public void doSocialRevoke(LeaveRequest leaveRequest) {
		SocialLoginService loginService = this.getLoginService(leaveRequest.getMemberRegisterType());
		loginService.revoke(leaveRequest);
		memberService.socialUserLeave(leaveRequest);
	}

}
