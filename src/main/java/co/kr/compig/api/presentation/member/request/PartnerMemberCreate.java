package co.kr.compig.api.presentation.member.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import co.kr.compig.api.domain.code.CareerCode;
import co.kr.compig.api.domain.code.DomesticForeignCode;
import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.domain.code.UserType;
import co.kr.compig.api.domain.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	@Email
	private String email; // 이메일

	@NotBlank
	private String userPw; // 사용자 비밀번호

	@NotBlank
	private String telNo; // 연락처

	@NotNull
	private GenderCode gender; // 성별

	@NotBlank
	private String address1; //주소

	@NotBlank
	private String address2; //주소

	@NotNull
	private DomesticForeignCode domesticForeignCode; //외국인 내국인

	@NotNull
	private CareerCode careerCode; //신입 경력

	private Integer careStartYear; //근무 시작 연도

	private String introduce; //자기소개

	private boolean marketingEmail; // 이메일 수신동의
	private boolean marketingAppPush; // 앱 푸시알림 수신동의
	private boolean marketingKakao; // 알림톡 수신동의
	private boolean marketingSms; // 문자 수신동의

	@NotBlank
	private String ci; //나이스 본인인증 ci 값

	public Member convertEntity() {
		return Member.builder()
			.userNm(this.userNm)
			.email(this.email)
			.userPw(this.userPw)
			.telNo(this.telNo)
			.gender(this.gender)
			.useYn(UseYn.Y)
			.userType(UserType.PARTNER)
			.memberRegisterType(MemberRegisterType.GENERAL)
			.address1(this.address1)
			.address2(this.address2)
			.domesticForeignCode(this.domesticForeignCode)
			.careerCode(this.careerCode)
			.careStartYear(this.careStartYear)
			.introduce(this.introduce)
			.marketingEmailDate(this.marketingEmail ? LocalDate.now() : null)
			.marketingAppPushDate(this.marketingAppPush ? LocalDate.now() : null)
			.marketingKakaoDate(this.marketingKakao ? LocalDate.now() : null)
			.marketingSmsDate(this.marketingSms ? LocalDate.now() : null)
			.ci(this.ci)
			.build();
	}
}
