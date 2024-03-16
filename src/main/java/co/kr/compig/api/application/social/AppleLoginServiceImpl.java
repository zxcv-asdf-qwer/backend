package co.kr.compig.api.application.social;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleIdTokenPayload;
import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleSocialTokenResponse;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;
import co.kr.compig.api.domain.code.MemberRegisterType;
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

	@Override
	public SocialUserResponse appTokenToSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		//TODO idToken 유효성검사
		AppleIdTokenPayload appleLoginResponse = appleGetMemberInfoService.decodePayload(socialLoginRequest.getToken(),
			AppleIdTokenPayload.class);

		log.info(getServiceName().getCode() + " appTokenToSocialUserResponse");
		log.info(appleLoginResponse.toString());

		return SocialUserResponse.builder()
			.sub(appleLoginResponse.getSub())
			.memberRegisterType(getServiceName())
			.email(appleLoginResponse.getEmail())
			.build();
	}

	@Override
	public SocialUserResponse webTokenToSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		return null;
	}

	@Override
	public void revoke(LeaveRequest leaveRequest) {
		try {
			AppleSocialTokenResponse tokens = appleGetMemberInfoService.getTokens(leaveRequest.getCode());
			appleGetMemberInfoService.revokeTokens(tokens);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		log.info(getServiceName().getCode() + " appTokenToSocialUserResponse");
	}

}
