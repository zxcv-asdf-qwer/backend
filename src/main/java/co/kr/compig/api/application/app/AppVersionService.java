package co.kr.compig.api.application.app;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.app.AppVersion;
import co.kr.compig.api.domain.app.AppVersionRepository;
import co.kr.compig.api.domain.app.AppVersionRepositoryCustom;
import co.kr.compig.api.presentation.app.request.AppVersionCreateRequest;
import co.kr.compig.api.presentation.app.request.AppVersionUpdateRequest;
import co.kr.compig.api.presentation.app.response.AppVersionResponse;
import co.kr.compig.global.code.AppOsType;
import co.kr.compig.global.dto.pagination.PageableRequest;
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

	public Long create(final AppVersionCreateRequest request) {
		//TODO "osCode", "lastVer", "minVer" uk validation 필요
		if (appVersionRepository.existsByOsCodeAndMinVer(request.getOsCode(), request.getMinVer())) {
			throw new IllegalArgumentException("이미 존재하는 버전입니다.");
		}
		AppVersion appVersion = request.converterEntity();
		return appVersionRepository.save(appVersion).getId();
	}

	@Transactional(readOnly = true)
	public AppVersionResponse getRecentByOsType(final String osType) {
		final AppOsType appOsType = AppOsType.valueOf(osType);

		return appVersionRepositoryCustom.findRecentByOsCode(appOsType)
			.map(AppVersion::toResponse)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));

	}

	@Transactional(readOnly = true)
	public AppVersionResponse getById(final Long appId) {
		return appVersionRepository.findById(appId)
			.map(AppVersion::toResponse)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));
	}

	public Long updateById(final Long appId, final AppVersionUpdateRequest request) {
		AppVersion appVersion = appVersionRepository.findById(appId)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));
		if (appVersionRepository.existsByOsCodeAndMinVer(appVersion.getOsCode(), request.getMinVer())) {
			throw new IllegalArgumentException("이미 존재하는 버전입니다.");
		}
		appVersion.update(request);
		return appVersion.getId();
	}

	public void deleteById(final Long appId) {
		AppVersion appVersion = appVersionRepository.findById(appId)
			.orElseThrow(() -> new NotExistDataException(ErrorCode.INVALID_NOT_EXIST_DATA));
		appVersionRepository.delete(appVersion);
	}

	@Transactional(readOnly = true)
	public Page<AppVersionResponse> getAppVersionPage(
		PageableRequest pageable) {
		return appVersionRepositoryCustom.getAppVersionPage(pageable);
	}
}
