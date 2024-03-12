package co.kr.compig.service.social;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.infrastructure.auth.social.common.model.SocialLoginRequest;
import co.kr.compig.api.infrastructure.auth.social.common.model.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
@Qualifier("defaultLoginService")
public class LoginServiceImpl implements SocialLoginService {

	@Override
	public MemberRegisterType getServiceName() {
		return MemberRegisterType.GENERAL;
	}

	@Override
	public SocialUserResponse tokenToSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		return null;
	}

	@Override
	public void revoke(LeaveRequest leaveRequest) {
	}

}
