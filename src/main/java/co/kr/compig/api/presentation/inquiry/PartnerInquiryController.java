package co.kr.compig.api.presentation.inquiry;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
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

import co.kr.compig.api.application.inquiry.InquiryService;
import co.kr.compig.api.presentation.inquiry.request.QuestionCreateRequest;
import co.kr.compig.api.presentation.inquiry.request.QuestionSearchRequest;
import co.kr.compig.api.presentation.inquiry.request.QuestionUpdateRequest;
import co.kr.compig.api.presentation.inquiry.response.QuestionDetailResponse;
import co.kr.compig.api.presentation.inquiry.response.QuestionResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "간병인 1:1 문의", description = "1:1 문의 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/partner/inquiry", produces = "application/json")
public class PartnerInquiryController {
	private final InquiryService questionService;

	@Operation(summary = "질문 생성하기")
	@PostMapping(path = "/question")
	public ResponseEntity<Response<?>> createQuestion(
		@ParameterObject @ModelAttribute @Valid QuestionCreateRequest questionCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("questionId", questionService.createQuestion(questionCreateRequest)))
			.build());
	}

	@Operation(summary = "질문 조회")
	@GetMapping(path = "/question")
	public ResponseEntity<SliceResponse<QuestionResponse>> getQuestionSlice(
		@ParameterObject @ModelAttribute @Valid QuestionSearchRequest questionSearchRequest,
		@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(questionService.getQuestionSlice(questionSearchRequest, pageable));
	}

	@Operation(summary = "질문 상세 조회")
	@GetMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<QuestionDetailResponse>> getQuestion(
		@PathVariable(name = "questionId") Long questionId
	) {
		return ResponseEntity.ok(Response.<QuestionDetailResponse>builder()
			.data(questionService.getQuestion(questionId))
			.build());
	}

	@Operation(summary = "질문 수정하기")
	@PutMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<?>> updateQuestion(
		@PathVariable(name = "questionId") Long questionId,
		@RequestBody @Valid QuestionUpdateRequest questionUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("questionId", questionService.updateQuestion(questionId, questionUpdateRequest)))
			.build());
	}

	@Operation(summary = "질문 삭제")
	@DeleteMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<?>> deleteQuestion(
		@PathVariable(name = "questionId") Long questionId) {
		questionService.deleteQuestion(questionId);
		return ResponseEntity.ok().build();
	}
}
