package co.kr.compig.api.domain.permission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.permission.request.MenuPermissionSearchRequest;
import co.kr.compig.api.presentation.permission.response.MenuPermissionResponse;

@Repository
public interface MenuPermissionRepositoryCustom {

	Page<MenuPermissionResponse> findPage(MenuPermissionSearchRequest menuPermissionSearchRequest, Pageable pageable);
}
