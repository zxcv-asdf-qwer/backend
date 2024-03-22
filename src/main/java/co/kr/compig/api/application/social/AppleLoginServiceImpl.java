package co.kr.compig.api.application.social;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleIdTokenPayload;
import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleSocialTokenResponse;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;
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
			return this.idTokenToUserInfo(tokens.getAccessToken());
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
		//TODO idToken 유효성검사
		AppleIdTokenPayload appleLoginResponse = appleGetMemberInfoService.decodePayload(idToken,
			AppleIdTokenPayload.class);

		log.info(appleLoginResponse.toString());

		return SocialUserResponse.builder()
			.socialId(appleLoginResponse.getSub())
			.memberRegisterType(getServiceName())
			.email(appleLoginResponse.getEmail())
			.build();
	}
}
