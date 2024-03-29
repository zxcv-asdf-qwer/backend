package co.kr.compig.api.presentation.permission;

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

import co.kr.compig.api.application.permission.MenuPermissionService;
import co.kr.compig.api.presentation.permission.request.MenuPermissionCreateRequest;
import co.kr.compig.api.presentation.permission.request.MenuPermissionSearchRequest;
import co.kr.compig.api.presentation.permission.request.MenuPermissionUpdateRequest;
import co.kr.compig.api.presentation.permission.response.MenuPermissionDetailResponse;
import co.kr.compig.api.presentation.permission.response.MenuPermissionResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/menu-permission", produces = "application/json")
public class AdminMenuPermissionController {

	private final MenuPermissionService menuPermissionService;

	@PostMapping
	public ResponseEntity<Response<?>> createMenuPermission(
		@ModelAttribute @Valid MenuPermissionCreateRequest menuPermissionCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("menuPermissionId", menuPermissionService.createMenuPermission(menuPermissionCreateRequest)))
			.build());
	}

	@GetMapping
	public ResponseEntity<PageResponse<MenuPermissionResponse>> pageListMenuPermission(
		@ModelAttribute @Valid MenuPermissionSearchRequest menuPermissionSearchRequest, Pageable pageable
	) {
		Page<MenuPermissionResponse> page = menuPermissionService.pageListMenuPermission(menuPermissionSearchRequest,
			pageable);
		PageResponse<MenuPermissionResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@GetMapping(path = "/{menuPermissionId}")
	public ResponseEntity<Response<MenuPermissionDetailResponse>> getMenuPermission(
		@PathVariable(name = "menuPermissionId") Long menuPermissionId
	) {
		return ResponseEntity.ok(Response.<MenuPermissionDetailResponse>builder()
			.data(menuPermissionService.getMenuPermission(menuPermissionId))
			.build());
	}

	@PutMapping(path = "/{menuPermissionId}")
	public ResponseEntity<Response<?>> updateMenuPermission(
		@PathVariable(name = "menuPermissionId") Long menuPermissionId,
		@RequestBody @Valid MenuPermissionUpdateRequest menuPermissionUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("menuPermissionId",
				menuPermissionService.updateMenuPermission(menuPermissionId, menuPermissionUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{menuPermissionId}")
	public ResponseEntity<Response<?>> deleteMenuPermission(
		@PathVariable(name = "menuPermissionId") Long menuPermissionId) {
		menuPermissionService.deleteMenuPermission(menuPermissionId);
		return ResponseEntity.ok().build();
	}
}
