package co.kr.compig.api.infrastructure.sms.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.kr.compig.global.code.SystemServiceType;
import co.kr.compig.api.domain.system.AccessKey;
import lombok.Getter;

@Getter
public class BizPpurioTokenResponse {

	/**
	 * {
	 *     "accesstoken": "asdfdsfdsafsadf.asdfdsaf.dsafuSvh-JuD3FTqhzzRfubWCKq2tWHsdoRE",
	 *     "type": "Bearer",
	 *     "expired": "20240312121927"
	 * }
	 */
	private String accesstoken;
	private String type;
	private String expired; //24시간 유효

	public AccessKey of() {
		return AccessKey.builder()
			.systemServiceType(SystemServiceType.SMS)
			.serviceName("bizPpurio")
			.accessKey(accesstoken)
			.expired(LocalDateTime.parse(expired, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
			.build();
	}
}
