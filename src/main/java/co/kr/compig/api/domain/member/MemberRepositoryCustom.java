package co.kr.compig.api.domain.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.response.GuardianMemberResponse;
import co.kr.compig.api.presentation.member.response.MemberPageResponse;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.api.presentation.member.response.PartnerMemberResponse;

@Repository
public interface MemberRepositoryCustom {

	Slice<MemberPageResponse> getUserPageCursor(MemberSearchRequest memberSearchRequest, Pageable pageable);

	Page<MemberResponse> getAdminPage(MemberSearchRequest memberSearchRequest, Pageable pageable);

	Page<PartnerMemberResponse> getPartnerPage(MemberSearchRequest memberSearchRequest, Pageable pageable);

	Page<GuardianMemberResponse> getGuardianPage(MemberSearchRequest memberSearchRequest, Pageable pageable);

}
