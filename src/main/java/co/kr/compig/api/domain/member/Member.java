package co.kr.compig.api.domain.member;

import static co.kr.compig.global.utils.CalculateUtil.*;
import static co.kr.compig.global.utils.KeyGen.*;
import static co.kr.compig.global.utils.LogUtil.*;
import static co.kr.compig.global.utils.PasswordValidation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import co.kr.compig.api.domain.account.Account;
import co.kr.compig.api.domain.apply.Apply;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.patient.Patient;
import co.kr.compig.api.domain.permission.MenuPermission;
import co.kr.compig.api.domain.review.Review;
import co.kr.compig.api.domain.wallet.Wallet;
import co.kr.compig.api.presentation.member.request.AdminMemberUpdate;
import co.kr.compig.api.presentation.member.request.GuardianMemberUpdate;
import co.kr.compig.api.presentation.member.request.MemberUpdateRequest;
import co.kr.compig.api.presentation.member.request.PartnerMemberUpdate;
import co.kr.compig.api.presentation.member.response.GuardianMemberResponse;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.api.presentation.member.response.PartnerMemberResponse;
import co.kr.compig.api.presentation.pass.request.PassSaveRequest;
import co.kr.compig.global.code.CareerCode;
import co.kr.compig.global.code.DeptCode;
import co.kr.compig.global.code.DomesticForeignCode;
import co.kr.compig.global.code.GenderCode;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.MemberRegisterType;
import co.kr.compig.global.code.MemberType;
import co.kr.compig.global.code.OrderStatus;
import co.kr.compig.global.code.UseYn;
import co.kr.compig.global.code.UserType;
import co.kr.compig.global.code.converter.DeptCodeConverter;
import co.kr.compig.global.code.converter.MemberTypeConverter;
import co.kr.compig.global.code.converter.UserTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.exception.KeyCloakRequestException;
import co.kr.compig.global.keycloak.KeycloakHandler;
import co.kr.compig.global.keycloak.KeycloakHolder;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(name = "uk01_member", columnNames = {"userId"}),
	})
public class Member {

	@Id
	@Column(name = "member_id")
	private String id; // Keycloak 의 id

	@Column(length = 150)
	private String userId; // 사용자 아이디

	@Column(length = 150)
	private String userPw; // 사용자 비밀번호

	@Column(length = 100)
	private String userNm; // 사용자 명

	@Column(length = 150)
	private String email; // 이메일

	@Column(length = 100)
	private String telNo; // 전화번호

	@Column
	@Enumerated(EnumType.STRING)
	private GenderCode gender; // 성별

	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private UseYn useYn = UseYn.Y; // 사용유무

	@Column(length = 10)
	@Convert(converter = UserTypeConverter.class)
	private UserType userType; // 사용자 구분

	@Column(length = 10)
	@Convert(converter = MemberTypeConverter.class)
	@Builder.Default
	private MemberType memberType = MemberType.MEMBER; // 회원 비회원 구분

	@Column(length = 10)
	@Convert(converter = DeptCodeConverter.class)
	private DeptCode deptCode; // 부서 구분

	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private MemberRegisterType memberRegisterType;  // 회원가입 유형

	@Column(length = 6)
	private String jumin1; // 주민등록번호 앞자리

	@Column(length = 7)
	private String jumin2; // 주민등록번호 뒷자리

	@Column(length = 200)
	private String address1; // 주소1

	@Column(length = 200)
	private String address2; // 주소2

	@Column
	private String picture; //프로필사진 s3 저장소 Path

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private DomesticForeignCode domesticForeignCode; //외국인 내국인

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private CareerCode careerCode; //신입 경력

	@Column
	private Integer careStartYear; //근무 시작 연도

	@Column(columnDefinition = "TEXT")
	private String introduce; //자기소개

	@Column
	private LocalDate marketingEmailDate;  // 이메일 수신동의 날짜
	@Column
	private LocalDate marketingAppPushDate; // 앱 푸시알림 수신동의 날짜
	@Column
	private LocalDate marketingKakaoDate;  // 알림톡 수신동의 날짜
	@Column
	private LocalDate marketingSmsDate;  // 문자 수신동의 날짜

	@Column
	private String leaveReason; //탈퇴 사유
	@Column
	private LocalDate leaveDate; // 회원 탈퇴 날짜

	@Column
	@ColumnDefault("'N'")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private IsYn realNameYn = IsYn.N; // 실명 확인 여부 //지우기

	@Column
	private String di; //나이스 본인인증 DI 값
	@Column
	private String ci; //나이스 본인인증 CI 값

