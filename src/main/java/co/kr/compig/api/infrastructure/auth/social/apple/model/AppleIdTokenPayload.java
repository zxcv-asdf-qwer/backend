package co.kr.compig.api.infrastructure.auth.social.apple.model;

import lombok.Getter;

@Getter
public class AppleIdTokenPayload {

	private String iss;
	private String aud;
	private Long exp;
	private Long iat;
	private String sub;
	private String nonce;
	private String c_hash;
	private String at_hash;
	private String email;
	private String name;
	private String email_verified;
	private String is_private_email;
	private Long auth_time;
	private boolean nonce_supported;
}
