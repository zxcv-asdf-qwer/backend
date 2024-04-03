package co.kr.compig.api.presentation.sms;

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

import co.kr.compig.api.application.sms.SmsTemplateService;
import co.kr.compig.api.presentation.sms.request.SmsTemplateCreateRequest;
import co.kr.compig.api.presentation.sms.request.SmsTemplateSearchRequest;
import co.kr.compig.api.presentation.sms.request.SmsTemplateUpdateRequest;
import co.kr.compig.api.presentation.sms.response.SmsTemplateResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 SMS 템플릿", description = "SMS 템플릿 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/smsTemplate", produces = "application/json")
public class AdminSmsTemplateController {

	private final SmsTemplateService smsTemplateService;

	@Operation(summary = "생성하기")
	@PostMapping(path = "/smsTemplate")
	public ResponseEntity<Response<?>> createSmsTemplate(
		@RequestBody @Valid SmsTemplateCreateRequest smsTemplateCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("smsTemplateId", smsTemplateService.create(smsTemplateCreateRequest)))
			.build());
	}

	@Operation(summary = "상세 조회")
	@GetMapping("/{smsTemplateId}")
	public ResponseEntity<Response<SmsTemplateResponse>> getByIdSmsTemplate(
		@PathVariable(name = "smsTemplateId") Long smsTemplateId) {
		return ResponseEntity.ok(Response.<SmsTemplateResponse>builder()
			.data(smsTemplateService.getById(smsTemplateId))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<PageResponse<SmsTemplateResponse>> getPage(
		@ModelAttribute @Valid SmsTemplateSearchRequest smsTemplateSearchRequest, Pageable pageable) {
		Page<SmsTemplateResponse> page = smsTemplateService.getPage(smsTemplateSearchRequest, pageable);
		PageResponse<SmsTemplateResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping("/{smsTemplateId}")
	public ResponseEntity<Response<?>> updateByIdSmsTemplate(
		@PathVariable(name = "smsTemplateId") Long smsTemplateId,
		@RequestBody @Valid SmsTemplateUpdateRequest smsTemplateUpdateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("smsTemplateId", smsTemplateService.updateById(smsTemplateId, smsTemplateUpdateRequest)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping("/{smsTemplateId}")
	public ResponseEntity<Response<?>> deleteByIdSmsTemplate(@PathVariable(name = "smsTemplateId") Long smsTemplateId) {
		smsTemplateService.deleteById(smsTemplateId);
		return ResponseEntity.ok().build();
	}
}