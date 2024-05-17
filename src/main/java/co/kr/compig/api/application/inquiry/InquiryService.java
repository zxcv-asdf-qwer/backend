package co.kr.compig.api.application.inquiry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.inquiry.Answer;
import co.kr.compig.api.domain.inquiry.AnswerRepository;
import co.kr.compig.api.domain.inquiry.Question;
import co.kr.compig.api.domain.inquiry.QuestionRepository;
import co.kr.compig.api.domain.inquiry.QuestionRepositoryCustom;
import co.kr.compig.api.presentation.inquiry.request.AnswerCreateRequest;
import co.kr.compig.api.presentation.inquiry.request.AnswerUpdateRequest;
import co.kr.compig.api.presentation.inquiry.request.QuestionCreateRequest;
import co.kr.compig.api.presentation.inquiry.request.QuestionSearchRequest;
import co.kr.compig.api.presentation.inquiry.request.QuestionUpdateRequest;
import co.kr.compig.api.presentation.inquiry.response.QuestionDetailResponse;
import co.kr.compig.api.presentation.inquiry.response.QuestionResponse;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.dto.pagination.SliceResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InquiryService {
	private final QuestionRepository questionRepository;
	private final QuestionRepositoryCustom questionRepositoryCustom;
	private final AnswerRepository answerRepository;

	public Long createQuestion(QuestionCreateRequest questionCreateRequest) {
		Question question = questionCreateRequest.converterEntity();
		return questionRepository.save(question).getId();
	}

	@Transactional(readOnly = true)
	public Page<QuestionResponse> getQuestionPage(QuestionSearchRequest questionSearchRequest) {
		return questionRepositoryCustom.getQuestionPage(questionSearchRequest);
	}

	@Transactional(readOnly = true)
	public QuestionDetailResponse getQuestion(Long questionId) {
		Question question = questionRepository.findById(questionId).orElseThrow(NotExistDataException::new);
		QuestionDetailResponse response = question.toQuestionDetailResponse();
		if (question.getIsAnswer() != IsYn.Y) {
			return response;
		}
		Answer answer = answerRepository.findByQuestion(question).orElseThrow(NotExistDataException::new);
		response.setAnswerId(answer.getId());
		response.setAnswerContent(answer.getAnswerContent());
		response.setAnswerUpdateOn(answer.getCreatedAndModified().getUpdatedOn());
		return response;
	}

	public Long updateQuestion(Long questionId, QuestionUpdateRequest questionUpdateRequest) {
		Question question = questionRepository.findById(questionId).orElseThrow(NotExistDataException::new);
		question.update(questionUpdateRequest);
		return question.getId();
	}

	public void deleteQuestion(Long questionId) {
		Question question = questionRepository.findById(questionId).orElseThrow(NotExistDataException::new);
		questionRepository.delete(question);
	}

	@Transactional(readOnly = true)
	public SliceResponse<QuestionResponse> getQuestionSlice(QuestionSearchRequest request) {
		Pageable pageable = request.pageable();
		Slice<QuestionResponse> slice = questionRepositoryCustom.getQuestionSlice(request, pageable);
		return new SliceResponse<>(slice.getContent(), pageable, slice.hasNext(),
			slice.getContent().getLast().getQuestionId().toString());
	}

	public Long createAnswer(Long questionId, AnswerCreateRequest answerCreateRequest) {
		Question question = questionRepository.findById(questionId).orElseThrow(NotExistDataException::new);
		if (question.getIsAnswer() == IsYn.Y) {
			throw new IllegalArgumentException("이미 답변이 있습니다.");
		}
		Answer answer = answerCreateRequest.converterEntity(question);
		answerRepository.save(answer);
		question.updateIsAnswer(IsYn.Y);
		return answer.getId();
	}

	public Long updateAnswer(Long answerId, AnswerUpdateRequest answerUpdateRequest) {
		Answer answer = answerRepository.findById(answerId).orElseThrow(NotExistDataException::new);
		answer.update(answerUpdateRequest);
		return answer.getId();
	}

	public void deleteAnswer(Long questionId) {
		Question question = questionRepository.findById(questionId).orElseThrow(NotExistDataException::new);
		Answer answer = answerRepository.findByQuestion(question).orElseThrow(NotExistDataException::new);
		answerRepository.delete(answer);
		question.updateIsAnswer(IsYn.N);
	}
}
