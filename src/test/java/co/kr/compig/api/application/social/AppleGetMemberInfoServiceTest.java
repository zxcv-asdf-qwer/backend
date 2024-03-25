package co.kr.compig.api.application.social;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleIdTokenPayload;

class AppleGetMemberInfoServiceTest {

	@Test
	void decodePayload() {
		String token = "eyJraWQiOiJmaDZCczhDIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLmV4YW1wbGUucmVjYXJlY2hhdC53ZWIiLCJleHAiOjE3MTExODQ1NjgsImlhdCI6MTcxMTA5ODE2OCwic3ViIjoiMDAxMzE1LjY5ZTFkMzY4Y2FhZDQ5OTBhYmNjMWE5MmM5N2YyNDJlLjAwMzkiLCJhdF9oYXNoIjoiMGpwcldHckpTU2oyUFVZVU9Bbk5ldyIsImF1dGhfdGltZSI6MTcxMTA5ODE2NSwibm9uY2Vfc3VwcG9ydGVkIjp0cnVlfQ.q3E3eqZqVKcJhKluF4fG27-0_LXXm8_Lcil6UhKrihaBZUWIjApnkZ5vk5dqzgGcVl8VG5eYD_pT8RW-poIIlaOHDCTTavkRoc0EmhB3jOXqDEgmgRWyp0uy-tTOF1950lJy2yBVvaRSjC5ahAuPXeVu578cYy5E-KwFIY8_ph0TTv1316ezmLALo55iWwTIdKGV779cfHWsOOO6-6gU6bId3-pSrsFTuxEQxAMN_ZpsftTnuZ5mn10rWHzuz_UeE9vagWm5ivVf4bfDTeD2TR4f2vQJ3w5131PWYzyhF2U3AFBh_Xc8WtLEB_l1nMmUb_Rd5ENNG95pSLf6hWjR0Q";

		String[] tokenParts = token.split("\\.");
		String payloadJWT = tokenParts[1];
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String payload = new String(decoder.decode(payloadJWT));
		ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			System.out.println(objectMapper.readValue(payload, AppleIdTokenPayload.class));
			System.out.println(objectMapper.readValue(payload, AppleIdTokenPayload.class));
		} catch (Exception e) {
			throw new RuntimeException("Error decoding token payload", e);
		}
	}
}