package co.kr.compig.api.application.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.app.AppVersion;
import co.kr.compig.api.domain.app.AppVersionRepository;
import co.kr.compig.api.domain.app.AppVersionRepositoryCustom;
import co.kr.compig.api.domain.code.AppOsType;
import co.kr.compig.api.presentation.app.request.AppVersionRequest;
import co.kr.compig.api.presentation.app.response.AppVersionResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.error.model.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AppVersionService {

	private final AppVersionRepository appVersionRepository;
	private final AppVersionRepositoryCustom appVersionRepositoryCustom;

	/**
	 * app version data create
	 */
	public void create(final AppVersionRequest request) {
		final AppVersion appVersion = request.toEntity();

		appVersionRepository.save(appVersion);
	}

	/**
	 * read recent by os type
	 * @param osType : os type
	 * @return AppVersionResponse
	 */
	@Transactional(readOnly = true)
	public AppVersionResponse getRecentByOsType(final String osType) {
		final AppOsType appOsType = AppOsType.of(osType);

		return appVersionRepositoryCustom.findRecentByOsCode(appOsType)
			.map(AppVersion::toResponse)
			.orElse(null);
	}

	/**
	 * read app version by pk
	 * @param appId : pk
	 * @return AppVersionResponse
	 */
	@Transactional(readOnly = true)
	public AppVersionResponse getById(final Long appId) {
		return appVersionRepository.findById(appId)
			.map(AppVersion::toResponse)
			.orElse(null);
	}

	/**
	 * update app version by pk
	 * @param appId pk
	 * @param request AppVersionRequest
	 */
	public void updateById(final Long appId, final AppVersionRequest request) {
		AppVersion appVersion = appVersionRepository.findById(appId)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));
		appVersion.update(request);
	}

	/**
	 * delete app version by pk
	 * @param appId app pk
	 */
	public void deleteById(final Long appId) {
		AppVersion appVersion = appVersionRepository.findById(appId)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));
		appVersionRepository.delete(appVersion);
	}
}
