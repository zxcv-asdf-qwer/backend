package co.kr.compig.api.infrastructure.sms.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "api.sms")
public class SmsApiProperties {

	private String url;
	private String serviceId;
	private String serviceKey;
}
