package co.kr.compig.api.application.member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.kr.compig.api.application.social.LoginServiceImpl;
import co.kr.compig.api.application.social.SocialLoginService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberGroup;
import co.kr.compig.api.domain.member.MemberGroupRepository;
import co.kr.compig.api.domain.member.MemberMapper;
import co.kr.compig.api.domain.member.MemberRepository;
import co.kr.compig.api.domain.member.MemberRepositoryCustom;
import co.kr.compig.api.infrastructure.auth.keycloak.KeycloakAuthApi;
import co.kr.compig.api.infrastructure.auth.keycloak.model.KeycloakAccessTokenRequest;
import co.kr.compig.api.presentation.member.request.AdminMemberCreate;
import co.kr.compig.api.presentation.member.request.AdminMemberUpdate;
import co.kr.compig.api.presentation.member.request.GuardianMemberCreate;
import co.kr.compig.api.presentation.member.request.GuardianMemberUpdate;
import co.kr.compig.api.presentation.member.request.LeaveRequest;
import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.request.MemberUpdateRequest;
import co.kr.compig.api.presentation.member.request.NoMemberCreate;
import co.kr.compig.api.presentation.member.request.PartnerMemberCreate;
import co.kr.compig.api.presentation.member.request.PartnerMemberUpdate;
import co.kr.compig.api.presentation.member.response.GuardianMemberResponse;
import co.kr.compig.api.presentation.member.response.MemberPageResponse;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.api.presentation.member.response.PartnerMemberResponse;
import co.kr.compig.api.presentation.pass.request.PassSaveRequest;
import co.kr.compig.api.presentation.social.request.SocialCreateRequest;
import co.kr.compig.api.presentation.social.request.SocialLoginRequest;
import co.kr.compig.api.presentation.social.response.SocialLoginResponse;
import co.kr.compig.api.presentation.social.response.SocialUserResponse;
import co.kr.compig.global.code.ApplicationType;
import co.kr.compig.global.code.MemberRegisterType;
import co.kr.compig.global.code.UseYn;
import co.kr.compig.global.code.UserType;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.keycloak.KeycloakHandler;
import co.kr.compig.global.keycloak.KeycloakHolder;
import co.kr.compig.global.keycloak.KeycloakProperties;
import co.kr.compig.global.utils.GsonLocalDateTimeAdapter;
import co.kr.compig.global.utils.S3Util;
import co.kr.compig.global.utils.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

	private final MemberRepositoryCustom memberRepositoryCustom;
	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;
	private final MemberGroupRepository memberGroupRepository;
	private final KeycloakHandler keycloakHandler;
	private final S3Util s3Util;
	private final List<SocialLoginService> loginServices;
	private final KeycloakAuthApi keycloakAuthApi;
	private final KeycloakProperties keycloakProperties;

	public String adminCreate(AdminMemberCreate adminMemberCreate) {
		Member member = adminMemberCreate.convertEntity();
		setReferenceDomain(member.getUserType(), member);
		member.createUserKeyCloak(null, null);
		member.passwordEncode();

		return memberRepository.save(member).getId();
	}

	public String guardianCreate(GuardianMemberCreate guardianMemberCreate) {
		Member member = guardianMemberCreate.convertEntity();
		setReferenceDomain(member.getUserType(), member);
		member.createUserKeyCloak(null, null);
		member.passwordEncode();

		return memberRepository.save(member).getId();
	}

	public String partnerCreate(PartnerMemberCreate partnerMemberCreate) {
		Member member = partnerMemberCreate.convertEntity();
		setReferenceDomain(member.getUserType(), member);
		member.createUserKeyCloak(null, null);
		member.passwordEncode();

		return memberRepository.save(member).getId();
	}

	public SocialLoginService getLoginService(MemberRegisterType memberRegisterType) {
		for (SocialLoginService loginService : loginServices) {
			if (memberRegisterType.equals(loginService.getServiceName())) {
				log.info("login service name: {}", loginService.getServiceName());
				return loginService;
			}
		}
		return new LoginServiceImpl();
	}

	public Object doSocialLogin(SocialLoginRequest socialLoginRequest) {
		SocialLoginService loginService = this.getLoginService(socialLoginRequest.getMemberRegisterType());
		SocialUserResponse socialUserResponse;
		if (socialLoginRequest.getApplicationType() != ApplicationType.WEB) {
			socialUserResponse = loginService.appSocialUserResponse(
				socialLoginRequest);
		} else {
			socialUserResponse = loginService.webSocialUserResponse(
				socialLoginRequest);
		}

		Optional<Member> optionalMember = memberRepository.findByEmailAndUseYn(socialUserResponse.getEmail(), UseYn.Y);
		if (optionalMember.isPresent()) {
			Member member = optionalMember.get();
			// 공통 로직 처리: 키클락 로그인 실행
			return this.getKeycloakAccessToken(member.getEmail(),
				member.getEmail() + member.getMemberRegisterType() + "compig");
			// 키클락 로그인 실행
		}
		return socialUserResponse;
	}

	public SocialLoginResponse doSocialCreate(SocialCreateRequest socialCreateRequest) {
		Optional<Member> optionalMember = memberRepository.findByEmailAndUseYn(socialCreateRequest.getEmail(), UseYn.Y);
		if (optionalMember.isPresent()) {
			throw new BizException("이미 가입된 회원 입니다.");
		}
		Member member = optionalMember.orElseGet(() -> {
			// 중복되지 않는 경우 새 회원 생성 후 반환
			Member newMember = socialCreateRequest.converterEntity();
			this.setReferenceDomain(socialCreateRequest.getUserType(), newMember);
			newMember.createUserKeyCloak(socialCreateRequest.getSocialId(), socialCreateRequest.getUserNm());
			newMember.passwordEncode();

			String newMemberId = memberRepository.save(newMember).getId();

			return memberRepository.findById(newMemberId).orElseThrow(() -> new RuntimeException("회원 생성 후 조회 실패"));
		});
		// 공통 로직 처리: 키클락 로그인 실행
		return this.getKeycloakAccessToken(member.getEmail(),
			member.getEmail() + member.getMemberRegisterType() + "compig");
		// 키클락 로그인 실행
	}

	public void setReferenceDomain(UserType userType, Member member) {
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

	private SocialLoginResponse getKeycloakAccessToken(String userId, String userPw) {
		ResponseEntity<?> response = keycloakAuthApi.getAccessToken(
			KeycloakAccessTokenRequest.builder()
				.client_id(keycloakProperties.getClientId())
				.client_secret(keycloakProperties.getClientSecret())
				.username(userId)
				.password(userPw)
				.build()
		);
		log.info("keycloak user response");
		log.info(response.toString());

		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
			.create();

		return gson.fromJson(
			Objects.requireNonNull(response.getBody()).toString(),
			SocialLoginResponse.class
		);
	}

	public String noMemberCreate(NoMemberCreate noMemberCreate) {
		Member noMember = noMemberCreate.convertEntity();
		noMember.setNoMemberCreate();
		return memberRepository.save(noMember).getId();
	}

	public void updateMember(MemberUpdateRequest memberUpdateRequest) {
		Member memberById = this.getMemberById(SecurityUtil.getMemberId());
		setReferenceDomain(memberUpdateRequest.getUserType(), memberById);
		memberById.updateUserKeyCloak();
		memberById.update(memberUpdateRequest);
	}

	public void userPictureUpdate(MultipartFile picture) {
		Optional<Member> byUserId = memberRepository.findById(SecurityUtil.getMemberId());
		byUserId.ifPresentOrElse(member -> {
			Optional.ofNullable(picture).ifPresent(file -> {
				member.setPicture(s3Util.uploads(List.of(file)).stream().findFirst().orElse(null));
			});
		}, () -> {
			throw new NotExistDataException();
		});
	}

	@Transactional(readOnly = true)
	public void availabilityEmail(String email) {
		if (memberRepository.findByEmailAndUseYn(email, UseYn.Y).isEmpty()) {
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

	@Transactional(readOnly = true)
	public Slice<MemberPageResponse> getUserPageCursor(@Valid MemberSearchRequest memberSearchRequest,
		Pageable pageable) {
		return memberRepositoryCustom.getUserPageCursor(memberSearchRequest, pageable);
	}

	@Transactional(readOnly = true)
	public Page<MemberResponse> getAdminPage(@Valid MemberSearchRequest memberSearchRequest) {
		return memberRepositoryCustom.getAdminPage(memberSearchRequest);
	}

	@Transactional(readOnly = true)
	public Page<PartnerMemberResponse> getPartnerPage(@Valid MemberSearchRequest memberSearchRequest) {
		return memberRepositoryCustom.getPartnerPage(memberSearchRequest);
	}

	@Transactional(readOnly = true)
	public List<GuardianMemberResponse> getGuardianPage(MemberSearchRequest memberSearchRequest) {
		return memberMapper.selectGuardianList(memberSearchRequest);
	}

	@Transactional(readOnly = true)
	public Member getMemberById(String memberId) {
		return memberRepository.findById(memberId).orElseThrow(
			NotExistDataException::new);
	}

	@Transactional(readOnly = true)
	public MemberResponse getMemberResponseByMemberId(String memberId) {
		Member member = this.getMemberById(memberId);
		return member.toResponse();
	}

	public void updateRecentLogin(String memberId) {
		Member member = this.getMemberById(memberId);
		member.updateRecentLogin();
	}

	public String updateAdminById(String memberId, AdminMemberUpdate adminMemberUpdate) {
		Member memberById = this.getMemberById(memberId);
		memberById.updateAdminMember(adminMemberUpdate);
		setReferenceDomain(memberById.getUserType(), memberById);
		memberById.updateUserKeyCloak();
		memberById.passwordEncode();
		return memberById.getId();
	}

	public String updatePartnerById(String memberId, PartnerMemberUpdate partnerMemberUpdate) {
		Member memberById = this.getMemberById(memberId);
		memberById.updatePartnerMember(partnerMemberUpdate);
		setReferenceDomain(memberById.getUserType(), memberById);
		memberById.updateUserKeyCloak();
		memberById.passwordEncode();
		return memberById.getId();
	}

	public String updateGuardianById(String memberId, GuardianMemberUpdate guardianMemberUpdate) {
		Member memberById = this.getMemberById(memberId);
		memberById.updateGuardianMember(guardianMemberUpdate);
		setReferenceDomain(memberById.getUserType(), memberById);
		memberById.updateUserKeyCloak();
		memberById.passwordEncode();
		return memberById.getId();
	}

	public void doUserLeave(String memberId, LeaveRequest leaveRequest) {
		Member member = getMemberById(memberId);
		doUserLeave(member, leaveRequest);
	}

	public void doUserLeave(Member member, LeaveRequest leaveRequest) {
		if (leaveRequest == null) {
			leaveRequest = new LeaveRequest();
		}
		if (member.getMemberRegisterType() != MemberRegisterType.GENERAL && leaveRequest.getCode() != null) {
			SocialLoginService loginService = this.getLoginService(member.getMemberRegisterType());
			loginService.revoke(leaveRequest);
		}

		member.setLeaveMember(leaveRequest.getLeaveReason());

		try {
			KeycloakHandler keycloakHandler = KeycloakHolder.get();
			keycloakHandler.deleteUser(member.getId());
		} catch (Exception e) {
			log.error("LeaveMember Keycloak Error", e);
		}
	}

	public String passUpdate(PassSaveRequest passSaveRequest) {
		Member memberById = this.getMemberById(SecurityUtil.getMemberId());
		memberById.passUpdate(passSaveRequest);
		return memberById.getId();
	}
}
