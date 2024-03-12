package co.kr.compig.service.social;

import org.springframework.stereotype.Service;

import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;

@Service
public interface SocialLoginService {

	MemberRegisterType getServiceName();

	//token to userInfo
	SocialUserResponse tokenToSocialUserResponse(LoginRequest loginRequest);

	//apple만
	void revoke(LeaveRequest leaveRequest);

}
