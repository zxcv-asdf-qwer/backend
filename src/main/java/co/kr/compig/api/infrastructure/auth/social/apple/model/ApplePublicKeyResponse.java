package co.kr.compig.api.infrastructure.auth.social.apple.model;

import java.util.List;

import lombok.Getter;

@Getter
public class ApplePublicKeyResponse {

	private List<Key> keys;

	@Getter
	public static class Key {

		private String kty;
		private String kid;
		private String use;
		private String alg;
		private String n;
		private String e;
	}
}
