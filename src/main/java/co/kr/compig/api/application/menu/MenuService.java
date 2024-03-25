package co.kr.compig.api.application.menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.menu.Menu;
import co.kr.compig.api.domain.menu.MenuRepository;
import co.kr.compig.api.domain.menu.MenuRepositoryCustom;
import co.kr.compig.api.presentation.menu.request.MenuCreateRequest;
import co.kr.compig.api.presentation.menu.request.MenuSearchRequest;
import co.kr.compig.api.presentation.menu.request.MenuUpdateRequest;
import co.kr.compig.api.presentation.menu.response.MenuDetailResponse;
import co.kr.compig.api.presentation.menu.response.MenuResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

	private final MenuRepository menuRepository;
	private final MenuRepositoryCustom menuRepositoryCustom;

	public Long createMenu(MenuCreateRequest menuCreateRequest) {
		Menu parentMenu = null;
		if (menuCreateRequest.getParentMenuId() != null) {
			parentMenu = menuRepository.findById(menuCreateRequest.getParentMenuId())
				.orElseThrow(NotExistDataException::new);
		}
		Menu menu = menuCreateRequest.converterEntity(parentMenu);
		return menuRepository.save(menu).getId();
	}

	@Transactional(readOnly = true)
	public Page<MenuResponse> pageListMenu(MenuSearchRequest menuSearchRequest, Pageable pageable) {
		return menuRepositoryCustom.findPage(menuSearchRequest, pageable);
	}

	@Transactional(readOnly = true)
	public MenuDetailResponse getMenu(Long menuId) {
		Menu menu = menuRepository.findById(menuId).orElseThrow(NotExistDataException::new);
		return menu.toMenuDetailResponse();
	}

	public Long updateMenu(Long menuId, MenuUpdateRequest menuUpdateRequest) {
		Menu menu = menuRepository.findById(menuId).orElseThrow(NotExistDataException::new);
		Menu parent = null;
		if (menuUpdateRequest.getParentMenuId() != null) {
			parent = menuRepository.findById(menuUpdateRequest.getParentMenuId())
				.orElseThrow(NotExistDataException::new);
		}
		menu.update(menuUpdateRequest, parent);
		return menu.getId();
	}

	public void deleteMenu(Long menuId) {
		Menu menu = menuRepository.findById(menuId).orElseThrow(NotExistDataException::new);
		menuRepository.delete(menu);
	}
}
