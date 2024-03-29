package co.kr.compig.api.application.permission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberRepository;
import co.kr.compig.api.domain.menu.Menu;
import co.kr.compig.api.domain.menu.MenuRepository;
import co.kr.compig.api.domain.permission.MenuPermission;
import co.kr.compig.api.domain.permission.MenuPermissionRepository;
import co.kr.compig.api.domain.permission.MenuPermissionRepositoryCustom;
import co.kr.compig.api.presentation.permission.request.MenuPermissionCreateRequest;
import co.kr.compig.api.presentation.permission.request.MenuPermissionSearchRequest;
import co.kr.compig.api.presentation.permission.request.MenuPermissionUpdateRequest;
import co.kr.compig.api.presentation.permission.response.MenuPermissionDetailResponse;
import co.kr.compig.api.presentation.permission.response.MenuPermissionResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuPermissionService {

	private final MenuPermissionRepository menuPermissionRepository;
	private final MenuPermissionRepositoryCustom menuPermissionRepositoryCustom;
	private final MenuRepository menuRepository;
	private final MemberRepository memberRepository;

	public Long createMenuPermission(MenuPermissionCreateRequest menuPermissionCreateRequest) {
		Member member = memberRepository.findById(menuPermissionCreateRequest.getMemberId()).orElseThrow(
			NotExistDataException::new);
		Menu menu = menuRepository.findById(menuPermissionCreateRequest.getMenuId())
			.orElseThrow(NotExistDataException::new);
		MenuPermission menuPermission = menuPermissionCreateRequest.converterEntity(member, menu);
		return menuPermissionRepository.save(menuPermission).getId();
	}

	public Page<MenuPermissionResponse> pageListMenuPermission(MenuPermissionSearchRequest menuPermissionSearchRequest,
		Pageable pageable) {
		return menuPermissionRepositoryCustom.findPage(menuPermissionSearchRequest, pageable);
	}

	public MenuPermissionDetailResponse getMenuPermission(Long menuPermissionId) {
		MenuPermission menuPermission = menuPermissionRepository.findById(menuPermissionId)
			.orElseThrow(NotExistDataException::new);
		return menuPermission.toMenuPermissionDetailResponse();
	}

	public Long updateMenuPermission(Long menuPermissionId, MenuPermissionUpdateRequest menuPermissionUpdateRequest) {
		MenuPermission menuPermission = menuPermissionRepository.findById(menuPermissionId)
			.orElseThrow(NotExistDataException::new);
		Member member = memberRepository.findById(menuPermissionUpdateRequest.getMemberId())
			.orElseThrow(NotExistDataException::new);
		Menu menu = menuRepository.findById(menuPermissionUpdateRequest.getMenuId())
			.orElseThrow(NotExistDataException::new);
		menuPermission.update(menuPermissionUpdateRequest, member, menu);
		return menuPermission.getId();
	}

	public void deleteMenuPermission(Long menuPermissionId) {
		MenuPermission menuPermission = menuPermissionRepository.findById(menuPermissionId)
			.orElseThrow(NotExistDataException::new);
		menuPermissionRepository.delete(menuPermission);
	}
}