	@Column
	private LocalDateTime recentLoginDate; //최종 접속 일시
	@Column
	private String ipAddress; //접속 IP
	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@OneToMany(
		mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<MemberGroup> groups = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<MenuPermission> menuPermissions = new HashSet<>();

	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
	private Account account;

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Patient> patients = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<OrderPatient> orderPatients = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Wallet> wallets = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<CareOrder> careOrders = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private final Set<Review> reviews = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference //연관관계 주인 반대 Entity 에 선언, 정상적으로 직렬화 수행
	private Set<Apply> applys = new HashSet<>();

	/* =================================================================
	 * Relation method
	   ================================================================= */
	public void addGroups(final MemberGroup group) {
		this.groups.add(group);
		group.setMember(this);
	}

	public void removeGroup(final String groupKey) {
		MemberGroup MemberGroup =
			groups.stream()
				.filter(g -> g.getGroupKey().equals(groupKey))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);

		this.groups.remove(MemberGroup);
	}

	public void removeGroup(final MemberGroup MemberGroup) {
		this.groups.remove(MemberGroup);
	}

	private void removeAllGroups(Set<MemberGroup> MemberGroups) {
		if (MemberGroups == null) {
			throw new BizException("group key 가 없습니다.");
		}
		this.groups.removeAll(MemberGroups);
	}

	public void removeAllGroups() {
		this.groups.removeAll(this.groups);
	}

	public void addRoles(final MenuPermission menuPermission) {
		this.menuPermissions.add(menuPermission);
		menuPermission.setMember(this);
	}

