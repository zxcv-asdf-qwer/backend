package co.kr.compig.service.social;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.kr.compig.api.infrastructure.auth.social.naver.NaverAuthApi;
import co.kr.compig.api.infrastructure.auth.social.naver.model.NaverLoginResponse;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.utils.GsonLocalDateTimeAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("naverLogin")
public class NaverLoginServiceImpl implements SocialLoginService {

	private final NaverAuthApi naverAuthApi;

	@Override
	public MemberRegisterType getServiceName() {
		return MemberRegisterType.NAVER;
	}

	@Override
	public SocialUserResponse tokenToSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		ResponseEntity<?> response = naverAuthApi.accessTokenToUserInfo(
			"Bearer " + socialLoginRequest.getToken());

		log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
		log.info(response.toString());

		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
			.create();

		NaverLoginResponse naverLoginResponse = gson.fromJson(
			response.getBody().toString(),
			NaverLoginResponse.class
		);

		return SocialUserResponse.builder()
			.sub(naverLoginResponse.getResponse().getId())
			.memberRegisterType(getServiceName())
			.email(naverLoginResponse.getResponse().getEmail())
			.build();
	}

	@Override
	public void revoke(LeaveRequest leaveRequest) {
	}

}
