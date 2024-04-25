package co.kr.compig.api.presentation.inquiry;

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

import co.kr.compig.api.application.inquiry.InquiryService;
import co.kr.compig.api.presentation.inquiry.request.AnswerCreateRequest;
import co.kr.compig.api.presentation.inquiry.request.AnswerUpdateRequest;
import co.kr.compig.api.presentation.inquiry.request.QuestionCreateRequest;
import co.kr.compig.api.presentation.inquiry.request.QuestionSearchRequest;
import co.kr.compig.api.presentation.inquiry.request.QuestionUpdateRequest;
import co.kr.compig.api.presentation.inquiry.response.QuestionDetailResponse;
import co.kr.compig.api.presentation.inquiry.response.QuestionResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 1:1 문의", description = "1:1 문의 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/inquiry", produces = "application/json")
public class AdminInquiryController {
	private final InquiryService inquiryService;

	@Operation(summary = "질문 생성하기")
	@PostMapping(path = "/question")
	public ResponseEntity<Response<?>> createQuestion(
		@ParameterObject @RequestBody @Valid QuestionCreateRequest questionCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("questionId", inquiryService.createQuestion(questionCreateRequest)))
			.build());
	}

	@Operation(summary = "질문 조회", description = "페이징")
	@GetMapping(path = "/question")
	public ResponseEntity<PageResponse> getQuestionPage(
		@ParameterObject @ModelAttribute QuestionSearchRequest questionSearchRequest) {
		Page<QuestionResponse> page = inquiryService.getQuestionPage(questionSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "질문 상세 조회")
	@GetMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<QuestionDetailResponse>> getQuestion(
		@PathVariable(name = "questionId") Long questionId) {
		return ResponseEntity.ok(Response.<QuestionDetailResponse>builder()
			.data(inquiryService.getQuestion(questionId))
			.build());
	}

	@Operation(summary = "질문 정보 수정하기")
	@PutMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<?>> updateQuestion(
		@PathVariable(name = "questionId") Long questionId,
		@RequestBody @Valid QuestionUpdateRequest questionUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("questionId", inquiryService.updateQuestion(questionId, questionUpdateRequest)))
			.build());
	}

	@Operation(summary = "질문 삭제")
	@DeleteMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<?>> deleteQuestion(
		@PathVariable(name = "questionId") Long questionId) {
		inquiryService.deleteQuestion(questionId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "답변 생성하기")
	@PostMapping(path = "/answer/{questionId}")
	public ResponseEntity<Response<?>> createAnswer(
		@PathVariable(name = "questionId") Long questionId,
		@ParameterObject @RequestBody @Valid AnswerCreateRequest answerCreateRequest) {
		return ResponseEntity.ok()
			.body(Response.<Map<String, Long>>builder()
				.data(Map.of("answerId", inquiryService.createAnswer(questionId, answerCreateRequest)))
				.build());
	}

	@Operation(summary = "답변 수정하기")
	@PutMapping(path = "/answer/{answerId}")
	public ResponseEntity<Response<?>> updateAnswer(
		@PathVariable(name = "answerId") Long answerId,
		@ParameterObject @RequestBody @Valid AnswerUpdateRequest answerUpdateRequest) {
		return ResponseEntity.ok()
			.body(Response.<Map<String, Long>>builder()
				.data(Map.of("answerId", inquiryService.updateAnswer(answerId, answerUpdateRequest)))
				.build());
	}

	@Operation(summary = "답변 삭제")
	@DeleteMapping(path = "/answer/{questionId}")
	public ResponseEntity<Response<?>> deleteAnswer(
		@PathVariable(name = "questionId") Long questionId) {
		inquiryService.deleteAnswer(questionId);
		return ResponseEntity.ok().build();
	}
}
