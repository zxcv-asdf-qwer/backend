package co.kr.compig.api.application.member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.member.NoMember;
import co.kr.compig.api.domain.member.NoMemberRepository;
import co.kr.compig.api.domain.member.NoMemberRepositoryCustom;
import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.request.NoMemberCreate;
import co.kr.compig.api.presentation.member.response.NoMemberResponse;
import co.kr.compig.api.presentation.member.response.UserMainSearchResponse;
import co.kr.compig.global.dto.pagination.PageResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoMemberService {

	private final NoMemberRepository noMemberRepository;
	private final NoMemberRepositoryCustom noMemberRepositoryCustom;

	public Long noMemberCreate(NoMemberCreate noMemberCreate) {
		NoMember noMember = noMemberCreate.convertEntity();
		return noMemberRepository.save(noMember).getId();
	}

	@Transactional(readOnly = true)
	public NoMember getNoMemberById(String memberId) {
		return noMemberRepository.findById(memberId).orElseThrow(
			NotExistDataException::new);
	}

	@Transactional(readOnly = true)
	public PageResponse<NoMemberResponse> getNoMemberPage(@Valid MemberSearchRequest memberSearchRequest,
		Pageable pageable) {

		Page<NoMemberResponse> page = noMemberRepositoryCustom.getNoMemberPage(memberSearchRequest, pageable);
		return new PageResponse<>(page.getContent(), pageable, page.getTotalElements());
	}

	@Transactional(readOnly = true)
	public List<UserMainSearchResponse> getNoMembersByNameAndTelNo(String userNm, String userTel) {
		return noMemberRepository.findByUserNmOrTelNo(userNm, userTel)
			.stream()
			.map(NoMember::toUserMainSearchResponse)
			.toList();
	}
}
