package co.kr.compig.service.member;

import co.kr.compig.api.member.dto.AdminMemberCreate;
import co.kr.compig.domain.member.Member;
import co.kr.compig.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;

  public Long create(AdminMemberCreate adminMemberCreate) {

    Member member = adminMemberCreate.convertEntity();

    // keycloak users 수정
    member.createUserKeyCloak(adminMemberCreate.getProviderId(),
        adminMemberCreate.getProviderUsername());
    member.passwordEncode();

    return null;
  }
}
