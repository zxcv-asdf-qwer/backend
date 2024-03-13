package co.kr.compig.api.presentation.inquiry;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/inquiry", produces = "application/json")
public class AdminInquiryController {
	private final InquiryService inquiryService;

	@PostMapping(path = "/question")
	public ResponseEntity<Response<?>> createQuestion(
		@ModelAttribute @Valid QuestionCreateRequest questionCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("questionId", inquiryService.createQuestion(questionCreateRequest)))
			.build());
	}

	@GetMapping(path = "/question")
	public ResponseEntity<PageResponse<QuestionResponse>> pageListQuestion(
		@RequestBody @Valid QuestionSearchRequest questionSearchRequest, Pageable pageable) {
		Page<QuestionResponse> page = inquiryService.pageListQuestionPage(questionSearchRequest, pageable);
		PageResponse<QuestionResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@GetMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<QuestionDetailResponse>> getQuestion(
		@PathVariable(name = "questionId") Long questionId
	) {
		return ResponseEntity.ok(Response.<QuestionDetailResponse>builder()
			.data(inquiryService.getQuestion(questionId))
			.build());
	}

	@PutMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<?>> updateQuestion(@PathVariable(name = "questionId") Long questionId,
		@RequestBody @Valid QuestionUpdateRequest questionUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("questionId", inquiryService.updateQuestion(questionId, questionUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<?>> deleteQuestion(@PathVariable(name = "questionId") Long questionId) {
		inquiryService.deleteQuestion(questionId);
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/answer/{questionId}")
	public ResponseEntity<Response<?>> createAnswer(@PathVariable(name = "questionId") Long questionId,
		@ModelAttribute @Valid AnswerCreateRequest answerCreateRequest) {
		return ResponseEntity.ok()
			.body(Response.<Map<String, Long>>builder()
				.data(Map.of("answerId", inquiryService.createAnswer(questionId, answerCreateRequest)))
				.build());
	}

	@PutMapping(path = "/answer/{answerId}")
	public ResponseEntity<Response<?>> updateAnswer(@PathVariable(name = "answerId") Long answerId,
		@RequestBody @Valid AnswerUpdateRequest answerUpdateRequest) {
		return ResponseEntity.ok()
			.body(Response.<Map<String, Long>>builder()
				.data(Map.of("answerId", inquiryService.updateAnswer(answerId, answerUpdateRequest)))
				.build());
	}

	@DeleteMapping(path = "/answer/{questionId}")
	public ResponseEntity<Response<?>> deleteAnswer(@PathVariable(name = "questionId") Long questionId) {
		inquiryService.deleteAnswer(questionId);
		return ResponseEntity.ok().build();
	}
}
