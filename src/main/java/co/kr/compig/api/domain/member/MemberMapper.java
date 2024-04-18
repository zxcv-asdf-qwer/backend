package co.kr.compig.api.domain.member;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.response.GuardianMemberResponse;

@Mapper
public interface MemberMapper {
	List<Member> selectMemberList(MemberSearchRequest request);
	List<GuardianMemberResponse> selectGuardianList(MemberSearchRequest request);

}
