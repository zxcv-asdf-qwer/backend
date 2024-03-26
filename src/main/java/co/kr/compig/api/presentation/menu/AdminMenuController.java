package co.kr.compig.api.presentation.menu;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.menu.MenuService;
import co.kr.compig.api.presentation.menu.request.MenuCreateRequest;
import co.kr.compig.api.presentation.menu.request.MenuSearchRequest;
import co.kr.compig.api.presentation.menu.request.MenuUpdateRequest;
import co.kr.compig.api.presentation.menu.response.MenuDetailResponse;
import co.kr.compig.api.presentation.menu.response.MenuResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/menu", produces = "application/json")
public class AdminMenuController {
	private final MenuService menuService;

	@PostMapping
	public ResponseEntity<Response<?>> createMenu(
		@ModelAttribute @Valid MenuCreateRequest menuCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("menuId", menuService.createMenu(menuCreateRequest)))
			.build());
	}

	@GetMapping
	public ResponseEntity<PageResponse<MenuResponse>> pageListMenu(
		@ModelAttribute @Valid MenuSearchRequest menuSearchRequest,
		Pageable pageable
	) {
		Page<MenuResponse> page = menuService.pageListMenu(menuSearchRequest, pageable);
		PageResponse<MenuResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@GetMapping(path = "/{menuId}")
	public ResponseEntity<Response<MenuDetailResponse>> getMenu(
		@PathVariable(name = "menuId") Long menuId
	) {
		return ResponseEntity.ok(Response.<MenuDetailResponse>builder()
			.data(menuService.getMenu(menuId))
			.build());
	}

	@PutMapping(path = "/{menuId}")
	public ResponseEntity<Response<?>> updateMenu(@PathVariable(name = "menuId") Long menuId,
		@RequestBody @Valid MenuUpdateRequest menuUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("menuId", menuService.updateMenu(menuId, menuUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{menuId}")
	public ResponseEntity<Response<?>> deleteMenu(@PathVariable(name = "menuId") Long menuId) {
		menuService.deleteMenu(menuId);
		return ResponseEntity.ok().build();
	}
}