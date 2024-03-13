package co.kr.compig.api.infrastructure.auth.social.apple.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AppleRefreshTokenResponse {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("expires_in")
	private Long expiresIn;

	@JsonProperty("id_token")
	private String idToken;
}
