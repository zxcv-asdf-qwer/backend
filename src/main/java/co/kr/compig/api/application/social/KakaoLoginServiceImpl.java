package co.kr.compig.api.application.social;

import java.time.LocalDateTime;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.kr.compig.global.code.MemberRegisterType;
import co.kr.compig.api.infrastructure.auth.social.kakao.KakaoAuthApi;
import co.kr.compig.api.infrastructure.auth.social.kakao.KakaoUserApi;
import co.kr.compig.api.infrastructure.auth.social.kakao.model.KaKaoLoginResponse;
import co.kr.compig.api.infrastructure.auth.social.kakao.model.KakaoProperties;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;
import co.kr.compig.global.utils.GsonLocalDateTimeAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("kakaoLogin")
public class KakaoLoginServiceImpl implements SocialLoginService {

	private final KakaoUserApi kakaoUserApi;
	private final KakaoAuthApi kakaoAuthApi;
	private final KakaoProperties kakaoProperties;

	@Override
	public MemberRegisterType getServiceName() {
		return MemberRegisterType.KAKAO;
	}

	@Override //accessToken
	public SocialUserResponse appSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		log.info(getServiceName().getCode() + " appSocialUserResponse");
		return this.accessTokenToUserInfo(socialLoginRequest.getToken());
	}

	@Override //code
	public SocialUserResponse webSocialUserResponse(SocialLoginRequest socialLoginRequest) {
		log.info(getServiceName().getCode() + " webSocialUserResponse");
		String accessToken = this.codeToAccessToken(socialLoginRequest.getCode());
		return this.accessTokenToUserInfo(accessToken);
	}

	@Override
	public void revoke(LeaveRequest leaveRequest) {
		log.info(getServiceName().getCode() + " revoke");
		String accessToken = this.codeToAccessToken(leaveRequest.getCode());
		try {
			kakaoAuthApi.revokeAccessToken("Bearer " + accessToken);
		} catch (HttpServerErrorException e) {
			log.error("Kakao revoke HttpServerErrorException - Status : {}, Message : {}",
				e.getStatusCode(),
				e.getMessage());
		} catch (UnknownHttpStatusCodeException e) {
			log.error("Kakao revoke UnknownHttpStatusCodeException - Status : {}, Message : {}",
				e.getStatusCode(),
				e.getMessage());
		}
	}

	private SocialUserResponse accessTokenToUserInfo(String accessToken) {
		try {
			ResponseEntity<?> response = kakaoUserApi.accessTokenToUserInfo(
				"Bearer " + accessToken);

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
				.socialId(kaKaoLoginResponse.getId())
				.memberRegisterType(getServiceName())
				.email(kaKaoLoginResponse.getKakao_account().getEmail())
				.name(kaKaoLoginResponse.getKakao_account().getName())
				.build();
		} catch (HttpServerErrorException e) {
			log.error("Kakao accessTokenToUserInfo HttpServerErrorException - Status : {}, Message : {}",
				e.getStatusCode(),
				e.getMessage());
		} catch (UnknownHttpStatusCodeException e) {
			log.error("Kakao accessTokenToUserInfo UnknownHttpStatusCodeException - Status : {}, Message : {}",
				e.getStatusCode(),
				e.getMessage());
		}
		return null;
	}

	private String codeToAccessToken(String code) {
		try {
			ResponseEntity<JSONObject> response = kakaoAuthApi.getAccessToken(
				kakaoProperties.getClientId(),
				kakaoProperties.getClientSecret(),
				kakaoProperties.getAuthorizationGrantType(),
				kakaoProperties.getRedirectUri(),
				code
			);
			JSONObject jsonObject = response.getBody();
			return jsonObject.get("access_token") + "";
		} catch (HttpServerErrorException e) {
			log.error("Kakao codeToAccessToken HttpServerErrorException - Status : {}, Message : {}",
				e.getStatusCode(),
				e.getMessage());
		} catch (UnknownHttpStatusCodeException e) {
			log.error("Kakao codeToAccessToken UnknownHttpStatusCodeException - Status : {}, Message : {}",
				e.getStatusCode(),
				e.getMessage());
		}
		return null;
	}
}
