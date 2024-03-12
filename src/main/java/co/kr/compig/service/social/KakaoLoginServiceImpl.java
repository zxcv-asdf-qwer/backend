package co.kr.compig.service.social;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.kr.compig.api.infrastructure.auth.social.kakao.KakaoUserAuthApi;
import co.kr.compig.api.infrastructure.auth.social.kakao.model.KaKaoLoginResponse;
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
@Qualifier("kakaoLogin")
public class KakaoLoginServiceImpl implements SocialLoginService {

	private final KakaoUserAuthApi kakaoUserAuthApi;

	@Override
	public MemberRegisterType getServiceName() {
		return MemberRegisterType.KAKAO;
	}

	@Override
	public SocialUserResponse tokenToSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		ResponseEntity<?> response = kakaoUserAuthApi.accessTokenToUserInfo(
			"Bearer " + socialLoginRequest.getToken());

		log.info(getServiceName().getCode() + " tokenToSocialUserResponse");
		log.info(response.toString());

		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
			.create();

		KaKaoLoginResponse kaKaoLoginResponse = gson.fromJson(
			response.getBody().toString(),
			KaKaoLoginResponse.class
		);

		return SocialUserResponse.builder()
			.sub(kaKaoLoginResponse.getId())
			.memberRegisterType(getServiceName())
			.email(kaKaoLoginResponse.getKakao_account().getEmail())
			.build();
	}

	@Override
	public void revoke(LeaveRequest leaveRequest) {

	}

}
