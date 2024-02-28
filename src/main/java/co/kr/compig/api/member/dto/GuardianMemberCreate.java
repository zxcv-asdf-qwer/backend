package co.kr.compig.api.member.dto;

import co.kr.compig.common.code.GenderCode;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.code.UserType;
import co.kr.compig.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianMemberCreate {

  @NotBlank
  @Length(min = 2, max = 100)
  @Pattern(regexp = "^[\\sㄱ-ㅎ가-힣A-Za-z0-9_-]{2,100}$")
  private String userNm; // 사용자 명

  @NotBlank
  @Length(min = 3, max = 15)
  @Pattern(regexp = "^[A-Za-z0-9_]{3,15}$")
  private String userId; // 사용자 아이디

  @NotBlank
  private String userPw; // 사용자 비밀번호

  @NotBlank
  private String telNo; // 연락처

  @NotBlank
  private String email; // 이메일

  @NotNull
  private GenderCode gender; // 성별

  @Builder.Default
  private UseYn useYn = UseYn.Y; // 사용유무

  @Builder.Default
  private UserType userType = UserType.GUARDIAN; //사용자 타입

  @Builder.Default
  private MemberRegisterType memberRegisterType = MemberRegisterType.GENERAL; // 회원가입 유형

  private LocalDate marketingEmailDate;  // 이메일 수신동의 날짜
  private LocalDate marketingAppPushDate; // 앱 푸시알림 수신동의 날짜
  private LocalDate marketingKakaoDate;  // 알림톡 수신동의 날짜
  private LocalDate marketingSmsDate;  // 문자 수신동의 날짜

  private IsYn realNameYn; // 실명 확인 여부

  public Member convertEntity() {
    return Member.builder()
        .userNm(this.userNm)
        .userId(this.userId)
        .userPw(this.userPw)
        .telNo(this.telNo)
        .email(this.email)
        .gender(this.gender)
        .useYn(this.useYn)
        .userType(this.userType)
        .memberRegisterType(this.memberRegisterType)
        .marketingEmailDate(this.marketingEmailDate)
        .marketingAppPushDate(this.marketingAppPushDate)
        .marketingKakaoDate(this.marketingKakaoDate)
        .marketingSmsDate(this.marketingSmsDate)
        .realNameYn(this.realNameYn)
        .build();
  }
}
