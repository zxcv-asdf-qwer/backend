package co.kr.compig.api.domain.member;

import static co.kr.compig.global.utils.CalculateUtil.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import co.kr.compig.api.domain.account.Account;
import co.kr.compig.api.domain.code.CareerCode;
import co.kr.compig.api.domain.code.DeptCode;
import co.kr.compig.api.domain.code.DomesticForeignCode;
import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.code.OrderStatusCode;
import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.domain.code.UserType;
import co.kr.compig.api.domain.code.converter.DeptCodeConverter;
import co.kr.compig.api.domain.code.converter.UserTypeConverter;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.patient.Patient;
import co.kr.compig.api.domain.permission.MenuPermission;
import co.kr.compig.api.domain.wallet.Wallet;
import co.kr.compig.api.presentation.member.request.MemberUpdateRequest;
import co.kr.compig.api.presentation.member.response.AdminMemberResponse;
import co.kr.compig.api.presentation.member.response.GuardianMemberResponse;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.api.presentation.member.response.PartnerMemberResponse;
import co.kr.compig.api.presentation.member.response.UserMainSearchResponse;
import co.kr.compig.global.embedded.CreatedAndUpdated;
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
		@UniqueConstraint(name = "uk01_member", columnNames = {"userId"})
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

	@Column(columnDefinition = "TEXT")
	private String ci; //나이스 본인인증 CI 값

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@OneToMany(
		mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MemberGroup> groups = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<MenuPermission> menuPermissions = new HashSet<>();

	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
	private Account account;

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Patient> patients = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<OrderPatient> orderPatients = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Wallet> wallets = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<CareOrder> careOrders = new HashSet<>();

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
		this.groups.removeAll(MemberGroups);
	}

	public void removeAllGroups() {
		this.groups.removeAll(this.groups);
	}

	public void addRoles(final MenuPermission menuPermission) {
		this.menuPermissions.add(menuPermission);
		menuPermission.setMember(this);
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

	public void updateUserKeyCloak() {
		KeycloakHandler keycloakHandler = KeycloakHolder.get();
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
		// 탈퇴 회원일 경우 keycloak 도 비 활성화 처리
		userRepresentation.setEnabled(!this.useYn.equals(UseYn.N) || this.leaveDate == null); //TODO 제거

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
	}

	public LocalDate marketingDate(boolean isMarketing) {
		return isMarketing ? LocalDate.now() : null;
	}

	public MemberResponse toResponse() {
		return MemberResponse.builder()
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
			.build();

	}

	public AdminMemberResponse toAdminMemberResponse() {
		return AdminMemberResponse.builder()
			.memberId(this.id)
			.userNm(this.userNm)
			.userId(this.userId)
			.deptCode(this.deptCode)
			.email(this.email)
			.telNo(this.telNo)
			.build();
	}

	public PartnerMemberResponse toPartnerMemberResponse() {
		return PartnerMemberResponse.builder()
			.memberId(this.id)
			.userNm(this.userNm)
			.telNo(this.telNo)
			.age(calculateAgeFromJumin(this.jumin1))
			.email(this.email)
			.memberRegisterType(this.memberRegisterType)
			.gender(this.gender.equals(GenderCode.M) ? "남자" : "여자")
			.registerDate(this.createdAndModified.getCreatedOn().toLocalDate())
			.picture(this.picture)
			.career(calculateYearsFromStartYear(this.careStartYear))
			.matchingCount((int)this.careOrders.stream()
				.filter(order -> order.getOrderStatus() == OrderStatusCode.ORDER_COMPLETE)
				.count())
			.starAverage(this.id) //TODO 리뷰 생기면 계산 로직 추가
			.address1(this.address1)
			.address2(this.address2)
			.introduce(this.introduce)
			.build();
	}

	public GuardianMemberResponse toGuardianMemberResponse() {
		return GuardianMemberResponse.builder()
			.memberId(this.id)
			.userNm(this.userNm)
			.telNo(this.telNo)
			.email(this.email)
			.memberRegisterType(this.memberRegisterType)
			.registerDate(this.createdAndModified.getCreatedOn().toLocalDate())
			.build();
	}

	public UserMainSearchResponse toUserMainSearchResponse() {
		return UserMainSearchResponse.builder()
			.memberId(this.id)
			.userNm(this.userNm)
			.telNo(this.telNo)
			.build();
	}

	public void setLeaveMember(String leaveReason) {
		this.userId = "DEL_".concat(this.userId);
		this.email = this.email != null ? "DEL_".concat(this.email) : null;
		this.leaveReason = leaveReason;
		this.leaveDate = LocalDate.now();
		this.useYn = UseYn.N;
	}
}
