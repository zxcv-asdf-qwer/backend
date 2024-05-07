package co.kr.compig.api.domain.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.review.request.ReviewSearchRequest;
import co.kr.compig.api.presentation.review.response.ReviewResponse;

@Repository
public interface ReviewRepositoryCustom {
	Slice<ReviewResponse> findAllByCondition(ReviewSearchRequest reviewSearchRequest, Pageable pageable);

	Slice<ReviewResponse> findAllByMemberId(ReviewSearchRequest reviewSearchRequest, Pageable pageable);
}
