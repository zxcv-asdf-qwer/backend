package co.kr.compig.service.member;

import co.kr.compig.api.member.dto.AdminMemberCreate;
import co.kr.compig.api.member.dto.GuardianMemberCreate;
import co.kr.compig.api.member.dto.MemberResponse;
import co.kr.compig.api.member.dto.MemberUpdateRequest;
import co.kr.compig.api.member.dto.PartnerMemberCreate;
import co.kr.compig.api.social.dto.LeaveRequest;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.code.UserType;
import co.kr.compig.common.exception.BizException;
import co.kr.compig.common.exception.NotExistDataException;
import co.kr.compig.common.keycloak.KeycloakHandler;
import co.kr.compig.common.keycloak.KeycloakHolder;
import co.kr.compig.common.utils.S3Util;
import co.kr.compig.common.utils.SecurityUtil;
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
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;
  private final MemberGroupRepository memberGroupRepository;
  private final KeycloakHandler keycloakHandler;
  private final S3Util s3Util;

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
      memberGroup.get()
          .updateGroupInfo(handler.get().getId(), handler.get().getName(), handler.get().getPath());
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
    Optional.ofNullable(partnerMemberCreate.getPicture()).ifPresent(file -> {
      member.setPicture(s3Util.uploads(List.of(file)).stream().findFirst().orElse(null));
    });

    return memberRepository.save(member).getId();
  }

  public String socialCreate(Member member) {
    setReferenceDomain(UserType.USER, member);
    member.createUserKeyCloak(member.getUserId(), member.getUserNm());
    member.passwordEncode();

    return memberRepository.save(member).getId();
  }

  public void updateMember(MemberUpdateRequest memberUpdateRequest) {
    Optional.ofNullable(SecurityUtil.getUserId()).ifPresentOrElse(currentUserId ->
        memberRepository.findByUserId(currentUserId).ifPresentOrElse(
            member -> {
              setReferenceDomain(memberUpdateRequest.getUserType(), member);
              member.updateUserKeyCloak();
              member.update(memberUpdateRequest);
            }, () -> {
              throw new NotExistDataException();
            }), () -> {
      throw new NotExistDataException();
    });
  }

  public void userPictureUpdate(MultipartFile picture) {
    Optional<Member> byUserId = memberRepository.findByUserId(SecurityUtil.getUserId());
    byUserId.ifPresentOrElse(member -> {
      Optional.ofNullable(picture).ifPresent(file -> {
        member.setPicture(s3Util.uploads(List.of(file)).stream().findFirst().orElse(null));
      });
    }, () -> {
      throw new NotExistDataException();
    });
  }

  @Transactional(readOnly = true)
  public MemberResponse getUser() {
    Member byUserId = memberRepository.findByUserId(SecurityUtil.getUserId()).orElseThrow(
        NotExistDataException::new);
    return byUserId.toResponse();
  }

  @Transactional(readOnly = true)
  public boolean availabilityUserId(String userId) {
    Member byUserId = memberRepository.findByUserId(userId).orElseThrow(
        NotExistDataException::new);
    return byUserId != null;
  }

  @Transactional(readOnly = true)
  public Boolean availabilityEmail(String email) {
    Member byUserId = memberRepository.findByEmail(email).orElseThrow(
        NotExistDataException::new);
    return byUserId != null;
  }

  @Transactional(readOnly = true)
  public String findUserId(String userNm, String userEmail) {
    Member member = memberRepository.findByUserNmAndEmail(userNm, userEmail).orElseThrow(
        NotExistDataException::new);
    if (member.getMemberRegisterType() != MemberRegisterType.GENERAL) {
      throw new BizException(
          member.getMemberRegisterType().getDesc().concat(" 회원입니다. 소셜로그인을 선택해주세요."));
    }
    return member.getUserId();
  }

  public void userLeave(LeaveRequest leaveRequest) { //TODO 연결 끊기
    String userId = SecurityUtil.getUserId();
    Member member = memberRepository.findByUserId(userId).orElseThrow(NotExistDataException::new);
    member.setLeaveMember(leaveRequest.getLeaveReason());
    try {
      KeycloakHandler keycloakHandler = KeycloakHolder.get();
      keycloakHandler.deleteUser(member.getId());
    } catch (Exception e) {
      log.error("LeaveMember Keycloak Error", e);
    }
  }

  public void socialUserLeave(LeaveRequest leaveRequest) { //TODO 연결 끊기
    String userId = SecurityUtil.getUserId();
    Member member = memberRepository.findByUserId(userId).orElseThrow(NotExistDataException::new);
    member.setLeaveMember(leaveRequest.getLeaveReason());
    try {
      KeycloakHandler keycloakHandler = KeycloakHolder.get();
      keycloakHandler.deleteUser(member.getId());
    } catch (Exception e) {
      log.error("LeaveMember Keycloak Error", e);
    }
  }
}
