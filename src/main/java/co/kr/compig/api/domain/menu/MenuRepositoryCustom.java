package co.kr.compig.api.domain.menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.menu.request.MenuSearchRequest;
import co.kr.compig.api.presentation.menu.response.MenuResponse;

@Repository
public interface MenuRepositoryCustom {
	Page<MenuResponse> findPage(MenuSearchRequest menuSearchRequest, Pageable pageable);
}
