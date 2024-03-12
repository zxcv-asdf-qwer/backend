package co.kr.compig.service.social;

import org.springframework.stereotype.Service;

import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.infrastructure.auth.social.common.model.SocialLoginRequest;
import co.kr.compig.api.infrastructure.auth.social.common.model.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;

@Service
public interface SocialLoginService {

	MemberRegisterType getServiceName();

	//token to userInfo
	SocialUserResponse tokenToSocialUserResponse(SocialLoginRequest socialLoginRequest);

	//apple만
	void revoke(LeaveRequest leaveRequest);

}
