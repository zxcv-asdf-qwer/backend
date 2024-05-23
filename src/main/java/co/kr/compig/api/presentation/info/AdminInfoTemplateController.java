package co.kr.compig.api.presentation.info;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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

import co.kr.compig.api.application.info.InfoTemplateService;
import co.kr.compig.api.presentation.info.request.InfoTemplateCreateRequest;
import co.kr.compig.api.presentation.info.request.InfoTemplateSearchRequest;
import co.kr.compig.api.presentation.info.request.InfoTemplateUpdateRequest;
import co.kr.compig.api.presentation.info.response.InfoTemplateResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 알림 템플릿", description = "알림 템플릿 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/infoTemplate", produces = "application/json")
public class AdminInfoTemplateController {

	private final InfoTemplateService infoTemplateService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createInfoTemplate(
		@RequestBody @Valid InfoTemplateCreateRequest infoTemplateCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("infoTemplateId", infoTemplateService.create(infoTemplateCreateRequest)))
			.build());
	}

	@Operation(summary = "상세 조회")
	@GetMapping("/{infoTemplateId}")
	public ResponseEntity<Response<InfoTemplateResponse>> getByIdInfoTemplate(
		@PathVariable(name = "infoTemplateId") Long infoTemplateId) {
		return ResponseEntity.ok(Response.<InfoTemplateResponse>builder()
			.data(infoTemplateService.getById(infoTemplateId))
			.build());
	}

	@Operation(summary = "목록 조회 InfoTemplateResponse")
	@GetMapping("/pages")
	public ResponseEntity<PageResponse> getPage(
		@ParameterObject @ModelAttribute InfoTemplateSearchRequest infoTemplateSearchRequest) {
		Page<InfoTemplateResponse> page = infoTemplateService.getPage(infoTemplateSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping("/{infoTemplateId}")
	public ResponseEntity<Response<?>> updateByIdInfoTemplate(
		@PathVariable(name = "infoTemplateId") Long infoTemplateId,
		@RequestBody @Valid InfoTemplateUpdateRequest infoTemplateUpdateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("infoTemplateId", infoTemplateService.updateById(infoTemplateId, infoTemplateUpdateRequest)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping("/{infoTemplateId}")
	public ResponseEntity<Response<?>> deleteByIdInfoTemplate(
		@PathVariable(name = "infoTemplateId") Long infoTemplateId) {
		infoTemplateService.deleteById(infoTemplateId);
		return ResponseEntity.ok().build();
	}
}