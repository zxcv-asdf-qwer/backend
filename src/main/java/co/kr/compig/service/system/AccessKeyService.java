package co.kr.compig.service.system;

import co.kr.compig.common.code.SystemServiceType;
import co.kr.compig.common.exception.NotExistDataException;
import co.kr.compig.domain.system.AccessKey;
import co.kr.compig.domain.system.AccessKeyRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessKeyService {

  private final AccessKeyRepository accessKeyRepository;

  public String getSecretKey(SystemServiceType systemServiceType) {
    List<AccessKey> bySystemServiceType = accessKeyRepository.findBySystemServiceTypeOrderByIdDesc(
        systemServiceType);
    Optional<AccessKey> first = bySystemServiceType.stream().findFirst();
    AtomicReference<String> returnAccessKey = new AtomicReference<>();
    first.ifPresentOrElse(accessKey -> {
      returnAccessKey.set(accessKey.getAccessKey());
    }, () -> {
      throw new NotExistDataException();
    });

    return returnAccessKey.get();
  }

}
