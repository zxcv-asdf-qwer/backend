package co.kr.compig.domain.member;

import co.kr.compig.common.code.*;
import co.kr.compig.common.code.converter.UserTypeConverter;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import co.kr.compig.domain.role.Role;
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
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

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

  @Column(length = 100)
  private String userNmEn; // 사용자 영문명

  @Column(length = 150)
  private String email; // 이메일

  @Column(length = 1)
  @ColumnDefault("0")
  @Builder.Default
  private Integer loginFailCnt = 0; // 로그인 실패 횟수

  @Column(length = 150)
  private String otpNo; // OTP No

  @Column(length = 1)
  @ColumnDefault("'N'")
  @Builder.Default
  private String otpYn = "N"; // OTP 사용유무

  @Column(length = 100)
  private String telNo; // 전화번호

  @Column(length = 2)
  private String deptCd; // 부서코드

  @Column(length = 3)
  @Convert(converter = UserTypeConverter.class)
  private UserType userType; // 사용자 구분

  @Column(length = 20)
  private String zipcode;

  @Column(length = 200)
  private String address1;

  @Column(length = 200)
  private String address2;

  @Column(length = 1)
  @ColumnDefault("'Y'")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private UseYn useYn = UseYn.Y; // 사용유무

  @Column(length = 35)
  @Enumerated(EnumType.STRING)
  private MemberRegisterType memberRegisterType;  // 회원가입 유형

  @Column
  private LocalDate deletedDate; // 회원 탈퇴 날짜
  @Column
  private LocalDate marketingEmailDate;  // 이메일 수신동의 날짜
  @Column
  private LocalDate marketingSmsDate;  // 문자 수신동의 날짜
  @Column
  private LocalDate marketingKakaoDate;  // 알림톡 수신동의 날짜
  @Column
  private LocalDate marketingAppDate; // 앱 푸시알림 수신동의 날짜
  @Column
  private LocalDate marketingThirdDate; // 제 3자 정보 제공 동의 날짜

  @Column
  @ColumnDefault("'N'")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private IsYn realNameYn = IsYn.N; // 실명 확인 여부

  @Column
  @Enumerated(EnumType.STRING)
  private GenderCode gender; // 성별

  @Column(length = 6)
  private String jumin1; // 주민등록번호 앞자리
  @Column(length = 150)
  private String jumin2; // 주민등록번호 뒷자리

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
  private Set<Role> roles = new HashSet<>();

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

  public void addRoles(final Role role) {
    this.roles.add(role);
    role.setMember(this);
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

}