	public void addReview(Review review) {
		this.reviews.add(review);
		review.setMember(this);
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	/* =================================================================
	 * Default columns
	   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

  /* =================================================================
   * Business
     ================================================================= */

	private boolean isExistGroups() {
		return CollectionUtils.isNotEmpty(this.groups);
	}

	/**
	 * Keycloak 사용자 생성
	 */
	public void createUserKeyCloak(String providerId, String providerUsername)
		throws KeyCloakRequestException {
		KeycloakHandler keycloakHandler = KeycloakHolder.get();
		UserRepresentation userRepresentation =
			keycloakHandler.createUser(this.getUserRepresentation(providerId, providerUsername));
		this.id = userRepresentation.getId();
		if (isExistGroups()) {
			keycloakHandler.usersJoinGroups(this.id, this.getGroups());
		}
	}

	/**
	 * Keycloak UserRepresentation
	 */
	public UserRepresentation getUserRepresentation(String providerId, String providerUsername) {
		UserRepresentation userRepresentation = new UserRepresentation();
		String userNm = this.userNm;
		if (userNm != null) {
			String[] userNmSplit = userNm.split(" ");
			String firstName = userNmSplit[0];
			String lastName = userNmSplit.length > 1 ? userNmSplit[1] : userNmSplit[0];
			userRepresentation.setFirstName(firstName);
			userRepresentation.setLastName(lastName);
		}

		userRepresentation.setId(this.id);
		userRepresentation.setUsername(Optional.ofNullable(this.userId).orElseGet(() -> this.email));
		userRepresentation.setEmail(this.email);
		userRepresentation.setEnabled(true);

		if (!MemberRegisterType.GENERAL.equals(this.memberRegisterType) && StringUtils.isNotBlank(
			providerUsername)) {
			String socialProvider = this.memberRegisterType.getCode().toLowerCase();

			FederatedIdentityRepresentation federatedIdentityRepresentation = new FederatedIdentityRepresentation();
			federatedIdentityRepresentation.setUserId(providerId);
			federatedIdentityRepresentation.setUserName(providerUsername);
			federatedIdentityRepresentation.setIdentityProvider(socialProvider);
			userRepresentation.setFederatedIdentities(List.of(federatedIdentityRepresentation));
			userRepresentation.setEmailVerified(true);
		}

		if (!isPasswordEncoded()) {
			CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
			credentialRepresentation.setType("password");
			credentialRepresentation.setValue(this.userPw);
			userRepresentation.setCredentials(List.of(credentialRepresentation));
		}

		return userRepresentation;
	}

	public void updateUserKeyCloak() {
		KeycloakHandler keycloakHandler = KeycloakHolder.get();
		keycloakHandler.updateUser(this.getUserRepresentation(null, null));
		if (isExistGroups()) {
			keycloakHandler.usersJoinGroups(this.id, this.getGroups());
		}
	}

	// 현재 비밀번호 확인
	public boolean equalsPassword(String password) {
		return KeycloakHolder.get().getPasswordEncoder().matches(StringUtils.defaultString(password), this.userPw);
	}

	public boolean isPasswordEncoded() {
		return StringUtils.defaultString(this.userPw).startsWith("{bcrypt}");
	}

	public void passwordEncode() {
		if (!isPasswordEncoded()) {
			this.userPw = KeycloakHolder.get().getPasswordEncoder().encode(this.userPw);
		}
	}

	public void update(MemberUpdateRequest memberUpdateRequest) {
		this.userNm = memberUpdateRequest.getUserNm();
		this.telNo = memberUpdateRequest.getTelNo();
		this.gender = memberUpdateRequest.getGender();
		this.useYn = memberUpdateRequest.getUseYn();
		this.userType = memberUpdateRequest.getUserType();
		this.address1 = memberUpdateRequest.getAddress1();
		this.address2 = memberUpdateRequest.getAddress2();
		this.domesticForeignCode = memberUpdateRequest.getDomesticForeignCode();
		this.careerCode = memberUpdateRequest.getCareerCode();
		this.careStartYear = memberUpdateRequest.getCareStartYear();
		this.introduce = memberUpdateRequest.getIntroduce();
		this.marketingEmailDate = marketingDate(memberUpdateRequest.isMarketingEmail());
		this.marketingAppPushDate = marketingDate(memberUpdateRequest.isMarketingAppPush());
		this.marketingKakaoDate = marketingDate(memberUpdateRequest.isMarketingKakao());
		this.marketingSmsDate = marketingDate(memberUpdateRequest.isMarketingSms());
		this.removeAllGroups(
			this.groups.stream()
				.filter(
					memberGroup ->
						memberUpdateRequest.getGroupKeys().stream()
							.filter(memberGroup::equalsGroupKey)
							.findAny()
							.isEmpty())
				.collect(Collectors.toSet()));

		for (String groupKey : memberUpdateRequest.getGroupKeys()) {
			Optional<MemberGroup> optional =
				this.groups.stream().filter(g -> g.getGroupKey().equals(groupKey)).findFirst();

			if (optional.isEmpty()) {
				this.addGroups(MemberGroup.builder().groupKey(groupKey).build());
			}
		}
	}

	public LocalDate marketingDate(boolean isMarketing) {
		return isMarketing ? LocalDate.now() : null;
	}

	public MemberResponse toResponse() {
		MemberResponse memberResponse = MemberResponse.builder()
			.memberId(this.id)
			.userId(this.userId)
			.userNm(this.userNm)
			.telNo(this.telNo)
			.email(this.email)
			.gender(this.gender)
			.useYn(this.useYn)
			.userType(this.userType)
			.memberRegisterType(this.memberRegisterType)
			.address1(this.address1)
			.address2(this.address2)
			.domesticForeignCode(this.domesticForeignCode)
			.careerCode(this.careerCode)
			.careStartYear(this.careStartYear)
			.introduce(this.introduce)
			.marketingEmail(
				this.marketingEmailDate != null && this.marketingEmailDate.isBefore(LocalDate.now()))
			.marketingAppPush(
				this.marketingAppPushDate != null && this.marketingAppPushDate.isBefore(
					LocalDate.now()))
			.marketingKakao(
				this.marketingKakaoDate != null && this.marketingKakaoDate.isBefore(LocalDate.now()))
			.marketingSms(
				this.marketingSmsDate != null && this.marketingSmsDate.isBefore(LocalDate.now()))
			.realNameYn(this.realNameYn)
			.recentLoginDate(this.recentLoginDate)
			.ipAddress(this.ipAddress)
			.build();

		memberResponse.setGroups(
			this.groups.stream().map(MemberGroup::converterDto).collect(Collectors.toSet()));
		memberResponse.setCreatedAndUpdated(this.createdAndModified);
		return memberResponse;
	}

	public PartnerMemberResponse toPartnerMemberResponse() {
		PartnerMemberResponse partnerMemberResponse = PartnerMemberResponse.builder()
			.memberId(this.id)
			.userNm(this.userNm)
			.telNo(this.telNo)
			.age(calculateAgeFromJumin(this.jumin1))
			.email(this.email)
			.memberRegisterType(this.memberRegisterType)
			.gender(getGenderDescription())
			.registerDate(this.createdAndModified.getCreatedOn().toLocalDate())
			.picture(this.picture)
			.career(calculateYearsFromStartYear(this.careStartYear))
			.matchingCount((int)this.careOrders.stream()
				.filter(order -> order.getOrderStatus() == OrderStatus.ORDER_COMPLETE)
				.count())
			.starAverage(this.reviews.size())
			.address1(this.address1)
			.address2(this.address2)
			.introduce(this.introduce)
			.build();
		partnerMemberResponse.setCreatedAndUpdated(this.createdAndModified);
		return partnerMemberResponse;
	}

	public GuardianMemberResponse toGuardianMemberResponse() {
		GuardianMemberResponse build = GuardianMemberResponse.builder()
			.memberId(this.id)
			.userNm(this.userNm)
			.telNo(this.telNo)
			.email(this.email)
			.memberRegisterType(this.memberRegisterType)
			.memberType(this.memberType)
			.registerDate(this.createdAndModified.getCreatedOn().toLocalDate())
			.build();
		build.setCreatedAndUpdated(this.createdAndModified);
		return build;
	}

	public void setLeaveMember(String leaveReason) {
		this.userId = "DEL_".concat(this.userId);
		this.email = this.email != null ? "DEL_".concat(this.email) : null;
		if (StringUtils.isNotEmpty(leaveReason)) {
			this.leaveReason = leaveReason;
		}
		this.leaveDate = LocalDate.now();
		this.useYn = UseYn.N;
	}

	public void updateAdminMember(AdminMemberUpdate adminMemberUpdate) {
		if (isUpdateUserPw(adminMemberUpdate.getNewUserPw(), adminMemberUpdate.getChkUserPw())) { // 비밀번호 변경의사 있음
			if (!isUpdatableUserPw(adminMemberUpdate.getNewUserPw(),
				adminMemberUpdate.getChkUserPw())) { // 모든 비밀번호 영역 값 입력 확인
				throw new BizException("모든 비밀번호를 입력해주세요.");
			}
			if (!isEqualsNewUserPw(adminMemberUpdate.getNewUserPw(),
				adminMemberUpdate.getChkUserPw())) { // 새 비밀번호와 확인의 동일 확인
				throw new BizException("새 비밀번호와 비밀번호 확인의 내용이 다릅니다.");
			}
			this.userPw = adminMemberUpdate.getNewUserPw();
		}

		this.userNm = adminMemberUpdate.getUserNm();
		this.telNo = adminMemberUpdate.getTelNo();
		this.userType =
			adminMemberUpdate.getDeptCode().equals(DeptCode.DEVELOPER) ? UserType.SYS_ADMIN : UserType.SYS_USER;
		this.deptCode = adminMemberUpdate.getDeptCode();

		this.removeAllGroups(
			this.groups.stream()
				.filter(
					memberGroup ->
						adminMemberUpdate.getGroupKeys().stream()
							.filter(memberGroup::equalsGroupKey)
							.findAny()
							.isEmpty())
				.collect(Collectors.toSet()));

		for (String groupKey : adminMemberUpdate.getGroupKeys()) {
			Optional<MemberGroup> optional =
				this.groups.stream().filter(g -> g.getGroupKey().equals(groupKey)).findFirst();

			if (optional.isEmpty()) {
				this.addGroups(MemberGroup.builder().groupKey(groupKey).build());
			}
		}
	}

	public void updatePartnerMember(PartnerMemberUpdate partnerMemberUpdate) {
		if (isUpdateUserPw(partnerMemberUpdate.getNewUserPw(), partnerMemberUpdate.getChkUserPw())) { // 비밀번호 변경의사 있음
			if (!isUpdatableUserPw(partnerMemberUpdate.getNewUserPw(),
				partnerMemberUpdate.getChkUserPw())) { // 모든 비밀번호 영역 값 입력 확인
				throw new BizException("모든 비밀번호를 입력해주세요.");
			}
			if (!isEqualsNewUserPw(partnerMemberUpdate.getNewUserPw(),
				partnerMemberUpdate.getChkUserPw())) { // 새 비밀번호와 확인의 동일 확인
				throw new BizException("새 비밀번호와 비밀번호 확인의 내용이 다릅니다.");
			}
			this.userPw = partnerMemberUpdate.getNewUserPw();
		}

		this.telNo = partnerMemberUpdate.getTelNo();
		this.gender = partnerMemberUpdate.getGender();
		this.address1 = partnerMemberUpdate.getAddress1();
		this.address2 = partnerMemberUpdate.getAddress2();
		this.domesticForeignCode = partnerMemberUpdate.getDomesticForeignCode();
		this.careerCode = partnerMemberUpdate.getCareerCode();
		this.careStartYear = partnerMemberUpdate.getCareStartYear();
		this.introduce = partnerMemberUpdate.getIntroduce();

		setMarketingDate(partnerMemberUpdate.isMarketingEmail(),
			partnerMemberUpdate.isMarketingAppPush(),
			partnerMemberUpdate.isMarketingKakao(),
			partnerMemberUpdate.isMarketingSms());

		this.removeAllGroups(
			this.groups.stream()
				.filter(
					memberGroup ->
						partnerMemberUpdate.getGroupKeys().stream()
							.filter(memberGroup::equalsGroupKey)
							.findAny()
							.isEmpty())
				.collect(Collectors.toSet()));

		for (String groupKey : partnerMemberUpdate.getGroupKeys()) {
			Optional<MemberGroup> optional =
				this.groups.stream().filter(g -> g.getGroupKey().equals(groupKey)).findFirst();

			if (optional.isEmpty()) {
				this.addGroups(MemberGroup.builder().groupKey(groupKey).build());
			}
		}
	}

	public void updateGuardianMember(GuardianMemberUpdate guardianMemberUpdate) {
		if (isUpdateUserPw(guardianMemberUpdate.getNewUserPw(), guardianMemberUpdate.getChkUserPw())) { // 비밀번호 변경의사 있음
			if (!isUpdatableUserPw(guardianMemberUpdate.getNewUserPw(),
				guardianMemberUpdate.getChkUserPw())) { // 모든 비밀번호 영역 값 입력 확인
				throw new BizException("모든 비밀번호를 입력해주세요.");
			}
			if (!isEqualsNewUserPw(guardianMemberUpdate.getNewUserPw(),
				guardianMemberUpdate.getChkUserPw())) { // 새 비밀번호와 확인의 동일 확인
				throw new BizException("새 비밀번호와 비밀번호 확인의 내용이 다릅니다.");
			}
			this.userPw = guardianMemberUpdate.getNewUserPw();
		}

		this.telNo = guardianMemberUpdate.getTelNo();

		setMarketingDate(guardianMemberUpdate.isMarketingEmail(),
			guardianMemberUpdate.isMarketingAppPush(),
			guardianMemberUpdate.isMarketingKakao(),
			guardianMemberUpdate.isMarketingSms());
	}

	private void setMarketingDate(boolean isMarketingEmail,
		boolean isMarketingAppPush,
		boolean isMarketingKakao,
		boolean isMarketingSms) {
		if (isMarketingEmail && this.marketingEmailDate == null) {
			this.marketingEmailDate = LocalDate.now();
		} else if (!isMarketingEmail && this.marketingEmailDate != null) {
			this.marketingEmailDate = null;
		}
		if (isMarketingAppPush && this.marketingAppPushDate == null) {
			this.marketingAppPushDate = LocalDate.now();
		} else if (!isMarketingAppPush && this.marketingAppPushDate != null) {
			this.marketingAppPushDate = null;
		}
		if (isMarketingKakao && this.marketingKakaoDate == null) {
			this.marketingKakaoDate = LocalDate.now();
		} else if (!isMarketingKakao && this.marketingKakaoDate != null) {
			this.marketingKakaoDate = null;
		}
		if (isMarketingSms && this.marketingSmsDate == null) {
			this.marketingSmsDate = LocalDate.now();
		} else if (!isMarketingSms && this.marketingSmsDate != null) {
			this.marketingSmsDate = null;
		}
	}

	public void passUpdate(PassSaveRequest passSaveRequest) {
		this.userNm = passSaveRequest.getName();
		this.jumin1 = passSaveRequest.getBirthdate().substring(2); //951011
		// gender 값이 0이면 "여성", 1이면 "남성"으로 변경
		if ("0".equals(passSaveRequest.getGender())) {
			this.gender = GenderCode.F;
		} else if ("1".equals(passSaveRequest.getGender())) {
			this.gender = GenderCode.M;
		}
		//0 내국인, 1 외국인
		if ("0".equals(passSaveRequest.getNationalInfo())) {
			this.domesticForeignCode = DomesticForeignCode.D;
		} else if ("1".equals(passSaveRequest.getGender())) {
			this.domesticForeignCode = DomesticForeignCode.F;
		}
		this.di = passSaveRequest.getDupInfo();
		this.ci = passSaveRequest.getConnInfo();
	}

	public void updateRecentLogin() {
		this.recentLoginDate = LocalDateTime.now();
		this.ipAddress = getUserIp();
	}

	public String getGenderDescription() {
		if (this.gender == null) {
			return null;
		}
		return switch (this.gender) {
			case M -> "남자";
			case F -> "여자";
			default -> "알 수 없음";  // 예시로 추가된 경우, 실제 GenderCode에 따라 다를 수 있음
		};
	}

	public void setNoMemberCreate() {
		this.userType = UserType.GUARDIAN;
		this.memberType = MemberType.NO_MEMBER;
		this.id = getRandomKey("compiglab");
	}

}
