package co.kr.compig.api.application.social;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.kr.compig.api.infrastructure.auth.social.google.GoogleAuthApi;
import co.kr.compig.api.infrastructure.auth.social.google.model.GoogleLoginResponse;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;
import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.global.utils.GsonLocalDateTimeAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("googleLogin")
public class GoogleLoginServiceImpl implements SocialLoginService {

	private final GoogleAuthApi googleAuthApi;

	@Override
	public MemberRegisterType getServiceName() {
		return MemberRegisterType.GOOGLE;
	}

	@Override
	public SocialUserResponse appTokenToSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		try {
			ResponseEntity<?> response = googleAuthApi.getAccessTokenToTokenInfo(
				socialLoginRequest.getToken());

			log.info(getServiceName().getCode() + " appTokenToSocialUserResponse");
			log.info(response.toString());

			Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
				.create();

			GoogleLoginResponse googleLoginResponse = gson.fromJson(response.getBody().toString(),
				GoogleLoginResponse.class);

			return SocialUserResponse.builder()
				.sub(googleLoginResponse.getSub())
				.memberRegisterType(getServiceName())
				.email(googleLoginResponse.getEmail())
				.build();
		} catch (HttpServerErrorException e) {
			log.error("Google getUserInfo HttpServerErrorException - Status : {}, Message : {}",
				e.getStatusCode(), e.getMessage());
		} catch (UnknownHttpStatusCodeException e) {
			log.error("Google getUserInfo UnknownHttpStatusCodeException - Status : {}, Message : {}",
				e.getStatusCode(), e.getMessage());
		}

		return null;
	}

	@Override
	public SocialUserResponse webTokenToSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		return null;
	}

	@Override
	public void revoke(LeaveRequest leaveRequest) {
	}

}
