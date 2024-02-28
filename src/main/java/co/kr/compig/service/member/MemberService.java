package co.kr.compig.service.member;

import co.kr.compig.api.member.dto.AdminMemberCreate;
import co.kr.compig.api.member.dto.GuardianMemberCreate;
import co.kr.compig.api.member.dto.PartnerMemberCreate;
import co.kr.compig.common.code.UserType;
import co.kr.compig.common.keycloak.KeycloakHandler;
import co.kr.compig.domain.member.Member;
import co.kr.compig.domain.member.MemberGroup;
import co.kr.compig.domain.member.MemberGroupRepository;
import co.kr.compig.domain.member.MemberRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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

  public String adminCreate(AdminMemberCreate adminMemberCreate) {
    Member member = adminMemberCreate.convertEntity();
    setReferenceDomain(adminMemberCreate.getUserType(), member);
    member.createUserKeyCloak(null, null);
    member.passwordEncode();

    return memberRepository.save(member).getId();
  }

  private void setReferenceDomain(UserType userType, Member member) {
    // keycloakHandler를 사용하여 그룹 리스트를 가져옴
    List<GroupRepresentation> groups = keycloakHandler.getGroups().groups();

    // 모든 그룹과 하위 그룹을 포함하는 하나의 리스트로 평탄화
    List<GroupRepresentation> allGroups = groups.stream()
        // 각 그룹에 대해 Stream<Group>을 반환
        .flatMap(group -> Stream.concat(Stream.of(group), group.getSubGroups().stream()))
        .toList();

    Optional<GroupRepresentation> handler = allGroups.stream()
        .filter(group -> group.getName().equals(userType.getCode()))
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

  public String guardianCreate(GuardianMemberCreate guardianMemberCreate) {
    Member member = guardianMemberCreate.convertEntity();
    setReferenceDomain(guardianMemberCreate.getUserType(), member);
    member.createUserKeyCloak(null, null);
    member.passwordEncode();

    return memberRepository.save(member).getId();
  }

  public String partnerCreate(PartnerMemberCreate partnerMemberCreate) {
    Member member = partnerMemberCreate.convertEntity();
    setReferenceDomain(partnerMemberCreate.getUserType(), member);
    member.createUserKeyCloak(null, null);
    member.passwordEncode();

    return memberRepository.save(member).getId();
  }
}
