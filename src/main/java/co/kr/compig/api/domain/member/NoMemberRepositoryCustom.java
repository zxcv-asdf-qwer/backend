package co.kr.compig.api.domain.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.response.NoMemberResponse;

@Repository
public interface NoMemberRepositoryCustom {

	Page<NoMemberResponse> getNoMemberPage(MemberSearchRequest memberSearchRequest, Pageable pageable);

}
