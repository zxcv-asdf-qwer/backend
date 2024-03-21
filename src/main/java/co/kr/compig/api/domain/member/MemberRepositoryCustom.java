package co.kr.compig.api.domain.member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.response.MemberPageResponse;

@Repository
public interface MemberRepositoryCustom {

	Slice<MemberPageResponse> getUserPageCursor(MemberSearchRequest memberSearchRequest, Pageable pageable);
}
