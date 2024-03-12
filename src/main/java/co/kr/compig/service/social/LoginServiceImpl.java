package co.kr.compig.service.social;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.SocialUserResponse;
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
	public SocialUserResponse tokenToSocialUserResponse(LoginRequest loginRequest) {
		return null;
	}

	@Override
	public void revoke(LeaveRequest leaveRequest) {
	}

}
