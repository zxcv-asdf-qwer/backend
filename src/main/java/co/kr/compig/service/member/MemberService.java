package co.kr.compig.service.member;

import co.kr.compig.api.member.dto.AdminMemberCreate;
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

  public String create(AdminMemberCreate adminMemberCreate) {
    return null;
  }

  public void passwordEncode() {

  }
}
