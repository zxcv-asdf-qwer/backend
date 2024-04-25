package co.kr.compig.api.application.system;

import org.springframework.stereotype.Service;

import co.kr.compig.api.domain.system.EncryptKey;
import co.kr.compig.api.domain.system.EncryptKeyRepository;
import co.kr.compig.global.code.EncryptTarget;
import co.kr.compig.global.crypt.AES256;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EncryptKeyService {
	private final EncryptKeyRepository encryptKeyRepository;

	public AES256 getEncryptKey() {
		EncryptKey encryptKey = encryptKeyRepository.findByEncryptTarget(EncryptTarget.ACCOUNT)
			.orElseThrow(NotExistDataException::new);
		return new AES256(encryptKey.getEncryptKey());
	}
}
