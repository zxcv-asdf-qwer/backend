package co.kr.compig.api.application.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.app.AppVersion;
import co.kr.compig.api.domain.app.AppVersionRepository;
import co.kr.compig.api.domain.app.AppVersionRepositoryCustom;
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
	 * app version check data create
	 */
	public void create(final AppVersionRequest request) {
		final AppVersion appVersion = request.toEntity();

		appVersionRepository.save(appVersion);
	}

	/**
	 * recent version read
	 * @return AppVersionResponse
	 */
	@Transactional(readOnly = true)
	public AppVersionResponse get() {
		return appVersionRepositoryCustom.findByRecentVersion()
			.map(AppVersion::toResponse)
			.orElse(null);
	}

	/**
	 * matching version read
	 * @param version : version
	 * @return AppVersionResponse
	 */
	@Transactional(readOnly = true)
	public AppVersionResponse get(final Integer version) {
		return appVersionRepository.findByLastVer(version)
			.map(AppVersion::toResponse)
			.orElse(null);
	}

	/**
	 * matching version name read
	 * @param version : version name
	 * @return AppVersionResponse
	 */
	@Transactional(readOnly = true)
	public AppVersionResponse get(final String version) {
		return appVersionRepository.findByLastVerNm(version)
			.map(AppVersion::toResponse)
			.orElse(null);
	}

	/**
	 * app version update
	 * @param version version
	 * @param request AppVersionRequest
	 */
	public void update(final Integer version, final AppVersionRequest request) {
		AppVersion appVersion = appVersionRepository.findByLastVer(version)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));
		appVersion.update(request);
	}

	/**
	 * app version by version name update
	 * @param version version name
	 * @param request AppVersionRequest
	 */
	public void update(final String version, final AppVersionRequest request) {
		AppVersion appVersion = appVersionRepository.findByLastVerNm(version)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));
		appVersion.update(request);
	}

	/**
	 * app version by version delete
	 * @param version version
	 */
	public void delete(final Integer version) {
		AppVersion appVersion = appVersionRepository.findByLastVer(version)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));
		appVersionRepository.delete(appVersion);
	}

	/**
	 * app version by version name delete
	 * @param version version
	 */
	public void delete(final String version) {
		AppVersion appVersion = appVersionRepository.findByLastVerNm(version)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));
		appVersionRepository.delete(appVersion);
	}
}
