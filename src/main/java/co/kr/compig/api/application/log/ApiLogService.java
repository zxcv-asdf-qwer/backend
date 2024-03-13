package co.kr.compig.api.application.log;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.log.ApiLog;
import co.kr.compig.api.domain.log.ApiLogRepository;
import co.kr.compig.global.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * API Log 처리 서비스
 */
@Slf4j
@Service
public class ApiLogService {

	private final ApiLogRepository apiLogRepository;

	public ApiLogService(ApiLogRepository apiLogRepository) {
		this.apiLogRepository = apiLogRepository;
	}

	/**
	 * API log 입력 LogInterceptor 에서 호출된다
	 *
	 * @param request
	 */
	@Transactional
	public void saveApiLog(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		apiLogRepository.save(
			ApiLog.builder()
				.httpMethod(request.getMethod())
				.requestUrl(request.getRequestURI())
				.userId(authentication.getName())
				.remoteIp(LogUtil.getUserIp())
				.build()
		);
	}
}
