package co.kr.compig.api.application.system;

import org.springframework.stereotype.Service;

import co.kr.compig.api.domain.system.EncryptKey;
import co.kr.compig.api.domain.system.EncryptKeyRepository;
import co.kr.compig.global.code.EncryptTarget;
import co.kr.compig.global.crypt.AES256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EncryptKeyService {
	private final EncryptKeyRepository encryptKeyRepository;

	public AES256 getEncryptKey() {
		EncryptKey encryptKey = encryptKeyRepository.findByEncryptTarget(EncryptTarget.ACCOUNT)
			.orElse(null);
		return new AES256(encryptKey != null ?encryptKey.getEncryptKey() : null);
	}
}
