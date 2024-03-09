package co.kr.compig.api.social.apple;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
}
