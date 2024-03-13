package co.kr.compig.api.application.member;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.code.UserType;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberGroup;
import co.kr.compig.api.domain.member.MemberGroupRepository;
import co.kr.compig.api.domain.member.MemberRepository;
import co.kr.compig.api.presentation.member.request.AdminMemberCreate;
import co.kr.compig.api.presentation.member.request.GuardianMemberCreate;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.member.request.MemberUpdateRequest;
import co.kr.compig.api.presentation.member.request.PartnerMemberCreate;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.keycloak.KeycloakHandler;
import co.kr.compig.global.keycloak.KeycloakHolder;
import co.kr.compig.global.utils.S3Util;
import co.kr.compig.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	public void availabilityEmail(String email) {
		if (memberRepository.findByEmail(email).isEmpty()) {
			return;
		}
		throw new BizException("이미 가입된 아이디 입니다.");
	}

	@Transactional(readOnly = true)
	public String findEmail(String userNm, String userTel) {
		Member member = memberRepository.findByUserNmAndTelNo(userNm, userTel).orElseThrow(
			NotExistDataException::new);
		if (member.getMemberRegisterType() != MemberRegisterType.GENERAL) {
			throw new BizException(
				member.getMemberRegisterType().getDesc().concat(" 회원입니다. 소셜로그인을 선택해주세요."));
		}
		//TODO 인증번호 발송
		return member.getUserId();
	}

	public void userLeave(LeaveRequest leaveRequest) {
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

	public void socialUserLeave(LeaveRequest leaveRequest) {
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
