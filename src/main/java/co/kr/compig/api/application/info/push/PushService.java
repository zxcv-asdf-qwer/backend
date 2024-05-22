package co.kr.compig.api.application.info.push;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.push.Push;
import co.kr.compig.api.domain.push.PushRepository;
import co.kr.compig.api.domain.push.PushRepositoryCustom;
import co.kr.compig.api.presentation.push.request.PushCreate;
import co.kr.compig.api.presentation.push.request.PushSearchRequest;
import co.kr.compig.api.presentation.push.response.PushResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PushService {
	private final PushRepository pushRepository;
	private final PushRepositoryCustom pushRepositoryCustom;

	public Long create(PushCreate pushCreate) {
		Push push = pushCreate.convertEntity();
		Push save = pushRepository.save(push);
		return save.getId();
	}
	@Transactional(readOnly = true)
	public Page<PushResponse> getPage(PushSearchRequest pushSearchRequest) {
		return pushRepositoryCustom.findPage(pushSearchRequest);
	}
}
