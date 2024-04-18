package co.kr.compig.api.application.system;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import co.kr.compig.api.domain.system.AccessKey;
import co.kr.compig.api.domain.system.AccessKeyRepository;
import co.kr.compig.api.infrastructure.sms.BizPpurioApi;
import co.kr.compig.api.infrastructure.sms.model.BizPpurioTokenResponse;
import co.kr.compig.api.infrastructure.sms.model.SmsApiProperties;
import co.kr.compig.global.code.SystemServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessKeyService {

	private final AccessKeyRepository accessKeyRepository;
	private final BizPpurioApi
		bizPpurioApi;
	private final SmsApiProperties smsApiProperties;

	public String getSecretKey(SystemServiceType systemServiceType) {
		if (systemServiceType == null) {
			throw new IllegalArgumentException("System service type cannot be null");
		}
		return accessKeyRepository.findBySystemServiceTypeOrderByIdDesc(systemServiceType)
			.stream()
			.findFirst()
			.map(accessKey -> {
				if (accessKey.getExpired().isBefore(LocalDateTime.now())) { // 만료시간이 현재 시간보다 과거일 경우
					return generateSmsAccessKey(); // 토큰 재발급
				}
				return accessKey.getAccessKey();
			})
			.orElseGet(this::generateSmsAccessKey); // 없어도 토큰 재발급
	}

	private String generateSmsAccessKey() {
		// String basicToken = generateBasicToken(smsApiProperties.getServiceId(),
		// 	smsApiProperties.getServiceKey());
		BizPpurioTokenResponse accessToken = bizPpurioApi.getAccessToken("Basic " + smsApiProperties.getBasicToken());
		AccessKey save = accessKeyRepository.save(accessToken.of());
		return save.getAccessKey();
	}

}
