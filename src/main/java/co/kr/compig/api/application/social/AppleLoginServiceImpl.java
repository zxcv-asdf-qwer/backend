package co.kr.compig.api.application.social;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleIdTokenPayload;
import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleSocialTokenResponse;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;
import co.kr.compig.global.error.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("appleLogin")
public class AppleLoginServiceImpl implements SocialLoginService {

	private final AppleGetMemberInfoService appleGetMemberInfoService;

	@Override
	public MemberRegisterType getServiceName() {
		return MemberRegisterType.APPLE;
	}

	@Override //idToken
	public SocialUserResponse appSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		log.info(getServiceName().getCode() + " appSocialUserResponse");
		return this.idTokenToUserInfo(socialLoginRequest.getToken());
	}

	@Override //code
	public SocialUserResponse webSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		log.info(getServiceName().getCode() + " webSocialUserResponse");
		try {
			AppleSocialTokenResponse tokens = appleGetMemberInfoService.getTokensForWeb(socialLoginRequest.getCode());
			if (socialLoginRequest.getUser() != null) {
				return this.idTokenToUserInfo(socialLoginRequest.getUser(), tokens.getIdToken());
			}
			return this.idTokenToUserInfo(tokens.getIdToken());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void revoke(LeaveRequest leaveRequest) {
		log.info(getServiceName().getCode() + " revoke");

		try {
			AppleSocialTokenResponse tokens = appleGetMemberInfoService.getTokensForApp(leaveRequest.getCode());
			appleGetMemberInfoService.revokeTokens(tokens);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}

	private SocialUserResponse idTokenToUserInfo(String idToken) {
		AppleIdTokenPayload appleLoginResponse = appleGetMemberInfoService.decodePayload(idToken,
			AppleIdTokenPayload.class);

		log.info(appleLoginResponse.toString());

		Optional.ofNullable(appleLoginResponse.getEmail()).orElseThrow(() -> new BizException(
			"apple 로그인 ERROR : 이메일이 없습니다."));

		return SocialUserResponse.builder()
			.socialId(appleLoginResponse.getSub())
			.memberRegisterType(getServiceName())
			.email(appleLoginResponse.getEmail())
			.build();
	}

	private SocialUserResponse idTokenToUserInfo(SocialLoginRequest.User user, String idToken) {
		AppleIdTokenPayload appleLoginResponse = appleGetMemberInfoService.decodePayload(idToken,
			AppleIdTokenPayload.class);

		log.info(appleLoginResponse.toString());

		Optional.ofNullable(appleLoginResponse.getEmail()).orElseThrow(() -> new BizException(
			"apple 로그인 ERROR : 이메일이 없습니다."));

		return SocialUserResponse.builder()
			.socialId(appleLoginResponse.getSub())
			.memberRegisterType(getServiceName())
			.email(user.getEmail())
			.name(user.getName().getFirstName() + " " + user.getName().getLastName())
			.build();
	}
}
