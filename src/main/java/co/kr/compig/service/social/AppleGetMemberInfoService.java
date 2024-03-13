package co.kr.compig.service.social;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import co.kr.compig.api.infrastructure.auth.social.apple.AppleAuthApi;
import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleProperties;
import co.kr.compig.api.infrastructure.auth.social.apple.model.ApplePublicKeyResponse;
import co.kr.compig.api.infrastructure.auth.social.apple.model.ApplePublicKeyResponse.Key;
import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleRefreshTokenResponse;
import co.kr.compig.api.infrastructure.auth.social.apple.model.AppleSocialTokenResponse;
import co.kr.compig.common.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleGetMemberInfoService {

	private final AppleAuthApi appleAuthClient;
	private final AppleProperties appleProperties;

	public AppleSocialTokenResponse getTokens(String authorizationCode)
		throws NoSuchAlgorithmException {
		return appleAuthClient.getTokens(
			appleProperties.getClientId(),
			generateClientSecret(),
			appleProperties.getAuthorizationGrantType(),
			authorizationCode
		);
	}

	/*
	 client secret 발급
	 */
	private String generateClientSecret() throws NoSuchAlgorithmException {
		LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
		Date now = new Date();
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(readPrivateKey());
		KeyFactory kf = KeyFactory.getInstance("EC");
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(appleProperties.getKeyId())
			.build();
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
			.issuer(appleProperties.getTeamId()) //잘못 적은거 아님 주의
			.issueTime(now)
			.expirationTime(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
			.audience(appleProperties.getIssueUrl()) //잘못 적은거 아님 주의
			.subject(appleProperties.getAudience()) //잘못 적은거 아님 주의
			.build();
		SignedJWT jwt = new SignedJWT(header, claimsSet);
		try {
			ECPrivateKey ecPrivateKey = (ECPrivateKey)kf.generatePrivate(spec);
			JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey);
			jwt.sign(jwsSigner);
		} catch (JOSEException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
		return jwt.serialize();
	}

	/**
	 * 파일에서 private key 획득
	 * @return Private Key
	 */
	private byte[] readPrivateKey() {
		Resource resource = new ClassPathResource(appleProperties.getKeyPath());
		byte[] content = null;

		try (Reader keyReader = new InputStreamReader(resource.getInputStream());
			 PemReader pemReader = new PemReader(keyReader)) {
			{
				PemObject pemObject = pemReader.readPemObject();
				content = pemObject.getContent();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	public AppleRefreshTokenResponse getRefreshTokenToAccessToken(String refreshToken)
		throws NoSuchAlgorithmException {
		return appleAuthClient.getRefreshTokenToAccessToken(
			appleProperties.getClientId(),
			generateClientSecret(),
			appleProperties.getAuthorizationGrantType(),
			refreshToken
		);
	}

	private ResponseEntity<?> revokeToken(String token, String tokenType)
		throws NoSuchAlgorithmException {
		return appleAuthClient.revokeToken(
			appleProperties.getClientId(),
			generateClientSecret(),
			token,
			tokenType
		);
	}

	public void revokeTokens(AppleSocialTokenResponse token) throws NoSuchAlgorithmException {
		try {
			ResponseEntity<?> responseEntity = this.revokeToken(token.getAccessToken(), "access_token");
		} catch (Exception e) {
			log.error(String.format("탈퇴 처리 access_token 오류 회원 아이디 : %s", SecurityUtil.getMemberId()));
		}
		try {
			ResponseEntity<?> responseEntity1 = this.revokeToken(token.getRefreshToken(),
				"refresh_token");
		} catch (Exception e) {
			log.error(String.format("탈퇴 처리 refresh_token 오류 회원 아이디 : %s", SecurityUtil.getMemberId()));
		}
	}

	private PrivateKey getPrivateKey() {
		Security.addProvider(new BouncyCastleProvider());
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

		try {
			byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getKeyPath());

			PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
			return converter.getPrivateKey(privateKeyInfo);
		} catch (Exception e) {
			throw new RuntimeException("Error converting private key from String", e);
		}
	}

	public boolean verifyPublicKey(SignedJWT signedJWT) {
		try {
			ApplePublicKeyResponse keys = appleAuthClient.getPublicKey();
			ObjectMapper objectMapper = new ObjectMapper();
			for (Key key : keys.getKeys()) {
				RSAKey rsaKey = (RSAKey)JWK.parse(objectMapper.writeValueAsString(key));
				RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
				JWSVerifier verifier = new RSASSAVerifier(publicKey);

				if (signedJWT.verify(verifier)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * id_token을 decode해서 payload 값 가져오기
	 */
	public <T> T decodePayload(String token, Class<T> targetClass) {
		String[] tokenParts = token.split("\\.");
		String payloadJWT = tokenParts[1];
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String payload = new String(decoder.decode(payloadJWT));
		ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			return objectMapper.readValue(payload, targetClass);
		} catch (Exception e) {
			throw new RuntimeException("Error decoding token payload", e);
		}
	}
}
