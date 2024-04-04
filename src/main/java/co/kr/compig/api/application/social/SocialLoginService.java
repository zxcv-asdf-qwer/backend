package co.kr.compig.api.application.social;

import org.springframework.stereotype.Service;

import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;

@Service
public interface SocialLoginService {

	MemberRegisterType getServiceName();

	//token to userInfo
	SocialUserResponse appSocialUserResponse(SocialLoginRequest socialLoginRequest);

	SocialUserResponse webSocialUserResponse(SocialLoginRequest socialLoginRequest);

	void revoke(LeaveRequest leaveRequest);

}
