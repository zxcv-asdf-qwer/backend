package co.kr.compig.service.member;

import co.kr.compig.api.member.dto.AdminMemberCreate;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.code.RoleCode;
import co.kr.compig.common.keycloak.KeycloakHandler;
import co.kr.compig.domain.member.Member;
import co.kr.compig.domain.member.MemberGroup;
import co.kr.compig.domain.member.MemberGroupRepository;
import co.kr.compig.domain.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;
  private final MemberGroupRepository memberGroupRepository;
  private final KeycloakHandler keycloakHandler;

  public String create(AdminMemberCreate adminMemberCreate) {

    Member member = adminMemberCreate.convertEntity();
    member.setMemberRegisterType(MemberRegisterType.GENERAL);
    setReferenceDomain(RoleCode.SYS_ADMIN, member);
    member.createUserKeyCloak(null, null);
    member.passwordEncode();

    return memberRepository.save(member).getId();
  }

  private void setReferenceDomain(RoleCode roleCode, Member member) {
    Optional<GroupRepresentation> handler = keycloakHandler.getGroups().groups().stream()
        .filter(group -> group.getName().equals(roleCode.getCode()))
        .findFirst();

    Optional<MemberGroup> memberGroup = memberGroupRepository.findByMember_id(member.getId());

    if (memberGroup.isPresent() && handler.isPresent()) {
      memberGroup.get().updateGroupInfo(handler.get().getName(), handler.get().getPath());
    } else {
      member.addGroups(MemberGroup.builder()
          .groupKey(handler.get().getId())
          .groupNm(handler.get().getName())
          .groupPath(handler.get().getPath())
          .build());
    }
  }
}
