package co.kr.compig.api.presentation.terms;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "간병인 게시판", description = "게시판 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/partner/terms", produces = "application/json")
public class PartnerTermsController {

	private final TermsService termsService;

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<TermsResponse>> pageListTerms(
		@ParameterObject @ModelAttribute @Valid TermsSearchRequest termsSearchRequest,
		Pageable pageable
	) {
		Slice<TermsResponse> slice = termsService.pageListTermsCursor(termsSearchRequest, pageable);
		SliceResponse<TermsResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable, slice.hasNext());

		return ResponseEntity.ok(sliceResponse);
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