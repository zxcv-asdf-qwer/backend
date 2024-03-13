package co.kr.compig.api.presentation.inquiry;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/inquiry", produces = "application/json")
public class UserInquiryController {
	private final InquiryService questionService;

	@PostMapping(path = "/question")
	public ResponseEntity<Response<?>> createQuestion(
		@ModelAttribute @Valid QuestionCreateRequest questionCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("questionId", questionService.createQuestion(questionCreateRequest)))
			.build());
	}

	@GetMapping(path = "/question")
	public ResponseEntity<SliceResponse<QuestionResponse>> pageListQuestion(
		@RequestBody @Valid QuestionSearchRequest questionSearchRequest, Pageable pageable) {
		Slice<QuestionResponse> slice = questionService.pageListCursor(questionSearchRequest, pageable);
		SliceResponse<QuestionResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable,
			slice.hasNext());
		return ResponseEntity.ok(sliceResponse);
	}

	@GetMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<QuestionDetailResponse>> getQuestion(
		@PathVariable(name = "questionId") Long questionId
	) {
		return ResponseEntity.ok(Response.<QuestionDetailResponse>builder()
			.data(questionService.getQuestion(questionId))
			.build());
	}

	@PutMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<?>> updateQuestion(@PathVariable(name = "questionId") Long questionId,
		@RequestBody @Valid QuestionUpdateRequest questionUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("questionId", questionService.updateQuestion(questionId, questionUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/question/{questionId}")
	public ResponseEntity<Response<?>> deleteQuestion(@PathVariable(name = "questionId") Long questionId) {
		questionService.deleteQuestion(questionId);
		return ResponseEntity.ok().build();
	}
}
