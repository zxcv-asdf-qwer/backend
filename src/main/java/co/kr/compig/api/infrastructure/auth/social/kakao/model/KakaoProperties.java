package co.kr.compig.api.infrastructure.auth.social.kakao.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth.kakao")
public class KakaoProperties {

	private String clientId;
	private String clientSecret;
	private String authorizationGrantType;
	private String redirectUri;
}
