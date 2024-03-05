package co.kr.compig.service.social;

import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.api.social.dto.LoginRequest;
import co.kr.compig.api.social.dto.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {

  MemberRegisterType getServiceName();

  //token to userInfo
  SocialUserResponse tokenToSocialUserResponse(LoginRequest loginRequest);

  //token to userInfo
  void revoke(LeaveRequest leaveRequest);

}
