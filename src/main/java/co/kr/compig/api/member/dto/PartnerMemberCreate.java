package co.kr.compig.api.member.dto;

import co.kr.compig.common.code.CareerCode;
import co.kr.compig.common.code.DomesticForeignCode;
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
public class PartnerMemberCreate {

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
  private UserType userType = UserType.PARTNER; //사용자 타입

  @Builder.Default
  private MemberRegisterType memberRegisterType = MemberRegisterType.GENERAL; // 회원가입 유형

  @NotBlank
  private String address1; //주소

  @NotBlank
  private String address2; //주소

  private String picture; //프로필사진 s3 저장소 Path

  @NotNull
  private DomesticForeignCode domesticForeignCode; //외국인 내국인

  @NotNull
  private CareerCode careerCode; //신입 경력

  private Integer careStartYear; //근무 시작 연도

  private String introduce; //자기소개

  //TODO CREATE 계좌번호 관리 테이블

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
        .address1(this.address1)
        .address2(this.address2)
        .picture(this.picture)
        .domesticForeignCode(this.domesticForeignCode)
        .careerCode(this.careerCode)
        .careStartYear(this.careStartYear)
        .introduce(this.introduce)
        .marketingEmailDate(this.marketingEmailDate)
        .marketingAppPushDate(this.marketingAppPushDate)
        .marketingKakaoDate(this.marketingKakaoDate)
        .marketingSmsDate(this.marketingSmsDate)
        .realNameYn(this.realNameYn)
        .build();
  }
}
