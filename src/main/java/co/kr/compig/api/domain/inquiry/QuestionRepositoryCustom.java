package co.kr.compig.api.domain.inquiry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.inquiry.request.QuestionSearchRequest;
import co.kr.compig.api.presentation.inquiry.response.QuestionResponse;

@Repository
public interface QuestionRepositoryCustom {
	Page<QuestionResponse> findPage(QuestionSearchRequest questionSearchRequest, Pageable pageable);

	Slice<QuestionResponse> findAllByCondition(QuestionSearchRequest questionSearchRequest, Pageable pageable);
}
