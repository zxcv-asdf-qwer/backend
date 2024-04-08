package co.kr.compig.api.presentation.terms;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
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

import co.kr.compig.api.application.terms.TermsService;
import co.kr.compig.api.presentation.board.request.TermsUpdateRequest;
import co.kr.compig.api.presentation.terms.request.TermsCreateRequest;
import co.kr.compig.api.presentation.terms.request.TermsSearchRequest;
import co.kr.compig.api.presentation.terms.response.TermsDetailResponse;
import co.kr.compig.api.presentation.terms.response.TermsResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 이용약관", description = "이용약관 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/terms", produces = "application/json")
public class AdminTermsController {

	private final TermsService termsService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createTerms(
		@ParameterObject @ModelAttribute @Valid TermsCreateRequest adminTermsCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("termsId", termsService.createTermsAdmin(adminTermsCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<PageResponse<TermsResponse>> pageListBoard(
		@ParameterObject @ModelAttribute @Valid TermsSearchRequest termsSearchRequest,
		Pageable pageable
	) {
		Page<TermsResponse> page = termsService.pageListTerms(termsSearchRequest, pageable);
		PageResponse<TermsResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{termsId}")
	public ResponseEntity<Response<TermsDetailResponse>> getTerms(
		@PathVariable(name = "termsId") Long termsId
	) {
		return ResponseEntity.ok(Response.<TermsDetailResponse>builder()
			.data(termsService.getTerms(termsId))
			.build());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping(path = "/{termsId}")
	public ResponseEntity<Response<?>> updateTerms(
		@PathVariable(name = "termsId") Long termsId,
		@RequestBody @Valid TermsUpdateRequest termsUpdateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("termsId", termsService.updateTerms(termsId, termsUpdateRequest)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{termsId}")
	public ResponseEntity<Response<?>> deleteTerms(
		@PathVariable(name = "termsId") Long termsId
	) {
		termsService.deleteTerms(termsId);
		return ResponseEntity.ok().build();
	}
}
