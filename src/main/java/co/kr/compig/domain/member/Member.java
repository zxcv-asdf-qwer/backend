package co.kr.compig.domain.member;

import co.kr.compig.common.code.CareerCode;
import co.kr.compig.common.code.DomesticForeignCode;
import co.kr.compig.common.code.GenderCode;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.code.UserType;
import co.kr.compig.common.code.converter.UserTypeConverter;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.common.exception.KeyCloakRequestException;
import co.kr.compig.common.keycloak.KeycloakHandler;
import co.kr.compig.common.keycloak.KeycloakHolder;
import co.kr.compig.domain.permission.MenuPermission;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

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

  @Column(length = 150, updatable = false)
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

  @Column(length = 35)
  @Enumerated(EnumType.STRING)
  private MemberRegisterType memberRegisterType;  // 회원가입 유형

  @Column(length = 6)
  private String jumin1; // 주민등록번호 앞자리
  @Column(length = 150)
  private String jumin2; // 주민등록번호 뒷자리

  @Column(length = 200)
  private String address1;

  @Column(length = 200)
  private String address2;

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
  private LocalDate deletedDate; // 회원 탈퇴 날짜

  @Column
  @ColumnDefault("'N'")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private IsYn realNameYn = IsYn.N; // 실명 확인 여부

  /* =================================================================
  * Domain mapping
  ================================================================= */
  @Builder.Default
  @OneToMany(
      mappedBy = "member",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<MemberGroup> groups = new HashSet<>();

  @Builder.Default
  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<MenuPermission> menuPermissions = new HashSet<>();

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
  * Business login
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
   *
   * @return UserRepresentation
   */
  public UserRepresentation getUserRepresentation(String providerId, String providerUsername) {
    UserRepresentation userRepresentation = new UserRepresentation();
    String userNm = this.userNm;

    String[] userNmSplit = userNm.split(" ");
    String firstName = userNmSplit[0];
    String lastName = userNmSplit.length > 1 ? userNmSplit[1] : userNmSplit[0];

    userRepresentation.setId(this.id);
    userRepresentation.setUsername(Optional.ofNullable(this.userId).orElseGet(() -> this.email));
    userRepresentation.setFirstName(firstName);
    userRepresentation.setLastName(lastName);
    userRepresentation.setEmail(this.email);
    // 탈퇴 회원일 경우 keycloak 도 비 활성화 처리
    if (this.useYn.equals(UseYn.N) && this.deletedDate != null) {
      userRepresentation.setEnabled(false);
    } else {
      userRepresentation.setEnabled(true);
    }

    if (!MemberRegisterType.GENERAL.equals(this.memberRegisterType) && StringUtils.isNotBlank(
        providerUsername)) {
      String socialProvider = this.memberRegisterType.getCode().toLowerCase();

      FederatedIdentityRepresentation federatedIdentityRepresentation = new FederatedIdentityRepresentation();
      federatedIdentityRepresentation.setUserId(providerId);
      federatedIdentityRepresentation.setUserName(providerUsername);
      federatedIdentityRepresentation.setIdentityProvider(socialProvider);
      userRepresentation.setFederatedIdentities(List.of(federatedIdentityRepresentation));
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
}
