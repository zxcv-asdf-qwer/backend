package co.kr.compig.api.application.social;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.domain.code.ApplicationType;
import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberRepository;
import co.kr.compig.api.infrastructure.auth.keycloak.KeycloakAuthApi;
import co.kr.compig.api.infrastructure.auth.keycloak.model.KeycloakAccessTokenRequest;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialLoginResponse;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;
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
	private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

	private SocialLoginService getLoginService(MemberRegisterType memberRegisterType) {
		for (SocialLoginService loginService : loginServices) {
			if (memberRegisterType.equals(loginService.getServiceName())) {
				log.info("login service name: {}", loginService.getServiceName());
				return loginService;
			}
		}
		return new LoginServiceImpl();
	}

	public SocialLoginResponse doSocialLogin(SocialLoginRequest socialLoginRequest) {
		SocialLoginService loginService = this.getLoginService(socialLoginRequest.getMemberRegisterType());
		new SocialUserResponse();
		SocialUserResponse socialUserResponse;
		if (socialLoginRequest.getApplicationType() != ApplicationType.WEB) {
			socialUserResponse = loginService.appTokenToSocialUserResponse(
				socialLoginRequest);
		} else {
			socialUserResponse = loginService.webTokenToSocialUserResponse(
				socialLoginRequest);
		}

		Optional<Member> optionalMember = memberRepository.findByUserId(socialUserResponse.getSub());
		Member member = optionalMember.orElseGet(() -> {
			// 중복되지 않는 경우 새 회원 생성 후 반환
			String newMemberId = memberService.socialCreate(socialUserResponse.convertEntity());
			return memberRepository.findById(newMemberId)
				.orElseThrow(() -> new RuntimeException("회원 생성 후 조회 실패"));
		});

		// 공통 로직 처리: 키클락 로그인 실행
		return this.getKeycloakAccessToken(member.getEmail(),
			member.getEmail() + member.getMemberRegisterType());
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

		SocialLoginResponse socialLoginResponse = gson.fromJson(
			response.getBody().toString(),
			SocialLoginResponse.class
		);

		socialLoginResponse.setEmail(userId);
		JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(
			oAuth2ResourceServerProperties.getJwt().getIssuerUri());
		// LoginResponse에서 토큰 문자열 가져오기
		String jwtToken = socialLoginResponse.getAccess_token();
		// 토큰 디코딩 및 파싱하여 Jwt 객체 얻기
		Jwt jwt = jwtDecoder.decode(jwtToken);
		socialLoginResponse.setRoles(jwt.getClaim("groups"));

		return socialLoginResponse;
	}

	public void doSocialRevoke(LeaveRequest leaveRequest) {
		SocialLoginService loginService = this.getLoginService(leaveRequest.getMemberRegisterType());
		loginService.revoke(leaveRequest);
		memberService.socialUserLeave(leaveRequest);
	}
}
