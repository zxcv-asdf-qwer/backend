package co.kr.compig.api.application.social;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleIdTokenPayload;

class AppleGetMemberInfoServiceTest {

	@Test
	void decodePayload() {
		String token = "eyJraWQiOiJweWFSUXBBYm5ZIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLmV4YW1wbGUucmVjYXJlY2hhdC53ZWIiLCJleHAiOjE3MTE0MzE4NTMsImlhdCI6MTcxMTM0NTQ1Mywic3ViIjoiMDAxMzE1LjY5ZTFkMzY4Y2FhZDQ5OTBhYmNjMWE5MmM5N2YyNDJlLjAwMzkiLCJhdF9oYXNoIjoibWVWTzVmTVV2Y1BBUm53V0pNTjU2dyIsImVtYWlsIjoiY29vbTY2MkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXV0aF90aW1lIjoxNzExMzQ1NDUwLCJub25jZV9zdXBwb3J0ZWQiOnRydWV9.KguZ6mg4QMMHdbuEqVV5fN5rk5alGsHI4gYvcNc-8cJL45YkAx5SdoAAnx-u19PBT04oXVnXV38RLH5Y24p7Kc0SkCivOENktYM23Zk0KYtoejtonstf-BNrsM_tDGKQNEDqi4njt86Nfl_TPTFMuALbLTDxMst6lGDVbPXXofloLyR30dFj69dHyOSTb1Yz0LeGxlhmnl8Jofaz1tKZxvLzpsPhQtaMLMe3lluqNIUxLA4PgZxW7no_sM11Vy3mEL8wYbS5CiC1HqREhlt_pDddghab3-tiXLPg6XMKNnOlHI7g9TZLgVMita_fj73O5EF-ezNnQVu3Slb0C8okGg";

		String[] tokenParts = token.split("\\.");
		String payloadJWT = tokenParts[1];
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String payload = new String(decoder.decode(payloadJWT));
		ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			System.out.println(objectMapper.readValue(payload, AppleIdTokenPayload.class).toString());
		} catch (Exception e) {
			throw new RuntimeException("Error decoding token payload", e);
		}
	}
}