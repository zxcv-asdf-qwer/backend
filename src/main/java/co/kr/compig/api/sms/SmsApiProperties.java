package co.kr.compig.api.sms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "api.sms")
public class SmsApiProperties {

  private String url;
  private String serviceId;
  private String serviceKey;
}
