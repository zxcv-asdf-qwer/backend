package co.kr.compig.api.infrastructure.auth.social.apple.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth.apple")
public class AppleProperties {

	private String authorizationGrantType;
	private String clientId;
	private String keyId;
	private String teamId;
	private String issueUrl;
	private String audience;
	private String keyPath;
	private String redirectUri;
}
