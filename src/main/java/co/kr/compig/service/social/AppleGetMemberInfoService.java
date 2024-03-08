package co.kr.compig.service.social;

import co.kr.compig.api.social.apple.AppleAuthApi;
import co.kr.compig.api.social.apple.AppleProperties;
import co.kr.compig.api.social.dto.AppleIdTokenPayload;
import co.kr.compig.api.social.dto.ApplePublicKeyResponse;
import co.kr.compig.api.social.dto.ApplePublicKeyResponse.Key;
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
import java.io.FileReader;
import java.io.IOException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleGetMemberInfoService {

  private final AppleAuthApi appleAuthClient;
  private final AppleProperties appleProperties;
  @Value("${}")
  private String ISS;
  @Value("${}")
  private String AUD;
  @Value("${}")
  private String SUB;
  @Value("${}")
  private String KEY_PATH;

  public AppleIdTokenPayload getTokens(String authorizationCode) throws NoSuchAlgorithmException {
    String idToken = appleAuthClient.getTokens(
        appleProperties.getClientId(),
        generateClientSecret(),
        appleProperties.getGrantType(),
        authorizationCode
    ).getIdToken();

    return decodePayload(idToken, AppleIdTokenPayload.class);
  }

  public AppleIdTokenPayload getRefreshToken(String refreshToken) throws NoSuchAlgorithmException {
    String idToken = appleAuthClient.getRefreshToken(
        appleProperties.getClientId(),
        generateClientSecret(),
        appleProperties.getGrantType(),
        refreshToken
    ).getIdToken();

    return decodePayload(idToken, AppleIdTokenPayload.class);
  }

  public boolean verifyPublicKey(SignedJWT signedJWT) {
    try {
      ApplePublicKeyResponse keys = appleAuthClient.getPublicKey();
      ObjectMapper objectMapper = new ObjectMapper();
      for (Key key : keys.getKeys()) {
        RSAKey rsaKey = (RSAKey) JWK.parse(objectMapper.writeValueAsString(key));
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

  private String generateClientSecret() throws NoSuchAlgorithmException {
    LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
    Date now = new Date();
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(readPrivateKey());
    KeyFactory kf = KeyFactory.getInstance("EC");
    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID("KEY_ID").build();
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
        .issuer(ISS)
        .issueTime(now)
        .expirationTime(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
        .audience(AUD)
        .subject(SUB)
        .build();
    SignedJWT jwt = new SignedJWT(header, claimsSet);
    try {

      ECPrivateKey ecPrivateKey = (ECPrivateKey) kf.generatePrivate(spec);
      JWSSigner jwsSigner = new ECDSASigner((ECPrivateKey) ecPrivateKey.getS());

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
   *
   * @return Private Key
   */
  private byte[] readPrivateKey() {

    Resource resource = new ClassPathResource(KEY_PATH);
    byte[] content = null;

    try (FileReader keyReader = new FileReader(resource.getURI().getPath());
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

  private PrivateKey getPrivateKey() {

    Security.addProvider(new BouncyCastleProvider());
    JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

    try {
      byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());

      PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
      return converter.getPrivateKey(privateKeyInfo);
    } catch (Exception e) {
      throw new RuntimeException("Error converting private key from String", e);
    }
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
