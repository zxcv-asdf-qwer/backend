package co.kr.compig.api.application.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.member.NoMember;
import co.kr.compig.api.domain.member.NoMemberRepository;
import co.kr.compig.api.presentation.member.request.NoMemberCreate;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoMemberService {

	private final NoMemberRepository noMemberRepository;

	public Long noMemberCreate(NoMemberCreate noMemberCreate) {
		NoMember noMember = noMemberCreate.convertEntity();
		return noMemberRepository.save(noMember).getId();
	}

	@Transactional(readOnly = true)
	public NoMember getNoMemberById(String memberId) {
		return noMemberRepository.findById(memberId).orElseThrow(
			NotExistDataException::new);
	}

}
