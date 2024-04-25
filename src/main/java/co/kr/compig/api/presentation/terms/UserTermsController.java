package co.kr.compig.api.presentation.terms;

import java.util.List;
import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.terms.TermsService;
import co.kr.compig.api.presentation.terms.request.TermsSearchRequest;
import co.kr.compig.api.presentation.terms.response.TermsDetailResponse;
import co.kr.compig.api.presentation.terms.response.TermsResponse;
import co.kr.compig.global.code.TermsType;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "사용자 이용약관", description = "이용약관 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/terms", produces = "application/json")
public class UserTermsController {
	private final TermsService termsService;

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<TermsResponse>> getTermsSlice(
		@ParameterObject @ModelAttribute @Valid TermsSearchRequest termsSearchRequest,
		@ParameterObject Pageable pageable
	) {
		return ResponseEntity.ok(termsService.getTermsSlice(termsSearchRequest, pageable));
	}

	@Operation(summary = "조회", description = "리스트")
	@GetMapping(path = "/list")
	public ResponseEntity<Map<TermsType, List<TermsResponse>>> getTermsList(
		@ParameterObject @ModelAttribute @Valid TermsSearchRequest termsSearchRequest
	) {
		return ResponseEntity.ok(termsService.getTermsList(termsSearchRequest));
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
}
