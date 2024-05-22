package co.kr.compig.api.presentation.sms;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.info.sms.SmsService;
import co.kr.compig.api.presentation.sms.request.SmsSearchRequest;
import co.kr.compig.api.presentation.sms.response.SmsResponse;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "SMS 관리", description = "SMS 관리 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/sms", produces = "application/json")
public class AdminSmsController {

	private final SmsService smsService;

	@Operation(summary = "메세지 내역 관리")
	@GetMapping("/pages")
	public ResponseEntity<PageResponse> getPage(
		@ParameterObject @ModelAttribute SmsSearchRequest smsSearchRequest) {
		Page<SmsResponse> page = smsService.getPage(smsSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

}
